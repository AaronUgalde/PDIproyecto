package NoImageOperation;

import Model.Image;

import static NoImageOperation.HelpFunctions.getRGBinArray;
import static NoImageOperation.HelpFunctions.normalize;

public final class RGBtoHSI {
    public static double[][][] apply(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double[][][] result = new double[height][width][3];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getImage().getRGB(x, y);
                int[] rgb = getRGBinArray(pixel);
                double[] rgbNorm = normalize(rgb);

                double I = (rgbNorm[0] + rgbNorm[1] + rgbNorm[2]) / 3.0;

                double minRGB = Math.min(rgbNorm[0], Math.min(rgbNorm[1], rgbNorm[2]));
                double S = (I == 0) ? 0 : 1 - (minRGB/I);

                double num = 0.5*((rgbNorm[0] - rgbNorm[1]) + (rgbNorm[0] - rgbNorm[2]));
                double den = Math.sqrt((rgbNorm[0] - rgbNorm[1]) * (rgbNorm[0] - rgbNorm[2]) + (rgbNorm[0] - rgbNorm[2]));
                double theta = (den == 0) ? 0 : Math.acos(num/den);
                double H = (rgbNorm[2] <= rgbNorm[1]) ? theta : (2 * Math.PI - theta);

                result[y][x][0] = H;
                result[y][x][1] = S;
                result[y][x][2] = I;
            }
        }
        return result;
    }
}
