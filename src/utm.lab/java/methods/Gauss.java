package methods;

import logger.LoggerFactoryUtil;
import logger.LoggerMatrix;
import org.slf4j.Logger;
import utils.Matrix;

import java.util.Arrays;

/**
 * Класс для реализации метода Гаусса.
 */
public class Gauss {
    private static final Logger logger = LoggerFactoryUtil.getLogger(Gauss.class);

    /**
     * Печатает решение системы линейных уравнений, используя метод Гаусса.
     */
    public static void printGauss() {
        logger.info("Метода Гаусса для решения системы уравнений.".toUpperCase());
        double[][] matrixA = Arrays.stream(Matrix.A).map(double[]::clone).toArray(double[][]::new);
        double[] vectorB = Arrays.copyOf(Matrix.b, Matrix.b.length);
        double[] solution = gaussianElimination(matrixA, vectorB);
        LoggerMatrix.logSolution(solution); // Логируем решение
    }

    /**
     * Выполняет метод исключения Гаусса для решения системы линейных уравнений.
     *
     * @param A матрица коэффициентов.
     * @param b вектор свободных членов.
     * @return массив значений переменных, полученных в результате решения системы.
     */
    private static double[] gaussianElimination(double[][] A, double[] b) {
        int Alen = A.length;

        // прямо ход метода Гауса
        for (int el = 0; el < Alen; el++) {
            int max = el;
            for (int i = el + 1; i < Alen; i++) {
                if (Math.abs(A[i][el]) > Math.abs(A[max][el])) {
                    max = i;
                }
            }
            logger.info("Выбрана строка {} с максимальным элементом для столбца {}", el + 1, max + 1);

            // меняем строки местми в зависимости от максмального первого элемента строки
            double[] temp = A[el];
            A[el] = A[max];
            A[max] = temp;

            double res = b[el];
            b[el] = b[max];
            b[max] = res;

            // приводим матрицу в треугольную форму
            for (int i = el + 1; i < Alen; i++) {
                double factor = A[i][el] / A[max][max];
                b[i] -= factor * b[max];
                logger.debug("Обновление свободного члена b[{}]: {}", i, b[i]);
                for (int j = el; j < Alen; j++) {
                    A[i][j] -= factor * A[el][j];
                    logger.debug("Обновление элемента A[{}][{}]: {}", i, j, A[i][j]);
                }
            }

            // Логируем текущую матрицу A и вектор b
            LoggerMatrix.logMatrix(A, "A");
            LoggerMatrix.loggerVectorB(b);
        }

        logger.info("Прямой ход завершён. Начало обратного хода.");

        // обратный ход метода гауса
        double[] x = new double[Alen];
        for (int i = Alen - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < Alen; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
            logger.debug("Вычислено значение x[{}]: {}", i + 1, x[i]);
        }

        logger.info("Обратный ход завершён. Решение найдено.");

        return x;
    }
}
