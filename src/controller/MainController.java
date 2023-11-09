package controller;

import utils.*;
import model.*;
import ui.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles interaction with the UI, has a network connection and handles ServerMessages.
 */
public class MainController implements CustomObserver, MessageHandler {
    private final ConnectionManager connectionManager;
    private final DrawingModel drawingModel;
    private final MainUI mainUI;
    private final MessageProcessor messageProcessor;

    /**
     * Constructor that sets up the clientside. Objects for model, UI and network connection are
     * assigned to the instance variables of this class.
     * This object observes passes itself to the connectionManager as an observer.
     * The main DoodleChat window is set visible.
     * Eventslistners are also applied to the drawarea and buttons.
     */
    public MainController() {
        drawingModel = new DrawingModel();
        mainUI = new MainUI();
        messageProcessor = new MessageProcessor();
        connectionManager = new ConnectionManager(requestUsername());
        connectionManager.addObserver(this);
        mainUI.getFrame().setVisible(true);

        setupEventListeners();
    }

    private void setupEventListeners() {
        mainUI.getMessageInput().addActionListener(e -> {
            String text = mainUI.getMessageInput().getText();
            if (!text.equals("")) {
                TextMessage textMessage = new TextMessage(connectionManager.getUsername(), text);
                try {
                    connectionManager.getOutputStream().writeObject(textMessage);
                } catch (IOException ex) {
                    System.err.println("Error sending message: " + ex.getMessage());
                }
                mainUI.getMessageInput().setText("");
            }
        });

        mainUI.getImageButton().addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif", "bmp"));
            int returnValue = fileChooser.showOpenDialog(mainUI.getFrame());

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File imageFile = fileChooser.getSelectedFile();
                String fileExtension = getFileExtension(imageFile).toLowerCase();
                try(ByteArrayOutputStream baos = new ByteArrayOutputStream();){
                    BufferedImage image = ImageIO.read(imageFile);

                    ImageIO.write(image, fileExtension, baos);
                    byte[] imageData = baos.toByteArray();

                    connectionManager.getOutputStream().writeObject(new ImageMessage(connectionManager.getUsername(), imageData));
                    connectionManager.getOutputStream().flush();
                } catch (IOException ex) {
                    System.err.println("Error sending image: " + ex.getMessage());
                }
            }
        });

        mainUI.getBrushSizeComboBox().addActionListener(e -> {
            float brushSize = Float.parseFloat((String) mainUI.getBrushSizeComboBox().getSelectedItem());
            drawingModel.setStroke(brushSize);
        });

        mainUI.getColorButton().addActionListener(e -> {
            Color chosenColor = JColorChooser.showDialog(mainUI.getFrame(), "Choose a color", drawingModel.getColor());
            if (chosenColor != null) {
                drawingModel.setColor(chosenColor);
            }
        });

        mainUI.getDrawingPanel().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                drawingModel.setCurrentSegment(new ArrayList<>());
                drawingModel.getCurrentSegment().add(e.getPoint());
            }

            public void mouseReleased(MouseEvent e) {
                if (drawingModel.getCurrentSegment().size() > 2) {
                    DrawEvent drawEvent = new DrawEvent(drawingModel.getCurrentSegment(), drawingModel.getColor(), drawingModel.getStroke());
                    try {
                        connectionManager.getOutputStream().writeObject(drawEvent);
                    } catch (IOException ex) {
                        System.err.println("Error sending draw event: " + ex.getMessage());
                    }
                }
            }
        });

        mainUI.getDrawingPanel().addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getPoint();

                if (mainUI.getDrawingPanel().getG2() != null) {
                    Point prevPoint = drawingModel.getCurrentSegment().get(drawingModel.getCurrentSegment().size() - 1);
                    mainUI.getDrawingPanel().drawSegment(prevPoint, currentPoint, drawingModel.getColor(), drawingModel.getStroke());
                    mainUI.getDrawingPanel().repaint();
                    drawingModel.getCurrentSegment().add(currentPoint);

                    // Send a model.DrawEvent with only the last two points
                    List<Point> lastTwoPoints = new ArrayList<>();
                    lastTwoPoints.add(prevPoint);
                    lastTwoPoints.add(currentPoint);
                    try {
                        connectionManager.getOutputStream().writeObject(new DrawEvent(lastTwoPoints, drawingModel.getColor(), drawingModel.getStroke()));
                    } catch (IOException ex) {
                        System.err.println("Error sending draw event: " + ex.getMessage());
                    }
                }
            }
        });
    }

    /**
     * Method for the local messageProcessor to handle ServerMessages that the client has received.
     * @param serverMessage ServerMessage to handle (text/draw/image)
     * @throws Exception
     */
    @Override
    public void update(ServerMessage serverMessage) throws Exception {
        serverMessage.process(messageProcessor, this);
    }

    /**
     * Method that tells the UI (view) to update chat with a new TextMessage.
     * @param textMessage The TextMessage to update with.
     */
    @Override
    public void displayTextMessage(TextMessage textMessage) {
        mainUI.displayTextMessage(textMessage);
    }

    /**
     * Method that tells the UI (view) to update chat with a new ImageMessage.
     * @param imageMessage The ImageMessage to update with.
     * @throws Exception
     */
    @Override
    public void displayImageMessage(ImageMessage imageMessage) throws Exception {
        mainUI.displayImageMessage(imageMessage);
    }

    /**
     * Method that tells the UI (view) to update the DrawPanel with a new DrawEvent.
     * @param drawEvent The DrawEvent to update with.
     */
    @Override
    public void displayDrawEvent(DrawEvent drawEvent) {
        mainUI.displayDrawEvent(drawEvent);
    }

    // Helper method to get the file extension based on a file.
    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    /**
     * Opens up a windows asking the user for a username.
     * @return the selected username. "Anonymous" is no name is entered.
     */
    private String requestUsername() {
        String username = JOptionPane.showInputDialog(
                new JFrame(),
                "Choose a username:",
                "Username selection",
                JOptionPane.PLAIN_MESSAGE
        );

        if (username == null) {
            System.out.println("User cancelled the dialog.");
            return "Anonymous";
        }
        return username;
    }
}