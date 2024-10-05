package methods;

import logger.LoggerFactoryUtil;
import logger.LoggerMatrix;
import org.slf4j.Logger;
import utils.Epsilons;
import utils.Matrix;

import java.util.Arrays;

/**
 * Класс для реализации метода Гаусса — Зейделя.
 */
public class GaussSeidel {

    private static final Logger logger = LoggerFactoryUtil.getLogger(GaussSeidel.class);

    /**
     * Печатает решение системы линейных уравнений методом Гаусса — Зейделя
     * для различных значений epsilon.
     */
    public static void printGaussSeidel() {
        // Массив значений epsilon
        double[] epsilons = {Epsilons.EPSILON_1, Epsilons.EPSILON_2};
        String[] epsilonLabels = {"e10^-3", "e10^-5"};

        for (int i = 0; i < epsilons.length; i++) {
            logger.info("Решение системы уровнений методом Гаусса — Зейделя {}".toUpperCase(), epsilonLabels[i]);

            // Клонируем матрицу и вектор для каждого значения epsilon
            double[][] matrixA = Arrays.stream(Matrix.A).map(double[]::clone).toArray(double[][]::new);
            double[] vectorB = Arrays.copyOf(Matrix.b, Matrix.b.length);

            double[] solution = gaussSeidelMethod(matrixA, vectorB, epsilons[i]);
            LoggerMatrix.logSolution(solution);
        }
    }

    /**
     * Выполняет итерационный метод Гаусса — Зейделя для решения системы линейных уравнений.
     *
     * @param A       матрица коэффициентов.
     * @param b       вектор свободных членов.
     * @param epsilon допустимая погрешность для проверки сходимости.
     * @return массив значений переменных, полученных в результате решения системы.
     */
    private static double[] gaussSeidelMethod(double[][] A, double[] b, double epsilon) {
        double[] xNew = new double[b.length];
        double[] xOld = new double[b.length];
        boolean converged = false;
        int iteration = 0;

        // Инициализируем xOld нулями
        Arrays.fill(xOld, 0.0);

        while (!converged) {
            iteration++;

            logger.info("Iteration: {}", iteration);

            for (int i = 0; i < b.length; i++) {
                double sum = 0;

                // Считаем сумму для x[i]
                for (int j = 0; j < b.length; j++) {
                    if (i != j) {
                        sum += A[i][j] * xNew[j]; // Используем уже обновленные значения
                        logger.info(String.format(
                                "sum += A[%d][%d] * x[%d] -> sum += %.4f * %.4f = %.4f",
                                i + 1, j + 1, j + 1, A[i][j], xNew[j], sum));
                    }
                }

                // Вычисляем новое значение x[i]
                xNew[i] = (b[i] - sum) / A[i][i];
                logger.info(String.format("x[%d] = (b[%d] - sum)/A[%d][%d] -> x[%d] = (%.4f - %.4f) / %.4f = %.4f"
                        , i + 1, i + 1, i + 1, i + 1, i + 1
                        , b[i], sum, A[i][i], xNew[i]));
            }

            // Проверка сходимости
            converged = checkConvergence(xOld, xNew, epsilon);

            // Обновляем xOld для следующей итерации
            System.arraycopy(xNew, 0, xOld, 0, xNew.length);
        }
        return xNew;
    }

    /**
     * Проверяет сходимость метода Гаусса — Зейделя.
     *
     * @param xOld    старые значения переменных.
     * @param xNew    новые значения переменных.
     * @param epsilon допустимая погрешность для проверки сходимости.
     * @return true, если решение сошлось, false в противном случае.
     */
    private static boolean checkConvergence(double[] xOld, double[] xNew, double epsilon) {
        for (int i = 0; i < xOld.length; i++) {
            if (Math.abs(xOld[i] - xNew[i]) > epsilon) {
                return false; // Не сошлись
            }
        }
        return true; // Сошлись
    }
}
