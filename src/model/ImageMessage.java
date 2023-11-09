package model;

import utils.MessageHandler;
import utils.MessageProcessor;

import java.io.Serializable;

/**
 * A Class used for sending image messages along with the username of
 * the client who sent the message, between server and clients.
 * @author Theo Ahlgren
 */
public class ImageMessage implements ServerMessage, Serializable {
    private static final long serialVersionUID = 1L;
    private final byte[] imageData;
    private final String sender;

    /**
     * Creates an object representing a chat message sent by a user.
     * @param sender username of client who sent the message.
     * @param imageData the data(?) of the image that was sent.
     */
    public ImageMessage(String sender, byte[] imageData) {
        this.sender = sender;
        this.imageData = imageData.clone();
    }

    /**
     * Method used to handle this ImageMessage. This ImageMessage is passed to the MessageProcessor
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
     * @return the username of the client who sent the image.
     */
    public String getSender() {
        return sender;
    }

    /**
     * @return the data which makes up the image.
     */
    public byte[] getImageData() {
        return imageData.clone();
    }
}