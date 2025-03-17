package NoImageOperation;

public final class MatrixOperations {

    public static int[][] sum(int[][] m1, int[][] m2) {
        if(m1.length != m2.length || m1[0].length != m2[0].length) {
            throw new IllegalArgumentException("The matrix must have the same length");
        }
        int[][] result = new int[m1.length][m2[0].length];
        for(int i = 0; i < m1.length; i++) {
            for(int j = 0; j < m2[0].length; j++) {
                result[i][j] = m1[i][j] + m2[i][j];
            }
        }
        return result;
    }

    public static int[][] substraction(int[][] m1, int[][] m2) {
        if(m1.length != m2.length || m1[0].length != m2[0].length) {
            throw new IllegalArgumentException("The matrix must have the same length");
        }
        int[][] result = new int[m1.length][m2[0].length];
        for(int i = 0; i < m1.length; i++) {
            for(int j = 0; j < m2[0].length; j++) {
                result[i][j] = m1[i][j] - m2[i][j];
            }
        }
        return result;
    }

    public static int[][] multiplication(int[][] A, int[][] B) {
        // Verificar que las matrices se puedan multiplicar
        if (A[0].length != B.length) {
            throw new IllegalArgumentException("Las matrices no se pueden multiplicar: "
                    + "el número de columnas de A debe ser igual al número de filas de B.");
        }

        int filasA = A.length;
        int columnasA = A[0].length; // También es igual a B.length
        int columnasB = B[0].length;

        int[][] resultado = new int[filasA][columnasB];

        // Multiplicación de matrices
        for (int i = 0; i < filasA; i++) {
            for (int j = 0; j < columnasB; j++) {
                resultado[i][j] = 0; // Inicializamos la posición
                for (int k = 0; k < columnasA; k++) {
                    resultado[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return resultado;
    }

}
