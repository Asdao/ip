import java.util.Scanner;

/**
 * Entry point for the Furina chatbot.
 */
class Furina {
    static void main(String[] args) {
        String separator = "____________________________________________________________";
        String banner = "    F U R I N A";

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
            System.out.println("    " + command);
            System.out.println(separator);
        }

        System.out.println(separator);
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println(separator);
    }
}
