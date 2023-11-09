package utils;

import model.ServerMessage;

/**
 * A custom observer interface for the observer-pattern.
 * @author Herman Olvik
 */
public interface CustomObserver {
    void update(ServerMessage serverMessage) throws Exception;
}