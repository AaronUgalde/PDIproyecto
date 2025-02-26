package Operations;

import Model.Image;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.BufferedImage;

public class Brightness implements Operation {
    private int brightness;

    @JsonCreator
    public Brightness(@JsonProperty("value") int brightness) {
        this.brightness = brightness;
    }

    @Override
    public void apply(Image image) {
        BufferedImage original = image.getImage();
        BufferedImage newImage = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int width = original.getWidth();
        int height = original.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = original.getRGB(x, y);

                // Extraer componentes de color y el canal alfa
                int a = (rgb >> 24) & 0xFF; // Canal alfa
                int r = ((rgb >> 16) & 0xFF) + brightness;
                int g = ((rgb >> 8) & 0xFF) + brightness;
                int b = (rgb & 0xFF) + brightness;

                // Asegurar que los valores estén entre 0 y 255
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));

                // Reconstruir el píxel con el canal alfa original
                int newRGB = (a << 24) | (r << 16) | (g << 8) | b;
                newImage.setRGB(x, y, newRGB);
            }
        }

        image.setBufferedImage(newImage);
    }

    public void setBrightness(int brightness, Image image) {
        this.brightness = brightness;
        apply(image); // Vuelve a aplicar el brillo automáticamente
    }
}
