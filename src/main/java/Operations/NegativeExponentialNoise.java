package Operations;

import Model.Image;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.BufferedImage;
import java.util.Random;

public class NegativeExponentialNoise implements Operation {
    private final float variance;
    private final Random random;

    @JsonCreator
    public NegativeExponentialNoise(@JsonProperty("variance") float variance) {
        this.variance = variance;
        this.random = new Random();
    }

    @Override
    public void apply(Image image) {
        Gray grayOperation = new Gray();
        grayOperation.apply(image);
        BufferedImage newImage = image.getImage();
        int width = newImage.getWidth();
        int height = newImage.getHeight();

        float A = (float) Math.sqrt(variance);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float r1 = (float) random.nextInt(32768) / 32767.1f;
                float r2 = (float) random.nextInt(32768) / 32767.1f;

                float theta = 2.0f * 3.14159f * (1.0f - r1);

                float raiz = (float) Math.sqrt(-2.0f * Math.log(r2));
                float Rx = A * raiz * (float) Math.cos(theta);
                float Ry = A * raiz * (float) Math.sin(theta);

                float noise = Rx * Rx + Ry * Ry;

                if (noise > 255) noise = 255;
                if (noise < 0)   noise = 0;

                int noiseInt = (int) (noise + 0.5f);

                int originalRGB = newImage.getRGB(x, y);
                int r = (originalRGB >> 16) & 0xFF;
                int g = (originalRGB >>  8) & 0xFF;
                int b = (originalRGB      ) & 0xFF;

                r = clamp(r + noiseInt);
                g = clamp(g + noiseInt);
                b = clamp(b + noiseInt);

                int newRGB = (r << 16) | (g << 8) | b;
                newImage.setRGB(x, y, newRGB);
            }
        }

        image.setBufferedImage(newImage);
    }
    private int clamp(int val) {
        return Math.max(0, Math.min(255, val));
    }
}
