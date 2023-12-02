package main.java;

import java.math.BigInteger;

public class EuklidAlgorithm {

    BigInteger x0 = BigInteger.ONE;
    BigInteger y0 = BigInteger.ZERO;
    BigInteger x1 = BigInteger.ZERO;
    BigInteger y1 = BigInteger.ONE;


    public BigInteger getGcd(BigInteger Z, BigInteger N) {
        BigInteger R = Z.mod(N);

        if(R.compareTo(BigInteger.ZERO) > 0) {
            return getGcd(N, R);
        } else {
            return N;
        }
    }

    public BigInteger getGcdExtended(BigInteger Z, BigInteger N) {
        BigInteger R = Z.mod(N);
        BigInteger D = Z.divide(N);

        BigInteger lastX1 = x1;
        BigInteger lastY1 = y1;

        x1 = x0.subtract(D.multiply(x1));
        y1 = y0.subtract(D.multiply(y1));
        x0 = lastX1;
        y0 = lastY1;

        if (R.compareTo(BigInteger.ZERO) > 0) {
            return getGcdExtended(N, R);
        } else {
            return y0;
        }
    }

}
