package model;

import java.awt.*;
import java.util.List;

/**
 * Class representing the current segment
 * (a continuous line, from that the mouse is pressed down until it is released).
 * @author Theo Ahlgren
 */
public class DrawingModel {
    private List<Point> currentSegment;
    private Color color = Color.BLACK;
    private float stroke = 2.0f;

    public DrawingModel() {}

    public List<Point> getCurrentSegment() {
        return currentSegment;
    }

    public Color getColor() {
        return color;
    }

    public float getStroke() {
        return stroke;
    }

    public void setCurrentSegment(List<Point> currentSegment) {
        this.currentSegment = currentSegment;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStroke(float stroke) {
        this.stroke = stroke;
    }
}