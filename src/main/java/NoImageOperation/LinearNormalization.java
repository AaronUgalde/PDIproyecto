package NoImageOperation;

import java.awt.image.BufferedImage;

import static NoImageOperation.HelpFunctions.getRGBinArray;
import static NoImageOperation.HelpFunctions.normalize;

public class LinearNormalization {
    public BufferedImage apply(int[][] image){
        int height = image.length;    // Número de filas (alto)
        int width = image[0].length;  // Número de columnas (ancho)

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Primer paso: Encontrar los valores mínimo y máximo de la imagen en escala de grises
        for (int y = 0; y < height; y++) {  // Recorrer por filas primero
            for (int x = 0; x < width; x++) {  // Luego recorrer por columnas
                int[] rgb = getRGBinArray(image[y][x]);  // image[y][x], no image[x][y]
                int gray = (rgb[0] + rgb[1] + rgb[2]) / 3; // Convertir a escala de grises

                min = Math.min(min, gray);
                max = Math.max(max, gray);
            }
        }

        // Segundo paso: Normalizar los valores de la imagen
        for (int y = 0; y < height; y++) {  // Nuevamente recorrer por filas primero
            for (int x = 0; x < width; x++) {
                int[] rgb = getRGBinArray(image[y][x]);  // Usar image[y][x]

                rgb[0] = normalize(rgb[0], min, max);
                rgb[1] = normalize(rgb[1], min, max);
                rgb[2] = normalize(rgb[2], min, max);

                int pixel = (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
                bufferedImage.setRGB(x, y, pixel); // Asegurar que se usa (x, y) correctamente
            }
        }

        return bufferedImage;
    }
}
