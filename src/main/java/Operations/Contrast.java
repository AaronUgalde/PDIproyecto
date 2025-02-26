package Operations;

import Model.Image;
import NoImageOperation.LinearNormalization;
import java.awt.image.BufferedImage;
import static NoImageOperation.HelpFunctions.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Contrast implements Operation {
    private double factor;

    @JsonCreator
    public Contrast(@JsonProperty("value") double factor) {
        this.factor = factor;
    }

    @Override
    public void apply(Image image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        if (factor < 1) {
            newImage = matrixToImage(multiplyImage(image, factor));
        }
        if (factor > 1) {
            LinearNormalization normalization = new LinearNormalization();
            newImage = normalization.apply(multiplyImage(image, factor));
        }
        image.setBufferedImage(newImage);
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public double getFactor() {
        return factor;
    }
}
