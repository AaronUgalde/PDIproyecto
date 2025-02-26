package Model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {
    private BufferedImage image;

    public Image(String path) throws IOException {
        this.image = ImageIO.read(new File(path));
    }

    public Image(BufferedImage Image){
        this.image = Image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void saveImage(File file) throws IOException {
        ImageIO.write(image, "png", file);
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public void setBufferedImage(BufferedImage newImage) {
        this.image = newImage;
    }
}
