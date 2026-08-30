package duke;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;

/** Processes Furina commands independently of the interface used to enter them. */
public class CommandHandler {
    private final ArrayList<Task> tasks;

    /** Creates a handler containing the tasks saved by the user. */
    public CommandHandler() {
        tasks = TaskStorage.load();
    }

    /**
     * Processes one command and returns the response text without console separators.
     *
     * @param command the command entered by the user
     * @return the response, or an empty string for blank input
     */
    public String handleCommand(String command) {
        if (command == null || command.isBlank()) {
            return "";
        }
        if (command.equals("bye")) {
            return "Bye. Hope to see you again soon!";
        }
        if (command.equals("list")) {
            return listTasks();
        }
        if (isCommand(command, "find")) {
            return findTasks(command);
        }
        if (isCommand(command, "delete")) {
            return deleteTask(command);
        }
        if (isCommand(command, "mark")) {
            return updateTaskStatus(command, true);
        }
        if (isCommand(command, "unmark")) {
            return updateTaskStatus(command, false);
        }
        try {
            Task newTask = createTask(command);
            tasks.add(newTask);
            String response = "Got it. I've added this task:\n"
                    + newTask + "\nNow you have " + tasks.size() + " tasks in the list.";
            saveTasks();
            return response;
        } catch (IllegalArgumentException exception) {
            return "OOPS!!! " + exception.getMessage();
        }
    }

    private String listTasks() {
        StringBuilder response = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            response.append("\n").append(i + 1).append(".").append(tasks.get(i));
        }
        return response.toString();
    }

    private String findTasks(String command) {
        String keyword = command.length() == 4 ? "" : command.substring(5).trim();
        if (keyword.isBlank()) {
            return "OOPS!!! Please provide a keyword to search for.";
        }
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        StringBuilder response = new StringBuilder("Here are the matching tasks in your list:");
        boolean hasMatch = false;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.description.toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                response.append("\n").append(i + 1).append(".").append(task);
                hasMatch = true;
            }
        }
        if (!hasMatch) {
            response.append("\nNo matching tasks found.");
        }
        return response.toString();
    }

    private String updateTaskStatus(String command, boolean isDone) {
        String[] commandParts = command.trim().split("\\s+");
        if (commandParts.length != 2) {
            return "OOPS!!! Please provide a task number, for example: "
                    + (isDone ? "mark 2." : "unmark 2.");
        }
        try {
            int taskIndex = Integer.parseInt(commandParts[1]) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                return "OOPS!!! There is no task with that number.";
            }
            Task task = tasks.get(taskIndex);
            if (isDone) {
                task.markAsDone();
            } else {
                task.markAsNotDone();
            }
            saveTasks();
            return (isDone ? "Nice! I've marked this task as done:\n" :
                    "OK, I've marked this task as not done yet:\n") + task;
        } catch (NumberFormatException exception) {
            return "OOPS!!! Task numbers must be positive whole numbers.";
        }
    }

    private String deleteTask(String command) {
        String[] commandParts = command.trim().split("\\s+");
        if (commandParts.length != 2) {
            return "OOPS!!! Please provide a task number, for example: delete 2.";
        }
        try {
            int taskIndex = Integer.parseInt(commandParts[1]) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                return "OOPS!!! There is no task with that number.";
            }
            Task deletedTask = tasks.remove(taskIndex);
            saveTasks();
            return "Noted. I've removed this task:\n" + deletedTask
                    + "\nNow you have " + tasks.size() + " tasks in the list.";
        } catch (NumberFormatException exception) {
            return "OOPS!!! Task numbers must be positive whole numbers.";
        }
    }

    private Task createTask(String command) {
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
                try {
                    return new Task(parts[0].trim(), DateTimeParser.parse(parts[1].trim()));
                } catch (DateTimeParseException ignored) {
                    return new Task(TaskType.DEADLINE, parts[0].trim(), parts[1].trim(), null, null);
                }
            }
            throw new IllegalArgumentException("A deadline needs a description and a date after /by.");
        }
        if (isCommand(command, "event")) {
            String[] parts = command.substring(5).trim().split("\\s+/from\\s+|\\s+/to\\s+", 3);
            if (parts.length == 3 && !parts[0].isBlank() && !parts[1].isBlank() && !parts[2].isBlank()) {
                return new Task(TaskType.EVENT, parts[0].trim(), null, parts[1].trim(), parts[2].trim());
            }
            throw new IllegalArgumentException(
                    "An event needs a description, a start time after /from, and an end time after /to.");
        }
        throw new IllegalArgumentException("I don't recognize that command.");
    }

    private boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }

    private void saveTasks() {
        TaskStorage.save(tasks);
    }
}
