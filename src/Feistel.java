import javax.swing.*;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Scanner;


//TODO реализовать циклический сдвиг. Возможно из-за этого не работает
public class Feistel {

    public static int rounds = 18;
    public static int[] key = new int[rounds];


    public static byte circularLeftShift(byte input, int n) {
        int numBitsInByte = 8;
        n = n % numBitsInByte;
        byte result = (byte)(input << n | input >>> (numBitsInByte - n));
        return result;
    }


    public static byte circularRightShift(byte input, int n) {
        int numBitsInByte = 8;
        n = n % numBitsInByte;
        byte result = (byte)(input >>> n | input << (numBitsInByte - n));
        return result;
    }

    public static void main(String[] args) {
        System.out.println("Enter message to encrypt:");
        Scanner sc = new Scanner(System.in);

        String source = sc.nextLine();
        ArrayList<Byte> cypher = encrypt(source);
        System.out.println("Encrypted msg as hex:\n" + cypherToHex(cypher));

    }

    public static ArrayList<Byte> feistelRounds(int rounds, byte[] left, byte[] right, int[] key) {
        SecureRandom rnd = new SecureRandom();
        byte[] buf = new byte[left.length];

        for (int i = 0; i < rounds - 1; i++) {
            key[i] = rnd.nextInt(32);
            for (int j = 0; j < left.length; j++) {
                left[j] = circularLeftShift(left[j] ,key[i]);
                buf[j] = left[j];
            }
            for (int j = 0; j < left.length; j++) {
                left[j] = (byte) (left[j] ^ right[j]);
            }
            for (int j = 0; j < right.length; j++) {
                right[j] = buf[j];
            }
        }

        //last iter is an exception
        key[rounds - 1] = rnd.nextInt(32);
        for (int j = 0; j < left.length; j++) {
            left[j] = circularLeftShift(left[j], key[rounds - 1]);
        }
        for (int j = 0; j < left.length; j++) {
            right[j] = (byte) (left[j] ^ right[j]);
        }


        ArrayList<Byte> cypher = new ArrayList<>();
        for (int i = 0; i < left.length; i++) {
            cypher.add(left[i]);
        }
        int j = 0;
        for(int i = 0; i < right.length; i++){
            cypher.add(right[j]);
            j++;
        }
        cypher.trimToSize();
        return cypher;
    }

    public static ArrayList<Byte> encrypt(String source){

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

        byte[] left = new byte[byteArr.size() / 2];
        byte[] right = new byte[byteArr.size() / 2];

        System.out.println("Source as hex bytes: ");
        for (byte b : byteArr) {
            System.out.printf("%x ", b);
        }
        System.out.printf("\n");

        for(int i = 0; i < (byteArr.size() / 2); i++){
            left[i] = byteArr.get(i);
        }

        int j = 0;
        for(int i = byteArr.size() / 2; i < byteArr.size(); i++){
            right[j] = byteArr.get(i);
            j++;
        }

        return feistelRounds(rounds, left, right, key);
    }

    public static String cypherToHex(ArrayList<Byte> cypher){
        StringBuilder sb = new StringBuilder();
        for(Byte c : cypher){
            sb.append(String.format("%02x ", c));
        }
        return sb.toString();
    }
}
