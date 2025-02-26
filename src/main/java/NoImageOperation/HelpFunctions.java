package NoImageOperation;

import Model.Image;
import java.awt.image.BufferedImage;

public class HelpFunctions {

    public static int normalize(int color, int min, int max){
        return (int) (((color - min) / (double) (max - min)) * 255);
    }

    public static int[] getRGBinArray(int pixel){
        int[] rgb = new int[3];
        rgb[0] = (pixel >> 16) & 0xff;
        rgb[1] = (pixel >> 8) & 0xff;
        rgb[2] = pixel & 0xff;
        return rgb;
    }

    public static int RGBtoPixel(int[] rgb){
        return (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
    }

    public static int multiplyPixel(int pixel, double factor){
        int[] rgb = getRGBinArray(pixel);

        // Multiplicar correctamente y asegurarse de que está en rango [0, 255]
        rgb[0] = Math.min(255, Math.max(0, (int) (rgb[0] * factor)));
        rgb[1] = Math.min(255, Math.max(0, (int) (rgb[1] * factor)));
        rgb[2] = Math.min(255, Math.max(0, (int) (rgb[2] * factor)));

        return RGBtoPixel(rgb);
    }

    public static int[][] multiplyImage(Image image, double factor){
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] pixels = new int[height][width]; // Matriz en formato [filas][columnas]

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getImage().getRGB(x, y);
                pixels[y][x] = multiplyPixel(pixel, factor);
            }
        }
        return pixels;
    }

    public static BufferedImage matrixToImage(int[][] matrix){
        int height = matrix.length;    // Número de filas
        int width = matrix[0].length;  // Número de columnas
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = matrix[y][x]; // Ahora accede correctamente
                image.setRGB(x, y, pixel);
            }
        }
        return image;
    }
}
