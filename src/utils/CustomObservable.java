package utils;

/**
 * A custom observable interface for the observer-pattern.
 * @author Herman Olvik
 */
public interface CustomObservable {
    void addObserver(CustomObserver observer);
    void removeObserver(CustomObserver observer);
}