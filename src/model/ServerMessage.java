package model;

import utils.MessageHandler;
import utils.MessageProcessor;

/**
 * The interface used for communication between server and clients.
 * @author Theo Ahlgren
 */
public interface ServerMessage {
    void process(MessageProcessor processor, MessageHandler handler) throws Exception;
    String getSender();
}