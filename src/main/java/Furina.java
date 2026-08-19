import java.util.Scanner;

/**
 * Entry point for the Furina chatbot.
 */
class Furina {
    private static final int MAX_TASKS = 100;

    static void main(String[] args) {
        String separator = "____________________________________________________________";
        String banner = "    F U R I N A";
        String[] tasks = new String[MAX_TASKS];
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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("    " + (i + 1) + ". " + tasks[i]);
                }
            } else if (taskCount < MAX_TASKS) {
                tasks[taskCount] = command;
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
}
