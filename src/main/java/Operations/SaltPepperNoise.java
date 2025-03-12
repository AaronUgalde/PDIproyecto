package Operations;

import Model.Image;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.BufferedImage;
import java.util.Random;

public class SaltPepperNoise implements Operation {
    private final float probability;
    private final Random random;

    @JsonCreator
    public SaltPepperNoise(@JsonProperty("probability") float probability) {
        this.probability = probability;
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

        int threshold = (int) (probability * 32768 / 2);
        int data1 = threshold + 16384;
        int data2 = 16384 - threshold;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int randValue = random.nextInt(32768);
                int newRGB = newBufferedImage.getRGB(x, y);

                if (randValue >= 16384 && randValue < data1) {
                    newRGB = 0x000000; // Negro (pepper noise)
                } else if (randValue <= data2 && randValue < 16384) {
                    newRGB = 0xFFFFFF; // Blanco (salt noise)
                }

                newImage.setRGB(x, y, newRGB);
            }
        }

        image.setBufferedImage(newImage);
    }
}
