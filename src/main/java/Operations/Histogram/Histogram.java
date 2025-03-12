package Operations.Histogram;

import Model.Image;
import NoImageOperation.RGBtoHSI;
import NoImageOperation.RGBtoHSV;
import NoImageOperation.RGBtoYIQ;
import Operations.Operation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import static NoImageOperation.HelpFunctions.getChanel;
import static NoImageOperation.HelpFunctions.getRGBinArray;

public class Histogram implements Operation {

    private final TypeOfHistogram typeOfHistogram;
    private static final int HIST_WIDTH = 512;
    private static final int HIST_HEIGHT = 400;
    private static final int MARGIN = 40;
    private static final int GRAPH_WIDTH = HIST_WIDTH - (2 * MARGIN);
    private static final int GRAPH_HEIGHT = HIST_HEIGHT - (2 * MARGIN);

    @JsonCreator
    public Histogram(@JsonProperty("chanel") TypeOfHistogram typeOfHistogram) {
        this.typeOfHistogram = typeOfHistogram;
    }

    @Override
    public void apply(Image image) {
        BufferedImage histImage = null;
        int[] histogram = null;
        Color barColor = Color.BLACK;
        String title = "Histogram";

        switch (typeOfHistogram) {
            case Y: {
                double[][][] yiqImage = RGBtoYIQ.apply(image);
                double[][] channel = getChanel(yiqImage, 0);
                histogram = getHistFromDoubleChannel(channel);
                barColor = new Color(50, 50, 50);
                title = "Y Channel Histogram (YIQ)";
                break;
            }
            case V: {
                double[][][] hsvImage = RGBtoHSV.apply(image);
                double[][] channel = getChanel(hsvImage, 2); // V is index 2
                histogram = getHistFromDoubleChannel(channel);
                barColor = new Color(75, 75, 75);
                title = "V Channel Histogram (HSV)";
                break;
            }
            case I: {
                double[][][] hsiImage = RGBtoHSI.apply(image);
                double[][] channel = getChanel(hsiImage, 2); // I is index 2
                histogram = getHistFromDoubleChannel(channel);
                barColor = new Color(100, 100, 100);
                title = "I Channel Histogram (HSI)";
                break;
            }
            case RED: {
                histogram = calculateRGBHistogram(image, 0);
                barColor = new Color(220, 0, 0);
                title = "Red Channel Histogram";
                break;
            }
            case GREEN: {
                histogram = calculateRGBHistogram(image, 1);
                barColor = new Color(0, 180, 0);
                title = "Green Channel Histogram";
                break;
            }
            case BLUE: {
                histogram = calculateRGBHistogram(image, 2);
                barColor = new Color(0, 0, 220);
                title = "Blue Channel Histogram";
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported histogram type: " + typeOfHistogram);
        }

        if (histogram != null) {
            histImage = plotHistogram(histogram, barColor, title);
        }

        try {
            String fileName = "histogram_" + typeOfHistogram + ".png";
            File outputFile = new File(fileName);
            ImageIO.write(histImage, "png", outputFile);
            System.out.println("Histogram image saved as: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        image.setBufferedImage(histImage);
    }

    private int[] calculateRGBHistogram(Image image, int channelIndex) {
        int[] histogram = new int[256];
        BufferedImage originalImage = image.getImage();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = originalImage.getRGB(x, y);
                int[] rgb = getRGBinArray(pixel);
                histogram[rgb[channelIndex]]++;
            }
        }
        return histogram;
    }

    private int[] getHistFromDoubleChannel(double[][] channel) {
        int[] histogram = new int[256];
        int height = channel.length;
        int width = channel[0].length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = (int) Math.round((channel[y][x] / (double) 255) * 255);
                value = Math.max(0, Math.min(255, value));
                histogram[value]++;
            }
        }
        return histogram;
    }

    private BufferedImage plotHistogram(int[] histogram, Color barColor, String title) {
        BufferedImage histImage = new BufferedImage(HIST_WIDTH, HIST_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = histImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, HIST_WIDTH, HIST_HEIGHT);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (HIST_WIDTH - titleWidth) / 2, 20);

        g2d.drawLine(MARGIN, HIST_HEIGHT - MARGIN, HIST_WIDTH - MARGIN, HIST_HEIGHT - MARGIN); // X-axis
        g2d.drawLine(MARGIN, MARGIN, MARGIN, HIST_HEIGHT - MARGIN); // Y-axis

        int maxValue = 0;
        for (int value : histogram) {
            if (value > maxValue) {
                maxValue = value;
            }
        }

        if (maxValue == 0) {
            maxValue = 1;
        }

        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.drawString("0", MARGIN - 5, HIST_HEIGHT - MARGIN + 15);
        g2d.drawString("255", HIST_WIDTH - MARGIN - 10, HIST_HEIGHT - MARGIN + 15);
        g2d.drawString("0", MARGIN - 20, HIST_HEIGHT - MARGIN + 5);
        g2d.drawString(String.valueOf(maxValue), MARGIN - 30, MARGIN + 10);
        g2d.drawString("Pixel Value", HIST_WIDTH / 2 - 30, HIST_HEIGHT - 10);

        g2d.rotate(-Math.PI / 2);
        g2d.drawString("Frequency", -HIST_HEIGHT / 2 - 30, 15);
        g2d.rotate(Math.PI / 2);

        double barWidth = (double) GRAPH_WIDTH / 256;

        g2d.setColor(barColor);
        for (int i = 0; i < histogram.length; i++) {
            int barHeight = (int) (((double) histogram[i] / maxValue) * GRAPH_HEIGHT);
            int x = MARGIN + (int) (i * barWidth);
            int y = HIST_HEIGHT - MARGIN - barHeight;
            int width = Math.max(1, (int) barWidth - 1);
            g2d.fillRect(x, y, width, barHeight);
        }

        g2d.setColor(new Color(220, 220, 220));
        for (int i = 1; i < 5; i++) {
            int y = HIST_HEIGHT - MARGIN - (i * GRAPH_HEIGHT / 5);
            g2d.drawLine(MARGIN, y, HIST_WIDTH - MARGIN, y);
            g2d.drawString(String.valueOf(i * maxValue / 5), MARGIN - 30, y + 5);
        }

        g2d.dispose();
        return histImage;
    }
}