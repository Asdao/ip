import java.util.ArrayList;

/**
 * Entry point for the Furina chatbot.
 */
class Furina {
    static void main(String[] args) {
        ArrayList<Task> tasks = TaskStorage.load();
        Ui ui = new Ui();

        ui.showWelcome();

        String command;
        while ((command = ui.readCommand()) != null) {

            if (command.isBlank()) {
                continue;
            }

            if (command.equals("bye")) {
                break;
            }

            ui.showLine();

            if (command.equals("list")) {
                ui.showTaskList(tasks);
            } else if (isCommand(command, "delete")) {
                deleteTask(command, tasks, ui);
            } else if (isCommand(command, "mark")) {
                updateTaskStatus(command, tasks, true, ui);
            } else if (isCommand(command, "unmark")) {
                updateTaskStatus(command, tasks, false, ui);
            } else {
                try {
                    Task newTask = createTask(command);
                    tasks.add(newTask);
                    saveTasks(tasks, ui);
                    ui.showAddedTask(newTask, tasks.size());
                } catch (IllegalArgumentException exception) {
                    ui.showError(exception.getMessage());
                }
            }

            ui.showLine();
        }

        ui.showGoodbye();
    }

    /** Saves after a mutation and reports storage failures without stopping the chatbot. */
    private static void saveTasks(ArrayList<Task> tasks, Ui ui) {
        if (!TaskStorage.save(tasks)) {
            ui.showError("I couldn't save the task list.");
        }
    }

    /**
     * Updates the completion status of a task from a command such as
     * "mark 2" or "unmark 2".
     */
    private static void updateTaskStatus(String command, ArrayList<Task> tasks,
                                         boolean isDone, Ui ui) {
        String[] commandParts = command.trim().split("\\s+");
        if (commandParts.length != 2) {
            ui.showError("Please provide a task number, for example: mark 2.");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(commandParts[1]);
            int taskIndex = taskNumber - 1;

            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                ui.showError("There is no task with that number.");
                return;
            }

            if (isDone) {
                tasks.get(taskIndex).markAsDone();
                saveTasks(tasks, ui);
                ui.showUpdatedTask(tasks.get(taskIndex), true);
            } else {
                tasks.get(taskIndex).markAsNotDone();
                saveTasks(tasks, ui);
                ui.showUpdatedTask(tasks.get(taskIndex), false);
            }
        } catch (NumberFormatException exception) {
            ui.showError("Task numbers must be positive whole numbers.");
        }
    }

    /**
     * Removes a task using a command such as "delete 3".
     */
    private static void deleteTask(String command, ArrayList<Task> tasks, Ui ui) {
        String[] commandParts = command.trim().split("\\s+");
        if (commandParts.length != 2) {
            ui.showError("Please provide a task number, for example: delete 2.");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(commandParts[1]);
            int taskIndex = taskNumber - 1;

            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                ui.showError("There is no task with that number.");
                return;
            }

            Task deletedTask = tasks.remove(taskIndex);
            saveTasks(tasks, ui);
            ui.showDeletedTask(deletedTask, tasks.size());
        } catch (NumberFormatException exception) {
            ui.showError("Task numbers must be positive whole numbers.");
        }
    }

    /**
     * Checks whether an input is a command by itself or starts with that
     * command followed by arguments.
     */
    private static boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }

    /**
     * Creates a task from a todo, deadline, event, or plain-text command.
     */
    private static Task createTask(String command) {
        if (isCommand(command, "todo")) {
            String description = command.length() == 4 ? "" : command.substring(5).trim();
            if (description.isBlank()) {
                throw new IllegalArgumentException("A todo task needs a description.");
            }
            return new Task(TaskType.TODO, description, null, null, null);
        }

        if (isCommand(command, "deadline")) {
            String[] parts = command.substring(8).trim().split("\\s+/by\\s+", 2);
            if (parts.length == 2 && !parts[0].isBlank() && !parts[1].isBlank()) {
                return new Task(TaskType.DEADLINE, parts[0].trim(), parts[1].trim(), null, null);
            }
            throw new IllegalArgumentException(
                    "A deadline needs a description and a date after /by.");
        }

        if (isCommand(command, "event")) {
            String eventDetails = command.substring(5).trim();
            String[] parts = eventDetails.split("\\s+/from\\s+|\\s+/to\\s+", 3);
            if (parts.length == 3 && !parts[0].isBlank()
                    && !parts[1].isBlank() && !parts[2].isBlank()) {
                String description = parts[0].trim();
                String from = parts[1].trim();
                String to = parts[2].trim();
                return new Task(TaskType.EVENT, description, null, from, to);
            }
            throw new IllegalArgumentException(
                    "An event needs a description, a start time after /from, and an end time after /to.");
        }

        throw new IllegalArgumentException("I don't recognize that command.");
    }
}
