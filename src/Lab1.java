import java.util.Arrays;
import java.util.Scanner;

public class Lab1 {

    public static int key_size = 6;
    public static int[] key = new int[]{2, 5, 3, 4, 1, 6};

    public static String transpose(String source){
        if((source.length() % key_size) != 0){
            char[] charArray = new char[key_size - (source.length() % key_size)];
            Arrays.fill(charArray,'a');
            String concat = new String(charArray);
            source += concat;
            System.out.println(source);
        }
        StringBuilder sb = new StringBuilder(source);
        for(int i = 0; i < key_size; i++){
            for(int j = i; j < source.length(); j += key_size){
                int k = j - (j % key_size);
                sb.setCharAt(j, source.charAt(k + key[i] - 1));
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Enter the message to be encrypted:");
        Scanner scanner = new Scanner(System.in);

        String source = scanner.nextLine();
        String cypher = transpose(source);
        System.out.println("Returned: " + cypher);
    }
}
