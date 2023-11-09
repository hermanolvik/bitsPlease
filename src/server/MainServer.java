package server;

import model.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The main server-class.
 * @author Pontus Brylander
 */
public class MainServer {

    private static final int PORT = 1234;
    private static final List<ClientHandler> clients = new ArrayList<>();
    private static ChatHistory chatHistory;

    /**
     * Starts listening to the port and assigns incoming connections a socket.
     * The socket is wrapped(?) in a clientHandler and put in the static variable "clients",
     * which is a list of all active ClientHandlers.
     * @param args unused
     */
    public static void main(String[] args) {
        chatHistory = new ChatHistory();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat server started on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    /**
     * Tells all connected ClientHandlers to send the message through their
     * socket to the client. Also adds the message to the message history of the server.
     * @param message TextMessage/ImageMessage/DrawEvent
     */
    static void broadcast(ServerMessage message) {
        chatHistory.addMessage(message);
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    /**
     * Removes a ClientHandler from the list of connected ClientHandlers.
     * The client assigned the socket which the removed ClientHandler handles will no longer receive broadcast messages.
     * @param client ClientHandler to remove
     */
    static void removeClient(ClientHandler client) {
        clients.remove(client);
        broadcast(new TextMessage(client.getUsername(), client.getUsername() + " has left the chat."));
    }

    /**
     * @return the chat history as a List of ServerMessage objects
     */
    static List<ServerMessage> getChatHistory() {
        return chatHistory.getHistory();
    }
}