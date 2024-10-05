
import methods.Cholesky;
import methods.Gauss;
import methods.GaussSeidel;
import methods.Jacobi;

public class Main {
    public static void main(String[] args) {
        Gauss.printGauss();
        Cholesky.printCholesky();
        Jacobi.printJacobi();
        GaussSeidel.printGaussSeidel();
    }
}
