package server;

import model.*;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class for handling ObjectStreams through sockets assigned to connected clients.
 * @author Pontus Brylander
 */
public class ClientHandler extends Thread {

    private final Socket socket;
    private ObjectOutputStream out;
    private String username;

    /**
     * @param socket Socket through which a connection to the client has been established.
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * Sends the history of the server (the chat and drawings) to the client and sets the Input- and
     * OutputObjectStreams to the instance variables in and out.
     * Also sets the username of the client and broadcasts a message
     * "*username* has joined the chat".
     * The thread listens for incoming objects (ServerMessages), which invoke the server
     * to broadcast the incoming ServerMessage.
     * When the client terminates the connection this handler is removed from the server
     * and the socket is closed.
     */
    @Override
    public void run() {
        try (   ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream())){
            this.out = out;

            // Send chat history to the client
            for (ServerMessage message : MainServer.getChatHistory()) {
                sendMessage(message);
            }

            username = (String) in.readObject();
            MainServer.broadcast(new TextMessage(username, username + " has joined the chat."));

            while (true) {
                ServerMessage serverMessage = (ServerMessage) in.readObject();
                  MainServer.broadcast(serverMessage);
            }
        } catch (IOException | ClassNotFoundException e) {
            MainServer.removeClient(this);
            System.err.println("Client error: " + e.getMessage());
        }
    }

    /**
     * The method that sends objects to the client.
     * @param message the message to send (only ServerMessages in our case).
     */
    public synchronized void sendMessage(Object message) {


            try{
                out.writeObject(message);
                out.reset(); // Resets the ObjectOutputStream cache
            } catch (IOException e) {
                System.err.println("Error sending message: " + e.getMessage());
            }

    }

    /**
     * @return the username of the client that this ClientHandler is handling.
     */
    public String getUsername() {
        return username;
    }
}
