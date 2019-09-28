import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RSA {
    public static void main(String[] args) {
        int p = 0;         //prime
        int q = 0;         //prime

        SecureRandom rnd = new SecureRandom();
        while (true) {
            p = rnd.nextInt(500);
            if (isPrime(p)) {
                break;
            }
        }
        while (true) {
            q = rnd.nextInt(500);
            if (isPrime(q)) {
                break;
            }
        }

        int n = p * q;
        int tmp = (p-1)*(q-1);
        BigInteger e = null;         //prime with (p-1)*(q-1)

        for(int i = 1; i < tmp; i++){
            if(gcd(i, tmp) == 1){
                e = BigInteger.valueOf(i);
            }
        }

        BigInteger d = e.modInverse(BigInteger.valueOf(tmp));

        System.out.println("p = " + p);
        System.out.println("q = " + q);
        System.out.println("n = p * q = " + n);
        System.out.println("e = " + e);
        System.out.println("d = " + d);
        System.out.println("public key: (" + e + ", " + n + ")");
        System.out.println("private key: (" + d + ", " + n + ")");
    }

        public static int gcd(int p, int q) {
            if (q == 0) {
                return p;
            }
            return gcd(q, p % q);
        }

    public static boolean isPrime(int n) {
        int i;
        for (i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
