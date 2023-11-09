package tests;

import model.*;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class ModelTests {

    @Test
    public void testTextMessage() {
        String username = "testUser";
        String content = "test message";
        TextMessage textMessage = new TextMessage(username, content);

        assertEquals(username, textMessage.getSender());
        assertEquals(content, textMessage.getMessage());
    }

    @Test
    public void testImageMessage() {
        String expectedUsername = "testUser";
        byte[] expectedImageBytes = new byte[10];

        ImageMessage imageMessage = new ImageMessage(expectedUsername, expectedImageBytes);

        assertEquals(expectedUsername, imageMessage.getSender());
        assertArrayEquals(expectedImageBytes, imageMessage.getImageData());
    }

    @Test
    public void testDrawEvent() {
        List<Point> segment = new ArrayList<Point>();
        int x1 = 5, y1 = 8, x2 = 2, y2 = 9;
        Point point1 = new Point(x1, y1);
        Point point2 = new Point(x2, y2);
        segment.add(point1);
        segment.add(point2);
        Color color = Color.BLACK;
        float stroke = 2.0f;
        DrawEvent drawEvent = new DrawEvent(segment, color, stroke);

        assertEquals(segment, drawEvent.getPoints());
        assertEquals(color, drawEvent.getColor());
        assertEquals(stroke, drawEvent.getStroke());
    }
}