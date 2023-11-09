package model;

import utils.*;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.stream.Collectors;

/**
 * A Class used for sending a drawn line along with the username of
 * the client who drew it, between server and clients.
 * @author Theo Ahlgren
 */
public class DrawEvent implements ServerMessage, Serializable {
    private static final long serialVersionUID = 1L;
    private final java.util.List<Point> points;
    private final Color color;
    private final float stroke;

    /**
     * Creates an object that represents a drawn line ready to send between the client and
     * the server.
     * @param points the List of points making up the line.
     * @param color the color of the line.
     * @param stroke the stroke size of the line.
     */
    public DrawEvent(java.util.List<Point> points, Color color, float stroke) {
        this.points = points;
        this.color = color;
        this.stroke = stroke;
    }

    /**
     * @return a List of points making up this line
     */
    public java.util.List<Point> getPoints() {
        return points.stream()
                .map(p -> new Point(p.x, p.y))
                .collect(Collectors.toList());
    }

    /**
     * @return the color of this line
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the stroke size of this line.
     */
    public float getStroke() {
        return stroke;
    }

    /**
     * Method used to handle this DrawEvent. This DrawEvent is passed to the MessageProcessor
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
     * @return the username of the client who drew this line.
     */
    @Override
    public String getSender() {
        return null;
    }
}