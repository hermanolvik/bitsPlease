package utils;

import model.DrawEvent;
import model.ImageMessage;
import model.TextMessage;

/**
 * An interface for objects capable of handling server messages.
 * @author Herman Olvik
 */
public interface MessageHandler {
    void displayTextMessage(TextMessage textMessage);

    void displayImageMessage(ImageMessage imageMessage) throws Exception;

    void displayDrawEvent(DrawEvent drawEvent) throws Exception;
}