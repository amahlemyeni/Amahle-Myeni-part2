
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    private static int totalMessages = 0;
    private static final List<Message> sentMessages = new ArrayList<>();

    // Constructor for automatically generated message IDs
    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageID(), messageNumber, recipient, messageText);
    }

    // Constructor retained for compatibility and unit testing
    public Message(String messageID, int messageNumber,
                   String recipient, String messageText) {

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // Generate a random ten-digit message ID
    private static String generateMessageID() {
        long id = ThreadLocalRandom.current().nextLong(
            1_000_000_000L, 10_000_000_000L
        );
        return String.valueOf(id);
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    // Check that the message ID contains exactly ten digits
    public boolean checkMessageID() {
        return messageID != null
            && messageID.matches("\\d{10}");
    }

    // Check message length
    public String checkMessageLength() {
        if (messageText == null) {
            return "Please enter a message of less than 250 characters.";
        }

        if (messageText.length() > 250) {
            return "Message exceeds 250 characters by "
                + (messageText.length() - 250)
                + "; please reduce the size.";
        }

        return "Message ready to send.";
    }

    // Validate a South African international cell number
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("\\+27\\d{9}")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    // Create the required uppercase message hash
    public String createMessageHash() {
        if (messageID == null || messageText == null
                || messageText.trim().isEmpty()) {
            return "";
        }

        String digits = messageID.replaceAll("\\D", "");

        if (digits.length() < 2) {
            return "";
        }

        String[] words = messageText.trim().split("\\s+");

        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1]
            .replaceAll("[^A-Za-z0-9]", "");

        return (digits.substring(0, 2) + ":"
            + messageNumber + ":"
            + firstWord + lastWord).toUpperCase();
    }

    // Send, disregard, or store a message
    public String sendMessage(String option) {

        if (option == null) {
            return "Invalid option. Please enter Send, Store, or Disregard.";
        }

        if (option.equalsIgnoreCase("Send")
                || option.equalsIgnoreCase("Send Message")) {

            if (!"Message ready to send.".equals(checkMessageLength())) {
                return checkMessageLength();
            }

            if (!"Cell phone number successfully captured."
                    .equals(checkRecipientCell())) {
                return checkRecipientCell();
            }

            if (!checkMessageID()) {
                return "Message ID must contain exactly ten digits.";
            }

            sentMessages.add(this);
            totalMessages++;

            return "Message successfully sent.";

        } else if (option.equalsIgnoreCase("Disregard")
                || option.equalsIgnoreCase("Disregard Message")) {

            return "Press 0 to delete the message.";

        } else if (option.equalsIgnoreCase("Store")
                || option.equalsIgnoreCase("Store Message")) {

            if (storeMessage()) {
                return "Message successfully stored.";
            }

            return "Message could not be stored.";

        } else {
            return "Invalid option. Please enter Send, Store, or Disregard.";
        }
    }

    // Alias in case your assignment or tests use SentMessage()
    public String SentMessage(String option) {
        return sendMessage(option);
    }

    // Display this message's details in the required order
    public String printMessages() {
        return "Message ID: " + messageID + "\n"
            + "Message Hash: " + messageHash + "\n"
            + "Recipient: " + recipient + "\n"
            + "Message: " + messageText;
    }

    // Display all messages successfully sent during this run
    public static String printAllMessages() {
        StringBuilder output = new StringBuilder();

        for (Message message : sentMessages) {
            output.append(message.printMessages()).append("\n\n");
        }

        return output.toString();
    }

    public int returnTotalMessages() {
        return totalMessages;
    }

    // Alias matching the assignment's method naming
    public int returnTotalMessagess() {
        return totalMessages;
    }

    // Store all message details in a valid JSON array
    public boolean storeMessage() {
        Path file = Paths.get("messages.json");

        try {
            List<String> existingMessages = new ArrayList<>();

            if (Files.exists(file)) {
                String contents = Files.readString(
                    file, StandardCharsets.UTF_8
                ).trim();

                if (!contents.isEmpty() && !contents.equals("[]")) {
                    if (!contents.startsWith("[")
                            || !contents.endsWith("]")) {
                        return false;
                    }

                    String body = contents.substring(
                        1, contents.length() - 1
                    ).trim();

                    if (!body.isEmpty()) {
                        existingMessages.add(body);
                    }
                }
            }

            String json = "{"
                + "\"messageID\":\"" + escapeJSON(messageID) + "\","
                + "\"messageHash\":\"" + escapeJSON(messageHash) + "\","
                + "\"messageNumber\":" + messageNumber + ","
                + "\"recipient\":\"" + escapeJSON(recipient) + "\","
                + "\"message\":\"" + escapeJSON(messageText) + "\""
                + "}";

            existingMessages.add(json);

            String output = "[\n"
                + String.join(",\n", existingMessages)
                + "\n]\n";

            Files.writeString(file, output, StandardCharsets.UTF_8);
            return true;

        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
            return false;
        }
    }

    // Escape special characters so the stored JSON remains valid
    private String escapeJSON(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
    public static int getTotalMessages() {
    return totalMessages;
}
}
