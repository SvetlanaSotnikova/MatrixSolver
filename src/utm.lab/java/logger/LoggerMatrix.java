package logger;

import org.slf4j.Logger;

import java.util.Arrays;
import java.util.function.BiFunction;

/***
 * Класс для логирования матриц и векторов с использованием SLF4J.
 */
public class LoggerMatrix {

    // Логгер для класса LoggerMatrix
    private static final Logger logger = LoggerFactoryUtil.getLogger(LoggerMatrix.class);

    /***
     * Логирует текущую матрицу с заданным именем.
     * @param matrix матрица для логирования
     * @param matrixName имя матрицы
     */
    public static void logMatrix(double[][] matrix, String matrixName) {
        String matrixDescription = "Mатрица " + matrixName;
        logMatrixCommon(matrix, matrixDescription, (i, j) -> matrix[i][j]);
    }

    /***
     * Логирует транспонированную матрицу с заданным именем.
     * @param matrix матрица для логирования
     * @param matrixName имя матрицы
     */
    public static void logTransposedMatrix(double[][] matrix, String matrixName) {
        String matrixDescription = "Транспонированная матрица " + matrixName;
        logMatrixCommon(matrix, matrixDescription, (i, j) -> matrix[j][i]);
    }

    /***
     * Общий метод для логирования матриц.
     * @param matrix матрица для логирования
     * @param description описание матрицы
     * @param getElement функция для получения элемента матрицы
     */
    private static void logMatrixCommon(double[][] matrix, String description, BiFunction<Integer, Integer, Double> getElement) {
        StringBuilder sb = new StringBuilder();
        sb.append(description).append(":\n");

        for (int i = 0; i < matrix[0].length; i++) { // Перебор строк
            sb.append("| ");
            for (int j = 0; j < matrix.length; j++) { // Перебор столбцов
                sb.append(String.format("%8.4f ", getElement.apply(i, j))); // Форматирование и добавление элемента
            }
            sb.append("\n");
        }
        logger.info(sb.toString()); // Логирование матрицы
    }

    /***
     * Логирует текущий вектор b.
     * @param b вектор для логирования
     */
    public static void loggerVectorB(double[] b) {
        logger.info("Текущий вектор b: | {} |\n",
                String.join(" | ",
                        Arrays.stream(b)
                                .mapToObj(v -> String.format("%.4f", v)) // Форматирование значений вектора
                                .toArray(String[]::new))); // Преобразование в массив строк
    }

    /***
     * Логирует решение системы уравнений.
     * @param x массив решений
     */
    public static void logSolution(double[] x) {
        logger.info("Решение системы уравнений:");
        for (int i = 0; i < x.length; i++) {
            logger.info("x[{}] = {}", i + 1, String.format("%.4f", x[i]));
        }
    }
}
