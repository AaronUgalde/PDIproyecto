package NoImageOperation;

import Model.Image;
import static NoImageOperation.HelpFunctions.getRGBinArray;
import static NoImageOperation.HelpFunctions.normalize;

public final class RGBtoYIQ {
    public static double[][][] apply(Image image){
        int width = image.getWidth();
        int height = image.getHeight();
        double[][][] result = new double[height][width][3];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int pixel = image.getImage().getRGB(x, y);
                int[] rgb = getRGBinArray(pixel);
                double[] rgbNorm = normalize(rgb);
                double Y = 0.299*rgbNorm[0] + 0.587*rgbNorm[1] + 0.114*rgbNorm[2];
                double I = 0.596*rgbNorm[0] - 0.274*rgbNorm[1] - 0.322*rgbNorm[2];
                double Q = 0.211*rgbNorm[0] - 0.523*rgbNorm[1] - 0.312*rgbNorm[2];
                result[y][x][0] = Y;
                result[y][x][1] = I;
                result[y][x][2] = Q;
            }
        }
        return result;
    }
}
