package NoImageOperation;

public final class VectorOperations {
    public static int[] substraction(int[] v1, int[] v2) {
        if(v1.length != v2.length) {
            throw new IllegalArgumentException("The vectors must have the same length");
        }
        int[] result = new int[v1.length];
        for(int i = 0; i < v1.length; i++) {
            result[i] = v1[i] - v2[i];
        }
        return result;
    }

    public static int[] sum(int[] v1, int[] v2) {
        if(v1.length != v2.length) {
            throw new IllegalArgumentException("The vectors must have the same length");
        }
        int[] result = new int[v1.length];
        for(int i = 0; i < v1.length; i++) {
            result[i] = v1[i] + v2[i];
        }
        return result;
    }


}
