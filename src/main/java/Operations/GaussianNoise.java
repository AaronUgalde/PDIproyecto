package Operations;

import Model.Image;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.BufferedImage;
import java.util.Random;

import static NoImageOperation.HelpFunctions.RGBtoPixel;
import static NoImageOperation.HelpFunctions.getRGBinArray;

public class GaussianNoise implements Operation {

    private final float var;
    private final float mean;
    Random rand;

    @JsonCreator
    public GaussianNoise(@JsonProperty("variance") float var, @JsonProperty("mean") float mean) {
        this.var = var;
        this.mean = mean;
        rand = new Random();
    }

    @Override
    public void apply(Image image) {
        Gray grayOperation = new Gray();
        grayOperation.apply(image);
        BufferedImage newBufferedImage = image.getImage();
        Random rand = new Random();

        int rows = newBufferedImage.getHeight();
        int cols = newBufferedImage.getWidth();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                double u1 = 1.0 - rand.nextDouble();
                double u2 = rand.nextDouble();

                double noise = Math.sqrt(-2.0 * var * Math.log(u1));
                double theta = 2.0 * Math.PI * u2 - Math.PI;

                double pixelValue = noise * Math.cos(theta) + mean;

                pixelValue = Math.round(pixelValue);
                if (pixelValue > 255) pixelValue = 255;
                if (pixelValue < 0) pixelValue = 0;

                int gray = (int) pixelValue;
                int pixel = newBufferedImage.getRGB(x, y);
                int[] RGB = getRGBinArray(pixel);
                int[] newRGB = new int[]{RGB[0] + gray, RGB[1] + gray, RGB[2] + gray};
                int newPixel = RGBtoPixel(newRGB);
                newBufferedImage.setRGB(x, y, newPixel);
            }
        }
        image.setBufferedImage(newBufferedImage);
    }
}
