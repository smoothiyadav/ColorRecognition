import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class ColorRecognitionApp 
{
    // JFrame and components
    private JFrame frame;
    private JLabel imageLabel;
    private JTextField redField, greenField, blueField;
    private JLabel resultLabel;
    private BufferedImage image;
    private HashMap<String, Color> colorMap;

    public static void main(String[] args) 
{
        SwingUtilities.invokeLater(() -> new ColorRecognitionApp().createAndShowGUI());
    }

    private void createAndShowGUI() 
{
        // Set up the main JFrame
        frame = new JFrame("Color Recognition App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Set up the main panel
        JPanel panel = new JPanel(new BorderLayout());

        // Image label where image will be displayed
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane imageScrollPane = new JScrollPane(imageLabel);
        panel.add(imageScrollPane, BorderLayout.CENTER);

        // Control panel for input and buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        // Input fields for RGB values
        redField = new JTextField(3);
        greenField = new JTextField(3);
        blueField = new JTextField(3);
        controlPanel.add(new JLabel("R:"));
        controlPanel.add(redField);
        controlPanel.add(new JLabel("G:"));
        controlPanel.add(greenField);
        controlPanel.add(new JLabel("B:"));
        controlPanel.add(blueField);

        // Buttons for actions
        JButton recognizeButton = new JButton("Recognize Color");
        JButton loadImageButton = new JButton("Load Image");
        JButton pickPixelButton = new JButton("Pick Pixel");

        controlPanel.add(recognizeButton);
        controlPanel.add(loadImageButton);
        controlPanel.add(pickPixelButton);

        // Result label for displaying recognized color
        resultLabel = new JLabel("Color: Unknown");
        controlPanel.add(resultLabel);

        // Add control panel and image display area to frame
        panel.add(controlPanel, BorderLayout.SOUTH);
        frame.add(panel, BorderLayout.CENTER);

        // Initialize color map
        initializeColorMap();

        // Set up button listeners
        recognizeButton.addActionListener(e -> recognizeColorFromInput());
        loadImageButton.addActionListener(e -> loadImage());
        pickPixelButton.addActionListener(e -> pickPixel());

        // Display the frame
        frame.setVisible(true);
    }

    // Initialize predefined colors in a HashMap
    private void initializeColorMap()
 {
        colorMap = new HashMap<>();
        colorMap.put("Red", new Color(255, 0, 0));
        colorMap.put("Green", new Color(0, 255, 0));
        colorMap.put("Blue", new Color(0, 0, 255));
        colorMap.put("Yellow", new Color(255, 255, 0));
        colorMap.put("Cyan", new Color(0, 255, 255));
        colorMap.put("Magenta", new Color(255, 0, 255));
        colorMap.put("Black", new Color(0, 0, 0));
        colorMap.put("White", new Color(255, 255, 255));
        colorMap.put("Gray", new Color(128, 128, 128));
        colorMap.put("Orange", new Color(255, 165, 0));
        colorMap.put("Pink", new Color(255, 192, 203));
    }

    // Load an image using JFileChooser
    private void loadImage()
 {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION)
 {
            File file = fileChooser.getSelectedFile();
            try 
{
                image = ImageIO.read(file);
                imageLabel.setIcon(new ImageIcon(image));
                frame.pack();
            }
 catch (IOException e)
 {
                JOptionPane.showMessageDialog(frame, "Couldn't load image.");
            }
        }
    }

    // Recognize color based on RGB input from text fields
    private void recognizeColorFromInput()
 {
        try 
{
            int r = Integer.parseInt(redField.getText());
            int g = Integer.parseInt(greenField.getText());
            int b = Integer.parseInt(blueField.getText());

            String colorName = getClosestColorName(r, g, b);
            resultLabel.setText("Color: " + colorName);
        }
 catch (NumberFormatException e)
 {
            JOptionPane.showMessageDialog(frame, "Enter valid RGB values.");
        }
    }

    // Pick a color from a clicked pixel
    private void pickPixel()  
{
        if (image == null)
 {
            JOptionPane.showMessageDialog(frame, "No image loaded.");
            return;
        }

        imageLabel.addMouseListener(new MouseAdapter()
 {
            @Override
            public void mouseClicked(MouseEvent e) 
{
                int x = e.getX();
                int y = e.getY();
                if (x < image.getWidth() && y < image.getHeight())
 {
                    int rgb = image.getRGB(x, y);
                    Color color = new Color(rgb, true);
                    String colorName = getClosestColorName(color.getRed(), color.getGreen(), color.getBlue());
                    resultLabel.setText("Picked Color: " + colorName);
                }
                imageLabel.removeMouseListener(this); // Remove listener after picking
            }
        });
    }

    // Get the closest color name from the color map
    private String getClosestColorName(int r, int g, int b) 
{
        String closestColor = "Unknown";
        double minDistance = Double.MAX_VALUE;

        for (String colorName : colorMap.keySet())
 {
            Color color = colorMap.get(colorName);
            double distance = colorDistance(r, g, b, color.getRed(), color.getGreen(), color.getBlue());
            if (distance < minDistance) 
{
                minDistance = distance;
                closestColor = colorName;
            }
        }
        return closestColor;
    }

    // Calculate Euclidean distance between two colors
    private double colorDistance(int r1, int g1, int b1, int r2, int g2, int b2)
 {
        return Math.sqrt(Math.pow(r2 - r1,2) + Math.pow(g2 - g1,2) + Math.pow(b2 - b1,2));
}
}
