package Operations.Histogram;

import GUI.ImageInterface;
import Model.Image;
import NoImageOperation.RGBtoHSI;
import NoImageOperation.RGBtoHSV;
import NoImageOperation.RGBtoYIQ;
import Operations.Operation;
import Operations.RGB.ColorChannel;
import Operations.RGB.RGB;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import static NoImageOperation.HelpFunctions.getChanel;
import static NoImageOperation.HelpFunctions.imageToMatrix;

public class Histogram implements Operation {

    private final TypeOfHistogram typeOfHistogram;

    @JsonCreator
    public Histogram(@JsonProperty("chanel") TypeOfHistogram typeOfHistogram) {
        this.typeOfHistogram = typeOfHistogram;
    }

    @Override
    public void apply(Image image) {
        BufferedImage histImage = null;

        switch (typeOfHistogram) {
            case Y: {
                double[][][] yiqImage = RGBtoYIQ.apply(image);
                // Se extrae el canal Y (índice 0) de la imagen YIQ
                double[][] channel = getChanel(yiqImage, 0);
                histImage = plotHist(getHist(channel));
                break;
            }
            case V: {
                double[][][] hsvImage = RGBtoHSV.apply(image);
                // Se extrae el canal V (valor) de la imagen HSV
                double[][] channel = getChanel(hsvImage, 0);
                histImage = plotHist(getHist(channel));
                break;
            }
            case I: {
                double[][][] hsiImage = RGBtoHSI.apply(image);
                // Se extrae el canal I (intensidad) de la imagen HSI; en este caso se usa el canal de índice 2
                double[][] channel = getChanel(hsiImage, 2);
                histImage = plotHist(getHist(channel));
                break;
            }
            case RED: {
                RGB red = new RGB(ColorChannel.RED);
                red.apply(image);
                int[][] redChannel = imageToMatrix(image);
                histImage = plotHist(getHist(redChannel));
                break;
            }
            case GREEN: {
                RGB green = new RGB(ColorChannel.GREEN);
                green.apply(image);
                int[][] greenChannel = imageToMatrix(image);
                histImage = plotHist(getHist(greenChannel));
                break;
            }
            case BLUE: {
                RGB blue = new RGB(ColorChannel.BLUE);
                blue.apply(image);
                ImageInterface blueImage = new ImageInterface(image, 200, 200);
                int[][] blueChannel = imageToMatrix(image);
                histImage = plotHist(getHist(blueChannel));
                break;
            }
            default:
                throw new UnsupportedOperationException("Tipo de histograma no soportado: " + typeOfHistogram);
        }

        // Guarda la imagen del histograma en un archivo PNG
        try {
            String fileName = "histogram_" + typeOfHistogram + ".png";
            ImageIO.write(histImage, "png", new File(fileName));
            System.out.println("Imagen del histograma guardada como: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        image.setBufferedImage(histImage);
    }

    /**
     * Calcula el histograma a partir de una matriz de valores double.
     *
     * @param channel Matriz con valores de intensidad.
     * @return Arreglo de 256 posiciones con la frecuencia de cada nivel (0 a 255).
     */
    private static int[] getHist(double[][] channel) {
        int width = channel.length;
        int height = channel[0].length;
        int[] hist = new int[256];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Se redondea el valor a entero y se asegura que esté en [0,255]
                int value = (int) Math.round(channel[i][j]);
                value = Math.max(0, Math.min(255, value));
                hist[value]++;
            }
        }
        return hist;
    }

    /**
     * Calcula el histograma a partir de una matriz de valores enteros.
     *
     * @param channel Matriz con valores de intensidad.
     * @return Arreglo de 256 posiciones con la frecuencia de cada nivel (0 a 255).
     */
    private static int[] getHist(int[][] channel) {
        int width = channel.length;
        int height = channel[0].length;
        int[] hist = new int[256];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int value = channel[i][j];
                System.out.println("value: " + value);
                value = Math.max(0, Math.min(255, value));
                hist[value]++;
            }
        }
        return hist;
    }

    /**
     * Genera una imagen del histograma a partir de un arreglo de frecuencias.
     *
     * @param hist Arreglo de 256 posiciones con la frecuencia de cada nivel.
     * @return BufferedImage con el histograma dibujado.
     */
    private static BufferedImage plotHist(int[] hist) {
        final int imageWidth = 512;  // Ancho de la imagen del histograma
        final int imageHeight = 400; // Alto de la imagen del histograma

        BufferedImage histogramImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = histogramImage.createGraphics();

        // Fondo blanco para mayor estética
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imageWidth, imageHeight);

        // Se busca el valor máximo del histograma para escalar las barras
        int max = 0;
        for (int value : hist) {
            if (value > max) {
                max = value;
            }
        }
        if (max == 0) {
            max = 1;
        }

        // Ancho de cada barra (bin)
        int binWidth = imageWidth / hist.length;

        // Dibujo de las barras en color negro
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < hist.length; i++) {
            System.out.print(hist[i] + "\t");
            int binHeight = (int) (((double) hist[i] / max) * imageHeight);
            int x = i * binWidth;
            int y = imageHeight - binHeight;
            g2d.fillRect(x, y, binWidth, binHeight);
        }

        g2d.dispose();
        return histogramImage;
    }
}
