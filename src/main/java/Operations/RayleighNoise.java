package Operations;

import Model.Image;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.BufferedImage;
import java.util.Random;

public class RayleighNoise implements Operation {
    private final float variance;
    private final Random random;

    @JsonCreator
    public RayleighNoise(@JsonProperty("variance") float variance) {
        this.variance = variance;
        this.random = new Random();
    }

    @Override
    public void apply(Image image) {

        Gray grayOperation = new Gray();
        grayOperation.apply(image);
        BufferedImage newBufferedImage = image.getImage();

        int width = newBufferedImage.getWidth();
        int height = newBufferedImage.getHeight();

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float r = (float) random.nextInt(32768) / 32767.1f;

                float A = 2.3299f * (float) Math.sqrt(
                        variance * -1.0f * (float) Math.log(1.0f - r)
                );

                if (A > 255) A = 255;
                if (A < 0)   A = 0;

                int noiseInt = (int) (A + 0.5f);

                int originalRGB = newBufferedImage.getRGB(x, y);
                int red   = (originalRGB >> 16) & 0xFF;
                int green = (originalRGB >>  8) & 0xFF;
                int blue  =  originalRGB        & 0xFF;

                red   = clamp(red   + noiseInt);
                green = clamp(green + noiseInt);
                blue  = clamp(blue  + noiseInt);

                int newRGB = (red << 16) | (green << 8) | blue;
                newImage.setRGB(x, y, newRGB);
            }
        }

        image.setBufferedImage(newImage);
    }

    private int clamp(int val) {
        return Math.max(0, Math.min(255, val));
    }
}
