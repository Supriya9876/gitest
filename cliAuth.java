package chatApplication.chat;

import java.io.*;
import java.net.*;
public class cliAuth {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to the chat server.");

            // Display server messages and send user input
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println("Server: " + serverMessage);

                if (serverMessage.contains("Enter your")) {
                    // Send username/password or other authentication details
                    System.out.print("> ");
                    String input = userInput.readLine();
                    out.println(input);
                } else if (serverMessage.equals("Authentication successful! You can now chat.")) {
                    // Start chatting
                    System.out.println("You can start chatting now!");
                    new Thread(() -> {
                        try {
                            String message;
                            while ((message = userInput.readLine()) != null) {
                                out.println(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else if (serverMessage.equals("Authentication failed. Connection closing.")) {
                    System.out.println("Exiting...");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}