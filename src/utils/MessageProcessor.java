package utils;

import model.DrawEvent;
import model.ImageMessage;
import model.TextMessage;

/**
 * Class for processing ServerMessages: TextMessage, ImageMessage, DrawEvent.
 * This class is used to differentiate between different types of ServerMessages and
 * take action depending on the type of ServerMessage (TextMessage, ImageMessage, DrawEvent).
 * @author Pontus Brylander
 */
public class MessageProcessor {

    public MessageProcessor() {}

    public void display(TextMessage textMessage, MessageHandler messageHandler) {
        messageHandler.displayTextMessage(textMessage);
    }

    public void display(ImageMessage imageMessage, MessageHandler messageHandler) throws Exception {
        messageHandler.displayImageMessage(imageMessage);
    }

    public void display(DrawEvent drawEvent, MessageHandler messageHandler) throws Exception {
        messageHandler.displayDrawEvent(drawEvent);
    }
}