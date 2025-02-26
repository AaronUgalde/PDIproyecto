package Operations.RGB;

import Model.Image;
import Operations.Operation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.awt.image.BufferedImage;
import static NoImageOperation.HelpFunctions.RGBtoPixel;
import static NoImageOperation.HelpFunctions.getRGBinArray;

public class RGB implements Operation {
    private ColorChannel color;

    @JsonCreator
    public RGB(@JsonProperty("color") ColorChannel color) {
        this.color = color;
    }

    @Override
    public void apply(Image image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getImage().getRGB(x, y);
                int[] rgb = getRGBinArray(pixel);
                int newPixel = 0;
                switch (color) {
                    case RED:
                        rgb[1] = 0;
                        rgb[2] = 0;
                        newPixel = RGBtoPixel(rgb);
                        break;
                    case GREEN:
                        rgb[0] = 0;
                        rgb[2] = 0;
                        newPixel = RGBtoPixel(rgb);
                        break;
                    case BLUE:
                        rgb[0] = 0;
                        rgb[1] = 0;
                        newPixel = RGBtoPixel(rgb);
                        break;
                }
                newImage.setRGB(x, y, newPixel);
            }
        }
        image.setBufferedImage(newImage);
    }

    // Opcional: getters y setters si lo requieres
    public ColorChannel getColor() {
        return color;
    }

    public void setColor(ColorChannel color) {
        this.color = color;
    }
}
