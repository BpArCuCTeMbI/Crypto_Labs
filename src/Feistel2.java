import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Feistel2 {
    public static int rounds = 3;

    public static void main(String[] args) {
        System.out.println("Enter message to encrypt:");
        Scanner sc = new Scanner(System.in);

        String source = sc.nextLine();
        FeistData data = new FeistData();
        data = encrypt(source);
        System.out.println("Ecnrypted as hex: " + arrToHex(data.cypher));
        ArrayList<Long> decrypted = decrypt(data.cypher, data.key);
        System.out.println("Decrypted as hex: " + arrToHex(decrypted));
    }

    /*
    * encryption and decryption works good,
    * problem is in the logic itself.
    * */
    public static FeistData encrypt(String source) {
        /*
        better calibrate amount of the rounds and size of each key element. If they are too big, we get long type overflow.
        For now it is small, but can be made larger if the appropriate boundaries will be found.
         */
        Integer key[] = null;
        ArrayList<Long> intArr = new ArrayList<>();
        byte[] tmp = source.getBytes(Charset.forName("UTF-8"));
        for (byte b : tmp) {
            intArr.add((long) b);
        }
        intArr.trimToSize();
        if (intArr.size() % 64 != 0) {
            int extraBytes = 64 - (intArr.size() % 64);
            SecureRandom rnd = new SecureRandom();
            for (int i = 0; i < extraBytes; i++) {
                intArr.add((long)rnd.nextInt(100));
            }
            intArr.trimToSize();
        }
        System.out.println("Size of intArr is: " + intArr.size());

        Long[] left = new Long[intArr.size() / 2];
        Long[] right = new Long[intArr.size() / 2];

        System.out.println("Source as hex bytes: " + arrToHex(intArr) + "\n"
                + "source as binary: " + arrToBinary(intArr)
        );

        for (int i = 0; i < (intArr.size() / 2); i++) {
            left[i] = intArr.get(i);
        }

        int j = 0;
        for (int i = intArr.size() / 2; i < intArr.size(); i++) {
            right[j] = intArr.get(i);
            j++;
        }

        /*
        now we have left and right arrays
         */
        SecureRandom sr = new SecureRandom();
        key = new Integer[left.length];
        Long[] buf = new Long[left.length];
        for(int i = 0 ; i < left.length; i++){
            key[i] = sr.nextInt(9) + 1;
        }
        for(int i = 0; i < rounds; i++){
            for(j = 0; j < left.length; j++){
                buf[j] = left[j];
            }
            for(j = 0; j < left.length; j++) {
                left[j] = modifiedLeftShift(left[j], key[j]);
            }
            for (j = 0; j < left.length; j++) {
                left[j] = (left[j] ^ right[j]);
            }
            for (j = 0; j < right.length; j++) {
                right[j] = buf[j];
            }
        }

        for(int i = 0; i < left.length; i++){
            intArr.set(i, left[i]);
        }

        j = 0;
        for(int i = intArr.size() / 2; i < intArr.size(); i++){
            intArr.set(i, right[j]);
            j++;
        }

        FeistData data = new FeistData();
        data.cypher = intArr;
        data.key = key;
        return data;
    }

    public static ArrayList<Long> decrypt(ArrayList<Long> cypher, Integer[] key){

        Long[] left = new Long[cypher.size() / 2];
        Long[] right = new Long[cypher.size() / 2];

        for (int i = 0; i < (cypher.size() / 2); i++) {
            left[i] = cypher.get(i);
        }

        int j = 0;
        for (int i = cypher.size() / 2; i < cypher.size(); i++) {
            right[j] = cypher.get(i);
            j++;
        }
        /*
        now we have left and right arrays
         */
        ArrayList<Integer> keyList = new ArrayList<>(Arrays.asList(key));
        Collections.reverse(keyList);
        Long[] buf = new Long[left.length];

        for(int i = 0; i < rounds; i++){
            for(j = 0; j < left.length; j++){
                buf[j] = left[j];
            }
            for(j = 0; j < left.length; j++) {
                left[j] = modifiedRightShift(left[j], keyList.get(j));
            }
            for (j = 0; j < left.length; j++) {
                left[j] = (left[j] ^ right[j]);
            }
            for (j = 0; j < right.length; j++) {
                right[j] = buf[j];
            }
        }

        for(int i = 0; i < left.length; i++){
            cypher.set(i, left[i]);
        }
        j = 0;
        for(int i = cypher.size() / 2; i < cypher.size(); i++){
            cypher.set(i, right[j]);
            j++;
        }
        return cypher;
    }

    public static String arrToHex(ArrayList<Long> cypher){
        StringBuilder sb = new StringBuilder();
        for(Long c : cypher){
            sb.append(String.format("%08x ", c));
        }
        return sb.toString();
    }

    public static String arrToBinary(ArrayList<Long> cypher){
        StringBuilder sb = new StringBuilder();
        for(Long c : cypher){
            sb.append(String.format("%8s", Long.toBinaryString(c & 0xFF)).replace(' ', '0'));
            sb.append(" ");
        }
        return sb.toString();
    }

    public static Long modifiedLeftShift(Long num, int key) {
        Long res = num;
        for (int i = 0; i < key; i++) {
            res *= 2;
        }
        return res;
    }

    public static Long modifiedRightShift(Long num, int key){
        Long res = num;
        for (int i = 0; i < key; i++) {
            res /= 2;
        }
        return res;
    }

}
