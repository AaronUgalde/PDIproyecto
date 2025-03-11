package Operations;

import GUI.ImageInterface;
import Model.Image;
import com.fasterxml.jackson.annotation.JsonCreator;

public class OpenJFrame implements Operation {

    @JsonCreator
    public OpenJFrame() {

    }

    @Override
    public void apply(Image image) {
        ImageInterface imageInterface = new ImageInterface(image, 100, 100);
    }
}
