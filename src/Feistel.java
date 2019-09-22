import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Scanner;

public class Feistel {

    public static int rounds = 18;

    public static void main(String[] args) {
        System.out.println("Enter message to encrypt:");
        Scanner sc = new Scanner(System.in);

        String source = sc.nextLine();
        encrypt(source);
    }

    public static String encrypt(String source){
        byte[] left = new byte[32];
        byte[] right = new byte[32];

        ArrayList<Byte> byteArr = new ArrayList<Byte>();
        byte[] tmp = source.getBytes(Charset.forName("UTF-8"));
        for(byte b : tmp){
            byteArr.add(b);
        }

        byteArr.trimToSize();
        if(byteArr.size() % 64 != 0) {
            int extraBytes = 64 - (byteArr.size() % 64);
            SecureRandom rnd = new SecureRandom();
            byte[] tmpByte = new byte[1];
            for (int i = 0; i < extraBytes; i++) {
                rnd.nextBytes(tmpByte);
                byte oneTmpByte = tmpByte[0];
                byteArr.add(oneTmpByte);
            }
            byteArr.trimToSize();
        }
        System.out.println("Size of byteArr is: " + byteArr.size());

        System.out.println("Source as hex bytes: ");
        for (byte b : byteArr) {
            System.out.printf("%x ", b);
        }

        for(int i = 0; i < rounds; i++){

        }
        return null;
    }
}
