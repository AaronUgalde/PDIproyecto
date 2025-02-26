package Operations;

import Model.Image;
import java.awt.image.BufferedImage;
import static NoImageOperation.HelpFunctions.getRGBinArray;
import com.fasterxml.jackson.annotation.JsonCreator;

public class Gray implements Operation {

    @JsonCreator
    public Gray() {
        // Constructor sin parámetros para la deserialización JSON
    }

    @Override
    public void apply(Image image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getImage().getRGB(x, y);
                int[] rgb = getRGBinArray(pixel);
                int gray = (rgb[0] + rgb[1] + rgb[2]) / 3;

                // Convertir a formato RGB válido
                int grayRGB = (gray << 16) | (gray << 8) | gray;
                newImage.setRGB(x, y, grayRGB);
            }
        }
        image.setBufferedImage(newImage);
    }
}
