import GUI.ImageInterface;
import Model.Image;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();

        try {
            ImageInterface test = new ImageInterface(new Image(file.getAbsolutePath()), 100, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}