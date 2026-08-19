import java.util.Scanner;

/**
 * Entry point for the Furina chatbot.
 */
class Furina {
    private static final int MAX_TASKS = 100;

    static void main(String[] args) {
        String separator = "____________________________________________________________";
        String banner = "    F U R I N A";
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello! I'm Furina.");
        System.out.println("What can I do for you?");
        System.out.println(separator);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                break;
            }

            System.out.println(separator);

            if (command.equals("list")) {
                System.out.println("    Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("    " + (i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                updateTaskStatus(command, tasks, taskCount, true);
            } else if (command.startsWith("unmark ")) {
                updateTaskStatus(command, tasks, taskCount, false);
            } else if (taskCount < MAX_TASKS) {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println("    added: " + command);
            } else {
                System.out.println("    The task list is full.");
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
    private static void updateTaskStatus(String command, Task[] tasks,
                                         int taskCount, boolean isDone) {
        try {
            String[] commandParts = command.split(" ");
            int taskNumber = Integer.parseInt(commandParts[1]);
            int taskIndex = taskNumber - 1;

            if (taskIndex < 0 || taskIndex >= taskCount) {
                System.out.println("    That task does not exist.");
                return;
            }

            if (isDone) {
                tasks[taskIndex].markAsDone();
                System.out.println("    Nice! I've marked this task as done:");
                System.out.println("      " + tasks[taskIndex]);
            } else {
                tasks[taskIndex].markAsNotDone();
                System.out.println("    OK, I've marked this task as not done yet:");
                System.out.println("      " + tasks[taskIndex]);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
            System.out.println("    Please provide a valid task number.");
        }
    }
}
