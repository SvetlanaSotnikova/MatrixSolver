package methods;

import logger.LoggerFactoryUtil;
import logger.LoggerMatrix;
import org.slf4j.Logger;
import utils.Epsilons;
import utils.Matrix;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Класс для реализации метода Якоби.
 */
public class Jacobi {
    private static final Logger logger = LoggerFactoryUtil.getLogger(Jacobi.class);

    /**
     * Печатает решение системы линейных уравнений методом Якоби.
     */
    public static void printJacobi() {
        logger.info("Решение системы уровнений методом Якоби".toUpperCase());
        double[][] matrixA = Arrays.stream(Matrix.A).map(double[]::clone).toArray(double[][]::new);
        double[] vectorB = Arrays.copyOf(Matrix.b, Matrix.b.length);
        double epsilon = Epsilons.EPSILON_1;
        double[] solution = jacobiMethod(matrixA, vectorB, epsilon);
        LoggerMatrix.logSolution(solution);
    }

    /**
     * Выполняет итерационный метод Якоби для решения системы линейных уравнений.
     *
     * @param A матрица коэффициентов.
     * @param b вектор свободных членов.
     * @param epsilon допустимая погрешность для проверки сходимости.
     * @return массив значений переменных, полученных в результате решения системы.
     */
    private static double[] jacobiMethod(double[][] A, double[] b, double epsilon) {
        double[] xNew = new double[b.length];
        double[] xOld = new double[b.length];
        boolean converged = false;
        int iteration = 0;

        while (!converged) {
            iteration++;

            logger.info("Iteration: {}", iteration);

            for (int i = 0; i < b.length; i++) {
                double sum = 0;
                for (int j = 0; j < b.length; j++) {
                    if (i != j) {
                        sum += A[i][j] * xOld[j];
                        logger.info(String.format(
                                "sum += A[%d][%d] * xOld[%d] -> sum += %.4f * %.4f = %.4f",
                                i + 1, j + 1, j + 1, A[i][j], xOld[j], sum));
                    }
                }
                xNew[i] = (b[i] - sum) / A[i][i];
                logger.info(String.format("x[%d] = (b[%d] - sum)/A[%d][%d] -> x[%d] = (%.4f - %.4f) / %.4f = %.4f"
                        , i + 1, i + 1, i + 1, i + 1, i + 1
                        , b[i], sum, A[i][i], xNew[i]));
            }

            converged = checkConvergence(xOld, xNew, epsilon);

            xOld = Arrays.copyOf(xNew, xNew.length);
        }
        return xNew;
    }

    /**
     * Проверяет сходимость метода Якоби.
     *
     * @param xOld старые значения переменных.
     * @param xNew новые значения переменных.
     * @param epsilon допустимая погрешность для проверки сходимости.
     * @return true, если решение сошлось, false в противном случае.
     */
    private static boolean checkConvergence(double[] xOld, double[] xNew, double epsilon) {
        for (int i = 0; i < xOld.length; i++) {
            if (Math.abs(xOld[i] - xNew[i]) > epsilon) {
                return false;
            }
        }
        return true;
    }
}
