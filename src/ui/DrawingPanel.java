package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Class representing a drawing canvas. A drawable part of the UI.
 * @author Mehdi Haidari
 */
public class DrawingPanel extends JPanel {

    private Image image;
    private Graphics2D g2;

    /**
     * Creates a new drawing canvas.
     */
    public DrawingPanel() {
        setDoubleBuffered(false);
        setPreferredSize(new Dimension(800, 600));
    }

    /**
     * Creates a new drawing canvas.
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }

        g.drawImage(image, 0, 0, null);
    }

    private void clear() {
        g2.setPaint(Color.white);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        repaint();
    }

    /**
     * Method for drawing a received line without interference.
     */
    public void drawReceivedSegment(Point prevPoint, Point currentPoint, Color color, float stroke) {
        if (image != null) {
            Graphics2D g2Temp = (Graphics2D) image.getGraphics();
            g2Temp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2Temp.setPaint(color);
            g2Temp.setStroke(new BasicStroke(stroke));
            g2Temp.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
            repaint();
        }
    }

    /**
     * Method for drawing a line.
     */
    public void drawSegment(Point prevPoint, Point currentPoint, Color color, float stroke) {
        g2.setPaint(color);
        g2.setStroke(new BasicStroke(stroke));
        g2.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
        repaint();
    }

    public Graphics2D getG2() {
        return g2;
    }
}