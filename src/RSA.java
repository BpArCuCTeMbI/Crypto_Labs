import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class RSA {
    public static void main(String[] args) {
        int p = 0;         //prime
        int q = 0;         //prime

        SecureRandom rnd = new SecureRandom();
        while (true) {
            p = rnd.nextInt(100);
            if (isPrime(p)) {
                break;
            }
        }
        while (true) {
            q = rnd.nextInt(100);
            if (isPrime(q)) {
                break;
            }
        }

        System.out.println("p = " + p);
        System.out.println("q = " + q);
        int n = p * q;
        int tmp = (p-1)*(q-1);
        BigInteger e = null;         //prime with (p-1)*(q-1)

        System.out.println("n = p * q = " + n);

        SecureRandom rnd2 = new SecureRandom();

        int i = 0 ;
        while(true) {
            i = rnd2.nextInt(tmp);
            if(gcd(i, tmp) == 1){
                e = BigInteger.valueOf(i);
                break;
            }
        }

        System.out.println("e = " + e);

        BigInteger d = e.modInverse(BigInteger.valueOf(tmp));

        System.out.println("d = " + d);
        System.out.println("public key: (" + e + ", " + n + ")");
        System.out.println("private key: (" + d + ", " + n + ")");

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter source message: ");
        String source = sc.nextLine();
        System.out.println("Source message: " + source);
        System.out.println("Source msg as hex: ");
        for(i = 0; i < source.length(); i++){
            System.out.printf(String.format("0x%08X ", (int)source.charAt(i)));
        }

        System.out.println("\nCypher as hex: ");
        int[] cypher = encrypt(source, e, n);
        for(i = 0; i < cypher.length; i++){
            System.out.printf(String.format("0x%08X ", cypher[i]));
        }
        System.out.println("\nDecrypted cypher as hex: ");
        int[] src = decrypt(cypher, d, n);
        for(i = 0; i < cypher.length; i++){
            System.out.printf(String.format("0x%08X ", src[i]));
        }
        /*
        System.out.println("\nDecrypted msg as text:");
        StringBuilder sb = new StringBuilder();
        for(int ch : src){
            sb.append(ch);
        }
        //System.out.println(sb.toString());
         */

    }

    public static int[] encrypt(String src, BigInteger e, int n){
        int[] cypher = new int[src.length()];
        int i = 0;
        for(int j : src.toCharArray()){
            j = modularPower(j, e.intValue(), n);
            cypher[i] = j;
            i++;
        }
        return cypher;
    }

    public static int[] decrypt(int[] cyp, BigInteger d, int n){
        int[] src = new int[cyp.length];
        int j = 0;
        for(int i = 0; i < cyp.length; i++){
            j = modularPower(cyp[i], d.intValue(), n);
            src[i] = j;
        }
        return src;
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

    static int modularPower(int x, int y, int p) {
        // Initialize result
        int res = 1;

        // Update x if it is more
        // than or equal to p
        x = x % p;

        while (y > 0) {
            // If y is odd, multiply x
            // with result
            if ((y & 1) == 1)
                res = (res * x) % p;

            // y must be even now
            // y = y / 2
            y = y >> 1;
            x = (x * x) % p;
        }
        return res;
    }

}
