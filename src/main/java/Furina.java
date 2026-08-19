import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point for the Furina chatbot.
 */
class Furina {
    static void main(String[] args) {
        String separator = "____________________________________________________________";
        String banner = "    F U R I N A";
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello! I'm Furina.");
        System.out.println("What can I do for you?");
        System.out.println(separator);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.isBlank()) {
                continue;
            }

            if (command.equals("bye")) {
                break;
            }

            System.out.println(separator);

            if (command.equals("list")) {
                System.out.println("    Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println("    " + (i + 1) + "." + tasks.get(i));
                }
            } else if (isCommand(command, "delete")) {
                deleteTask(command, tasks);
            } else if (isCommand(command, "mark")) {
                updateTaskStatus(command, tasks, true);
            } else if (isCommand(command, "unmark")) {
                updateTaskStatus(command, tasks, false);
            } else {
                try {
                    Task newTask = createTask(command);
                    tasks.add(newTask);
                    System.out.println("    Got it. I've added this task:");
                    System.out.println("      " + newTask);
                    System.out.println("    Now you have " + tasks.size() + " tasks in the list.");
                } catch (IllegalArgumentException exception) {
                    System.out.println("    OOPS!!! " + exception.getMessage());
                }
            }

            System.out.println(separator);
        }

        System.out.println(separator);
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println(separator);
    }

    /**
     * Updates the completion status of a task from a command such as
     * "mark 2" or "unmark 2".
     */
    private static void updateTaskStatus(String command, ArrayList<Task> tasks,
                                         boolean isDone) {
        String[] commandParts = command.trim().split("\\s+");
        if (commandParts.length != 2) {
            System.out.println("    OOPS!!! Please provide a task number, for example: mark 2.");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(commandParts[1]);
            int taskIndex = taskNumber - 1;

            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                System.out.println("    OOPS!!! There is no task with that number.");
                return;
            }

            if (isDone) {
                tasks.get(taskIndex).markAsDone();
                System.out.println("    Nice! I've marked this task as done:");
                System.out.println("      " + tasks.get(taskIndex));
            } else {
                tasks.get(taskIndex).markAsNotDone();
                System.out.println("    OK, I've marked this task as not done yet:");
                System.out.println("      " + tasks.get(taskIndex));
            }
        } catch (NumberFormatException exception) {
            System.out.println("    OOPS!!! Task numbers must be positive whole numbers.");
        }
    }

    /**
     * Removes a task using a command such as "delete 3".
     */
    private static void deleteTask(String command, ArrayList<Task> tasks) {
        String[] commandParts = command.trim().split("\\s+");
        if (commandParts.length != 2) {
            System.out.println("    OOPS!!! Please provide a task number, for example: delete 2.");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(commandParts[1]);
            int taskIndex = taskNumber - 1;

            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                System.out.println("    OOPS!!! There is no task with that number.");
                return;
            }

            Task deletedTask = tasks.remove(taskIndex);
            System.out.println("    Noted. I've removed this task:");
            System.out.println("      " + deletedTask);
            System.out.println("    Now you have " + tasks.size() + " tasks in the list.");
        } catch (NumberFormatException exception) {
            System.out.println("    OOPS!!! Task numbers must be positive whole numbers.");
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
            return new Task("T", description, null, null, null);
        }

        if (isCommand(command, "deadline")) {
            String[] parts = command.substring(8).trim().split("\\s+/by\\s+", 2);
            if (parts.length == 2 && !parts[0].isBlank() && !parts[1].isBlank()) {
                return new Task("D", parts[0].trim(), parts[1].trim(), null, null);
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
                return new Task("E", description, null, from, to);
            }
            throw new IllegalArgumentException(
                    "An event needs a description, a start time after /from, and an end time after /to.");
        }

        throw new IllegalArgumentException("I don't recognize that command.");
    }
}
