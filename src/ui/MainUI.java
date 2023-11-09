package ui;

import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

/**
 * Class for UI. Creates a window with a chat box and a DrawingPanel.
 * @author Mehdi Haidari
 */
public class MainUI {
    private final JFrame frame;
    private final JTextField messageInput;
    private final JTextPane messageArea;
    private final JScrollPane messageAreaScrollPane;
    private final JButton imageButton;
    private final DrawingPanel drawingPanel;
    private final JButton colorButton;
    private final JComboBox<String> brushSizeComboBox;
    private static final int ROWS = 8;
    private static final int COLUMNS = 40;

    /**
     * Constructor that puts the JFrame together. DrawingPanel, chatbox, buttons for
     * brush-size, color and send image.
     */
    public MainUI() {
        this.drawingPanel = new DrawingPanel();

        frame = new JFrame("bitsPlease");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        messageInput = new JTextField(40);

        colorButton = new JButton("Choose color");
        String[] brushSizes = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        brushSizeComboBox = new JComboBox<>(brushSizes);
        brushSizeComboBox.setSelectedIndex(1); // Default brush size is 2
        imageButton = new JButton("Send image");

        // Add color, brush size and send image button components to the UI.
        JPanel controlPanel = new JPanel();
        controlPanel.add(colorButton);
        controlPanel.add(brushSizeComboBox);
        controlPanel.add(imageButton);

        // Setting up the message area using values from the 'COLUMNS' and 'ROWS' class variables.
        messageArea = new JTextPane();
        messageArea.setEditable(false);

        FontMetrics fontMetrics = messageArea.getFontMetrics(messageArea.getFont());
        int width = COLUMNS * fontMetrics.charWidth('m');
        int height = ROWS * fontMetrics.getHeight();
        messageArea.setPreferredSize(new Dimension(width, height));

        // Add message panel to a scroll pane to enable scrolling features.
        messageAreaScrollPane = new JScrollPane(messageArea);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        frame.getContentPane().add(messageAreaScrollPane, c);

        c.gridx = 1;
        c.weightx = 1.0;
        frame.getContentPane().add(drawingPanel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        frame.getContentPane().add(messageInput, c);

        c.gridy = 2;
        frame.getContentPane().add(controlPanel, c);

        frame.pack();
    }

    private void scrollToBottom() { messageArea.setCaretPosition(messageArea.getDocument().getLength()); }

    private ImageIcon resizeImage(ImageIcon imageIcon, int maxWidth, int maxHeight) {
        int width = imageIcon.getIconWidth();
        int height = imageIcon.getIconHeight();

        if (width > maxWidth || height > maxHeight) {
            double scaleFactor = Math.min((double) maxWidth / width, (double) maxHeight / height);
            width = (int) (scaleFactor * width);
            height = (int) (scaleFactor * height);
            Image img = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }

        return imageIcon;
    }

    /**
     * Inserts the user and the text of the TextMessage to the messageArea, which is the
     * "chat-box" of the window.
     * @param textMessage the TextMessage to display in the chat.
     */
    public void displayTextMessage(TextMessage textMessage) {
        try {
            Document doc = messageArea.getDocument();
            doc.insertString(doc.getLength(), textMessage.getSender() + ": " + textMessage.getMessage() + "\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        scrollToBottom();
    }

    /**
     * Inserts the user and the image of the ImageMessage to the messageArea, which is the
     * "chat-box" of the window.
     * @param imageMessage the ImageMessage to display in the chat.
     * @throws Exception
     */
    public void displayImageMessage(ImageMessage imageMessage) throws Exception {
        // Insert sender's name as text.
        messageArea.getDocument().insertString(messageArea.getDocument().getLength(),
                imageMessage.getSender() + ":\n", null);
        ByteArrayInputStream bais = new ByteArrayInputStream(imageMessage.getImageData());

        // Note: ImageIO.read() is capable of reading all image formats.
        BufferedImage receivedImage = ImageIO.read(bais);
        ImageIcon icon = new ImageIcon(receivedImage);

        // Resize the image to normalize its dimensions.
        ImageIcon resizedIcon = resizeImage(icon, 400, 400);

        // Wrap image in a paragraph (JLabel) to force layout to accommodate the full image height.
        JLabel imageLabel = new JLabel(resizedIcon);

        // Display image using StyleConstraints.setComponent.
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setComponent(attrs, imageLabel);
        messageArea.getDocument().insertString(messageArea.getDocument().getLength(), " ", attrs);
        messageArea.getDocument().insertString(messageArea.getDocument().getLength(), "\n", null);

        scrollToBottom();
    }

    /**
     * Displays a DrawEvent on the this.drawingPanel.
     * @param drawEvent A DrawEvent object which consists of a list of Points along with color and stroke size.
     */
    public void displayDrawEvent(DrawEvent drawEvent) {
        java.util.List<Point> points = drawEvent.getPoints();
        Color receivedColor = drawEvent.getColor();
        float receivedStroke = drawEvent.getStroke();
        for (int i = 1; i < points.size(); i++) {
            Point prevPoint = points.get(i - 1);
            Point currentPoint = points.get(i);
            drawingPanel.drawReceivedSegment(prevPoint, currentPoint, receivedColor, receivedStroke);
        }
        scrollToBottom();
    }

    /**
     * @return the JTextField in which the user writes the chat messages.
     */
    public JTextField getMessageInput() {
        return messageInput;
    }

    /**
     * @return the frame that is the main DoodleChat-window. A JFrame with Chat box, DrawPanel and buttons.
     */
    public JFrame getFrame() {
        return frame;
    }

    public JButton getImageButton() {
        return imageButton;
    }

    public DrawingPanel getDrawingPanel() {
        return drawingPanel;
    }

    public JButton getColorButton() { return colorButton; }

    public JComboBox<String> getBrushSizeComboBox() {
        return brushSizeComboBox;
    }
}