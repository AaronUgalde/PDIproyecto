package Operations;

import Model.Image;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Save implements Operation {

    @JsonCreator
    public Save() {

    }

    @Override
    public void apply(Image image) {
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
