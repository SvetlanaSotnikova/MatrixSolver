package methods;

import logger.LoggerFactoryUtil;
import logger.LoggerMatrix;
import utils.Matrix;
import org.slf4j.Logger;

import java.util.Arrays;

/**
 * Класс для реализации метода Холецкого.
 */
// такие случаи для использования холецкого вообще бывают?
public class Cholesky {
    private static final Logger logger = LoggerFactoryUtil.getLogger(Cholesky.class);

    /**
     * Печатает решение методом Холецкого.
     */
    public static void printCholesky() {
        logger.info("\n*********************************************");
        logger.info("\nРешение методом Холецки".toUpperCase());

        // копируем матрицы в переменные, чтобы не было не явного преобразования
        double[][] A = Arrays.stream(Matrix.A).map(double[]::clone).toArray(double[][]::new);
        double[] b = Arrays.copyOf(Matrix.b, Matrix.b.length);

        double[][] H = choleskyDecomposition(A);

        assert H != null; // if H != null :
        LoggerMatrix.logMatrix(H, "H");
        LoggerMatrix.logTransposedMatrix(H, "H^T");

        double[] x = solveCholesky(A, b, H); // результат решения
        // Логируем значение x[i]
        LoggerMatrix.logSolution(x);
    }

    /**
     * Выполняет разложение Холецкого.
     *
     * @param A Матрица для разложения.
     * @return Матрица H, полученная в результате разложения.
     */
    private static double[][] choleskyDecomposition(double[][] A) {
        if (!checkMatrix(A)) {
            return null;
        }
        //Ax = b
        //A = H*H^T
        double[][] H = new double[A.length][A[0].length];
        logger.info("Начинаем разложение Холецкого.");
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j <= i; j++) {
                double sum = 0.0;

                for (int k = 0; k < j; k++) {
                    sum += H[i][k] * H[j][k];
                    logger.info(String.format("sum = %.3f * %.3f (h[%d][%d], h[%d][%d])",
                            H[i][k], H[j][k], i + 1, k + 1, j + 1, k + 1));
                }

                if (i == j) {
                    H[i][j] = Math.sqrt(A[i][i] - sum);
                } else {
                    H[i][j] = (1.0 / H[j][j]) * (A[i][j] - sum);
                }

                logger.info(String.format("H[%d][%d] = %.4f", i + 1, j + 1, H[i][j]));
            }
        }
        logger.info("Разложение Холецкого завершено.");
        return H;
    }

    /**
     * Решает уравнение Hy = b. <br>
     * Решает уравнение H^Tx = y.
     *
     * @param A Матрица коэффициентов.
     * @param b Вектор свободных членов.
     * @param H Матрица разложения Холецкого.
     * @return Решение x.
     */
    private static double[] solveCholesky(double[][] A, double[] b, double[][] H) {
        double[] y = new double[b.length];
        double[] x = new double[b.length];

        logger.info("Начинаем решение Hy = b.");

        //Hy = b
        for (int i = 0; i < b.length; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++) {
                sum += H[i][j] * y[j];
            }
            y[i] = (b[i] - sum) / H[i][i];
            logger.info("y[{}] = {}", i + 1, y[i]); // Логируем значение y[i]
        }
        logger.info("Решение для y завершено. Переход к решению H^T * x = y.");

        //y = H^T * x
        for (int i = A.length - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < A.length; j++) {
                sum += H[j][i] * x[j];
            }
            x[i] = (y[i] - sum) / H[i][i];
        }
        return x;
    }

    /**
     * Проверяет, является ли матрица квадратной, симметричной и положительно определённой.
     *
     * @param A матрица для проверки.
     * @return true, если матрица соответствует условиям для разложения Холецкого, иначе false.
     */
    private static boolean checkMatrix(double[][] A) {
        if (A == null || A.length == 0 || A[0].length == 0) {
            return logAndReturn("Матрица пустая или некорректная.", false);
        }

        if (A.length != A[0].length) {
            return logAndReturn("Матрица не квадратная. Метод Холецкого не сработает.", false);
        }

        if (!isSymmetric(A)) {
            return logAndReturn("Матрица не симметрична относительно главной диагонали. Метод Холецкого не сработает.", false);
        }


        for (int i = 0; i < A.length; i++) {
            if (determinant(A, i) <= 0) {
                return logAndReturn("Матрица не положительно определенная. Метод Холецкого не сработает.", false);
            }
        }

        return logAndReturn("Матрица положительно определенная и симметрична относительно главной диагонали.", true);
    }

    /**
     * Проверяет, является ли матрица симметричной.
     *
     * @param matrix матрица для проверки.
     */
    private static boolean isSymmetric(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < i; j++) {
                if (Math.abs(matrix[i][j] - matrix[j][i]) > 1e-10) {
                    return false; // матрица не симметрична
                }
            }
        }
        return true; // матрица симметрична
    }

    /**
     * Вычисляет определитель матрицы с использованием метода Гаусса.
     *
     * @param matrix матрица для вычисления определителя.
     * @param n      размерность матрицы.
     * @return значение определителя.
     */
    private static double determinant(double[][] matrix, int n) {
        double[][] tempMatrix = new double[n][];
        for (int i = 0; i < n; i++) {
            tempMatrix[i] = Arrays.copyOf(matrix[i], n);
        }

        double det = 1;

        for (int i = 0; i < n; i++) {
            double pivot = tempMatrix[i][i];
            if (pivot == 0) return 0; // Определитель равен нулю

            for (int j = i + 1; j < n; j++) {
                double factor = tempMatrix[j][i] / pivot;
                for (int k = i; k < n; k++) {
                    tempMatrix[j][k] -= factor * tempMatrix[i][k];
                }
            }
            det *= pivot;
        }
        return det;
    }


    /**
     * Вспомогательный метод для логирования и возврата результата.
     *
     * @param message сообщение для логирования.
     * @param result  результат проверки.
     * @return возвращает результат проверки.
     */
    private static boolean logAndReturn(String message, boolean result) {
        logger.info(message);
        return result;
    }
}
