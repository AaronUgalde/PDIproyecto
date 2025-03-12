package Operations;

import Model.Image;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.BufferedImage;
import java.util.Random;

public class GammaNoise implements Operation {
    private final int alpha;
    private final float variance;
    private final Random random;

    @JsonCreator
    public GammaNoise(@JsonProperty("alpha") int alpha, @JsonProperty("variance") float variance) {
        this.alpha = Math.max(1, alpha); // Alpha debe ser >= 1
        this.variance = variance;
        this.random = new Random();
    }

    @Override
    public void apply(Image image) {
        Gray grayOperation = new Gray();
        grayOperation.apply(image);
        BufferedImage newBufferedImage = image.getImage();

        int rows = newBufferedImage.getHeight();
        int cols = newBufferedImage.getWidth();

        float a = (float) Math.sqrt((double)variance / (double)alpha) / 2;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                float image1 = 0.0f;

                for (int i = 1; i < alpha; i++) {
                    float noise = (float) Math.sqrt(-2 * a * a *
                            Math.log(1.0 - (float)random.nextInt(32767) / 32767.0f));

                    float theta = (float)random.nextInt(32767) / 32767.0f * 1.9175345E-4f - 3.14159265f;

                    float rx = noise * (float)Math.cos(theta);
                    float ry = noise * (float)Math.sin(theta);

                    noise = rx * rx + ry * ry;

                    image1 = image1 + noise;
                }

                int noise1 = (int)(image1 + 0.5f);
                if (noise1 > 255) noise1 = 255;

                int originalRGB = newBufferedImage.getRGB(x, y);
                int r = (originalRGB >> 16) & 0xFF;
                int g = (originalRGB >> 8) & 0xFF;
                int b = originalRGB & 0xFF;

                r = Math.min(255, Math.max(0, r + noise1));
                g = Math.min(255, Math.max(0, g + noise1));
                b = Math.min(255, Math.max(0, b + noise1));

                int newRGB = (r << 16) | (g << 8) | b;
                newBufferedImage.setRGB(x, y, newRGB);
            }
        }

        image.setBufferedImage(newBufferedImage);
    }
}