package GUI;

import Model.Image;
import Operations.OperationProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ImageInterface extends JFrame {

    private final Image image;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLUS = 50;
    private final int xLocation;
    private final int yLocation;


    public ImageInterface(Image image, int xLocation, int yLocation) {
        this.image = image;
        this.xLocation = xLocation;
        this.yLocation = yLocation;

        this.setTitle("Image");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocation(xLocation, yLocation);

        JPanel buttonPanel = new JPanel();

        JButton btnSave = new JButton("Save");
        JButton btnLoadJson = new JButton("Load Json");
        JButton btnExit = new JButton("Exit");
        JButton btnLoadImage = new JButton("Load Image");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnLoadJson);
        buttonPanel.add(btnExit);
        buttonPanel.add(btnLoadImage);

        JLabel imageLabel = new JLabel();
        updateImage(imageLabel);

        add(imageLabel);
        add(buttonPanel, BorderLayout.SOUTH);

        // Añadir Listeners a los botones
        btnSave.addActionListener(new SaveActionListener());
        btnLoadJson.addActionListener(new LoadJsonActionListener());
        btnExit.addActionListener(new ExitActionListener());
        btnLoadImage.addActionListener(new LoadImageActionListener());

        setVisible(true);
    }

    private void updateImage(JLabel imageLabel) {
        int imageWidth = image.getImage().getWidth(null);
        int imageHeight = image.getImage().getHeight(null);

        double scaleFactor = Math.min((double) WIDTH / imageWidth, (double) HEIGHT / imageHeight);

        int newWidth = (int) (imageWidth * scaleFactor);
        int newHeight = (int) (imageHeight * scaleFactor);

        java.awt.Image scaledImage = image.getImage().getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
    }

    // Listener para guardar la imagen
    private class SaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecciona la carpeta donde guardar la imagen");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection != JFileChooser.APPROVE_OPTION) {
                System.out.println("No se puede guardar la imagen 1");
                return;
            }

            File selectedFile = fileChooser.getSelectedFile();
            String name = JOptionPane.showInputDialog("Ingrese el nombre del archivo");

            if (name == null || name.isEmpty()) {
                System.out.println("No se puede guardar la imagen por algo del nombre");
                return;
            }

            if (!name.toLowerCase().endsWith(".png")) {
                name = name + ".png";
            }

            File imageFile = new File(selectedFile, name);
            try{
                image.saveImage(imageFile);
                JOptionPane.showMessageDialog(null, "Archivo guardado exitosamente");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // Listener para cargar JSON y aplicar operaciones
    private class LoadJsonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Cargar JSON de operaciones");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON Files", "json"));

            int userSelection = fileChooser.showOpenDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File jsonFile = fileChooser.getSelectedFile();
                try {

                    OperationProcessor operationProcessor = new OperationProcessor(jsonFile);
                    Image newImage = new Image(image.getImage());
                    operationProcessor.applyOperations(newImage);
                    ImageInterface newImageInterface =  new ImageInterface(newImage, xLocation+PLUS, yLocation+PLUS);

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al cargar el JSON.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Error al cargar el JSON. " + ex.getMessage());
                }
            }
        }
    }

    private class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    private class LoadImageActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Cargar imagen");

            int userSelection = fileChooser.showOpenDialog(null); // Mostrar el diálogo

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile(); // Ya no será null
                try {
                    ImageInterface newImageInterface = new ImageInterface(new Image(selectedFile.getAbsolutePath()), xLocation+PLUS, yLocation+PLUS);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else {
                System.out.println("No se seleccionó ningún archivo.");
            }
        }
    }
}
