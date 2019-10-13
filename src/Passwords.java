import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Passwords {
    public static void main(String[] args) {
        ArrayList<String> passwords = new ArrayList<>();
        passwords.add("pass1");
        passwords.add("pass2");
        passwords.add("pass3");
        passwords.add("pass4");
        passwords.add("pass5");


        while(true){
            System.out.println("Enter your password: ");
            Scanner sc = new Scanner(System.in);

            String tmp = sc.nextLine();
            if(passwords.contains(tmp)){
                passwords.remove(passwords.indexOf(tmp));
                System.out.println("Password is good, deleting from password pool");
                continue;
            }else{
                System.out.println("Password is wrong, try again");
            }
        }
    }
}
