package chatApplication.chat;
import java.io.*;
import java.net.*;
import java.util.HashMap;

public class serAuth {
    
    private static final int PORT = 12345;
    private static HashMap<String, String> userDatabase = new HashMap<>(); // In-memory user database

    public static void main(String[] args) {
        // Predefined users (username -> password)
        userDatabase.put("user1", "password1");
        userDatabase.put("user2", "password2");

        System.out.println("Chat Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected.");

                // Handle client in a separate thread
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            // Authentication
            out.println("Welcome to the Chat Server! Please log in.");
            out.println("Enter your username:");
            String username = in.readLine();
            out.println("Enter your password:");
            String password = in.readLine();

            if (authenticate(username, password)) {
                out.println("Authentication successful! You can now chat.");
                System.out.println(username + " logged in.");

                // Communication loop
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(username + ": " + message);
                    out.println("You: " + message); // Echo back to the client
                }
            } else {
                out.println("Authentication failed. Connection closing.");
                System.out.println("Authentication failed for a client.");
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean authenticate(String username, String password) {
        // Check username and password in the user database
        return userDatabase.containsKey(username) && userDatabase.get(username).equals(password);
    }
}