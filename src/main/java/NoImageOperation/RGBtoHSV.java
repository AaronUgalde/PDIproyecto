package NoImageOperation;

import Model.Image;

import static NoImageOperation.HelpFunctions.getRGBinArray;
import static NoImageOperation.HelpFunctions.normalize;

public final class RGBtoHSV {
    public static double[][][] apply(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double[][][] result = new double[height][width][3];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getImage().getRGB(x, y);
                int[] rgb = getRGBinArray(pixel);
                double[] rgbNorm = normalize(rgb);
                double max = Math.max(rgbNorm[0], Math.max(rgbNorm[1], rgbNorm[2]));
                double min = Math.min(rgbNorm[0], Math.min(rgbNorm[1], rgbNorm[2]));
                double delta = max - min;

                double H = 0;
                double S = 0;
                double V = max;

                if(delta != 0){
                    if(max == rgbNorm[0]){
                        H = 60 * (((rgbNorm[1] - rgbNorm[2]) / delta) % 6);
                    }else if(max == rgbNorm[1]){
                        H = 60 * (((rgbNorm[1] - rgbNorm[0]) / delta) + 2);
                    }else if(max == rgbNorm[2]){
                        H = 60 * (((rgbNorm[0] - rgbNorm[1]) / delta) + 4);
                    }
                }

                if(H < 0) H += 360;

                result[y][x][0] = H;
                result[y][x][1] = S;
                result[y][x][2] = V;
            }
        }
        return result;
    }
}
