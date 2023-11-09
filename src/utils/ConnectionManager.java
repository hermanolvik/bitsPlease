package utils;

import model.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Class for handling clients connection to the server and
 * send incoming ServerMessages the observers.
 * @author Johan Franz
 */
public class ConnectionManager implements CustomObservable {
    private final ArrayList<CustomObserver> observers;
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final String username;

    /**
     * Sets up a connection through a socket to the server and creates a ReadThread (which is a thread) object which is run
     * and listens to the socket.
     * The username is written to the server. This is the first thing the client send to the server, so the server knows
     * that the first thing that comes in through the socket is the clients username.
     * @param username Username of the client.
     */
    public ConnectionManager(String username) {
        this.username = username;
        this.observers = new ArrayList<CustomObserver>();
        new ConnectionManager.ReadThread(this).start();

    }

    // Add a custom observer to receive data updates.
    public void addObserver(CustomObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(CustomObserver observer) {
        observers.remove(observer);
    }

    private class ReadThread extends Thread {
        private final ConnectionManager connectionManager;

        public ReadThread(ConnectionManager connectionManager) { this.connectionManager = connectionManager; }

        // Listen for incoming server messages in this separate thread.

        /**
         * Listens for incoming ServerMessages in this thread and sends the incoming
         * ServerMessages to the connectionManager.
         */
        @Override
        public void run() {
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                 ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream i = new ObjectInputStream(socket.getInputStream())){
                out = o;
                in = i;
                out.writeObject(username);

                while(true) {
                    ServerMessage serverMessage = (ServerMessage) in.readObject();
                    if (serverMessage != null) {
                        connectionManager.updateObservers(serverMessage);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error reading from server: " + e.getMessage());
            }
        }
    }

    /**
     * Sends a ServerMessage to the observers.
     * @param serverMessage ServerMessage to send to the observer.
     * @throws Exception
     */

    public void updateObservers(ServerMessage serverMessage) throws Exception {
        for (CustomObserver observer : observers) {
            observer.update(serverMessage);
        }
    }

    public ObjectOutputStream getOutputStream() {
        return out;
    }

    public String getUsername() {
        return username;
    }
}
