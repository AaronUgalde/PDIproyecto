package Operations;

import Model.Image;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.BufferedImage;
import java.util.Random;

public class UniformNoise implements Operation {

    private final float mean;
    private final float variance;
    private final Random random;

    @JsonCreator
    public UniformNoise(
            @JsonProperty("mean") float mean,
            @JsonProperty("variance") float variance
    ) {
        this.mean = mean;
        this.variance = variance;
        this.random = new Random();
    }

    @Override
    public void apply(Image image) {

        Gray grayOperation = new Gray();
        grayOperation.apply(image);
        BufferedImage bufferedGray = image.getImage();

        int width = bufferedGray.getWidth();
        int height = bufferedGray.getHeight();

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                float r = (float) random.nextInt(32768);

                float noise = (float) Math.sqrt(variance) * r * 1.73205e-3f + mean;

                if (noise > 255) noise = 255;
                if (noise < 0)   noise = 0;

                int noiseInt = (int) (noise + 0.5f);

                int originalRGB = bufferedGray.getRGB(x, y);
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
