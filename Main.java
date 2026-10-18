
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Registration
        System.out.println("Enter your first name:");
        String firstName = scanner.nextLine();

        System.out.println("Enter your last name:");
        String lastName = scanner.nextLine();

        Login user = null;
        boolean isRegistered = false;

        while (!isRegistered) {

            System.out.println("Enter your username:");
            String username = scanner.nextLine();

            System.out.println("Enter your password:");
            String password = scanner.nextLine();

            System.out.println("Enter your cell phone number:");
            String cellPhone = scanner.nextLine();

            user = new Login(
                username, password, cellPhone, firstName, lastName
            );

            String registrationResult = user.registerUser();
            System.out.println(registrationResult);

            if (registrationResult != null &&
                registrationResult.toLowerCase().contains("successfully captured")) {
                isRegistered = true;
            } else {
                System.out.println("Please try again.\n");
            }
        }

        // Login
        System.out.println("\n--- LOGIN ---");

        boolean isLoggedIn = false;

        while (!isLoggedIn) {

            System.out.println("Enter your username:");
            String loginUsername = scanner.nextLine();

            System.out.println("Enter your password:");
            String loginPassword = scanner.nextLine();

            boolean loginResult =
                user.loginUser(loginUsername, loginPassword);

            System.out.println(
                user.returnLoginStatus(loginUsername, loginPassword)
            );

            if (loginResult) {
                isLoggedIn = true;
                System.out.println("Login successful!");
            } else {
                System.out.println(
                    "Incorrect username or password. Please try again.\n"
                );
            }
        }

        // QuickChat
        System.out.println("\nWelcome to QuickChat.");
        System.out.print("How many messages do you want to send? ");

        int totalToSend;

        while (true) {
            try {
                totalToSend = Integer.parseInt(scanner.nextLine().trim());

                if (totalToSend < 0) {
                    System.out.print(
                        "Please enter zero or a positive number: "
                    );
                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                System.out.print(
                    "Please enter a valid whole number: "
                );
            }
        }

        int messagesEntered = 0;

        while (true) {

            System.out.println("\nMenu:");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {

                if (messagesEntered >= totalToSend) {
                    System.out.println(
                        "You have reached your message limit."
                    );
                    continue;
                }

                System.out.print("Enter Recipient Cell (e.g. +27718693002): ");
                String recipient = scanner.nextLine().trim();

                System.out.print("Enter Message: ");
                String messageText = scanner.nextLine();

                Message msg = new Message(
                    messagesEntered,
                    recipient,
                    messageText
                );

                // Validate message ID
                if (!msg.checkMessageID()) {
                    System.out.println("Invalid message ID. Please try again.");
                    continue;
                }

                // Validate message length
                String lengthResult = msg.checkMessageLength();

                if (!lengthResult.equals("Message ready to send.")) {
                    System.out.println(lengthResult);
                    continue;
                }

                // Validate recipient number
                String recipientResult = msg.checkRecipientCell();

                if (!recipientResult.equals(
                    "Cell phone number successfully captured.")) {
                    System.out.println(recipientResult);
                    continue;
                }

                // Display hash and request action
                System.out.println(
                    "Message Hash: " + msg.createMessageHash()
                );

                System.out.println("Choose: Send, Store, or Disregard");
                String option = scanner.nextLine().trim();

                String result = msg.sendMessage(option);
                System.out.println(result);

                if (result.equals("Message successfully sent.")) {
                    System.out.println(msg.printMessages());
                    messagesEntered++;

                } else if (result.equals("Message successfully stored.")
                        || result.equals("Press 0 to delete the message.")) {
                    messagesEntered++;

                } else {
                    System.out.println(
                        "Your message was not counted. Please try again."
                    );
                }

                if (messagesEntered >= totalToSend) {
                    System.out.println(
                        "You have entered all the messages you requested."
                    );
                }

            } else if (choice.equals("2")) {

                System.out.println("Coming Soon.");

            } else if (choice.equals("3")) {

                System.out.println("\nGoodbye!");
                System.out.println(
                    "Total messages sent: " + Message.getTotalMessages()
                );

                scanner.close();
                return;

            } else {

                System.out.println(
                    "Invalid option. Please choose 1, 2, or 3."
                );
            }
        }
    }
}

