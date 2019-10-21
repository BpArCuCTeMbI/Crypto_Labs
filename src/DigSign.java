import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

public class DigSign {

    public static int bitSet = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter source message: ");
        String source = sc.nextLine();
        System.out.println("Source message: " + source);

        byte bytes[] = source.getBytes();

        for(int i = 0; i < bytes.length; i++){
            bitSet += Integer.bitCount(bytes[i]);
        }
        System.out.println("Hash of source is: " + bitSet);

        Random rnd = new Random();

        int p = rnd.nextInt(100000000);
        while(!isPrime(p)){
            p = rnd.nextInt(10000000);
        }

        long q = rnd.nextInt((p - 2 - 3) + 1) + 3;
        while( (!isPrime(q)) || ( (p - 1 ) % q != 0 ) ){
            q = rnd.nextInt((p - 2 - 3) + 1) + 3;
        }
        long z = (p - 1) / q;
        long g = pow(rnd.nextInt((p - 2 - 2) + 1) + 2, z) % p;
        System.out.println("Prime p: " + p);
        System.out.println("Prime q, (p-1) mod q equals 0: " + q);
        System.out.println("g, multiplicative order of g mod p equals q: " + g);

        int x = 0;
        x = rnd.nextInt((int) ((q - 1 - 2) + 1)) + 2;
        long y = pow(g, x) % p;
        System.out.println("Secret key for signature x: " + x);
        System.out.println("Public key for signature y: " + y);
        long r = 0;
        long s = 0;

        long k = 0;
        while(r == 0 || s == 0){
            k = rnd.nextInt((int) ((q - 1 - 1) + 1)) + 1;
            r = (pow(g, k) % p ) % q;
            s = (x * r + k * bitSet) % q;
        }

        System.out.println("Digital signature: (" + r + ", " + s + "),\nMessage: " + source);
        boolean ifValid = isValid(r, s, q, p, bitSet, y, g);

        String wrong = "wrong";
        bytes = wrong.getBytes();

        bitSet = 0;
        for(int i = 0; i < bytes.length; i++){
            bitSet += Integer.bitCount(bytes[i]);
        }
        System.out.println("Hash of wrong source is: " + bitSet);
        r = 0;
        s = 0;

        k = 0;
        while(r == 0 || s == 0){
            k = rnd.nextInt((int) ((q - 1 - 1) + 1)) + 1;
            r = (pow(g, k) % p ) % q;
            s = (x * r + k * bitSet) % q;
        }

        System.out.println("Digital signature: (" + r + ", " + s + "),\nMessage: " + source);
        ifValid = isValid(r, s, q, p, bitSet, y, g);

    }

    public static boolean isPrime(long num) {
        for(int i = 2; i < Math.sqrt(num) + 1; i++){
            if(num % i == 0){
                return false;
            }
        }
        return true;
    }

    public static long pow(long a, long b){
        for(int i = 0; i < b; i++){
            a *= a;
        }
        return a;
    }

    public static boolean isValid(long r, long s, long q, long p, long h, long y, long g){
        long w = pow(h, q - 2) % q;
        long u1 = (s * w) % q;
        long u2 = ((q - r) * w) % q;
        long v = ((pow(g, u1) * pow(y, u2)) % p) % q;
        if( v == r){
            System.out.println("v == r, signature is valid");
            return true;
        }else {
            System.out.println("v != r, signature is invalid");
            return false;
        }
    }

}
