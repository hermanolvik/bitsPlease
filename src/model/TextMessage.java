package model;

import utils.*;

import java.io.Serializable;

/**
 * A Class used for sending text messages along with the username of
 * the client who sent the message, between server and clients.
 * @author Theo Ahlgren
 */
public class TextMessage implements ServerMessage, Serializable {
    private static final long serialVersionUID = 1L;
    private final String message;
    private final String sender;

    /**
     * Creates a object representing a chat message sent by a user.
     * @param sender username of client who sent the message.
     * @param message the text that was sent.
     */
    public TextMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    /**
     * Method used to handle this TextMessage. This TextMessage is passed to the MessageProcessor
     * along with a MessageHandler for the next step.
     * @param processor The MessageProcessor
     * @param handler The MessageHandler
     * @throws Exception
     */
    @Override
    public void process(MessageProcessor processor, MessageHandler handler) throws Exception {
        processor.display(this, handler);
    }

    /**
     * @return the username of the client who sent the message.
     */
    public String getSender() {
        return sender;
    }

    /**
     * @return the text of the message.
     */
    public String getMessage() {
        return message;
    }
}