import java.util.InputMismatchException;
import java.util.Scanner;

public class blah {
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        try {


            System.out.println("enter: ");
            double num1 = scanner.nextInt();
            scanner.nextLine();
            System.out.println("enter: ");
            double num2 = scanner.nextInt();
            scanner.nextLine();

            double result = num1 / num2 ;

            System.out.println(result);
        }

        catch (InputMismatchException e) {
            System.out.println("your input was invalid: "+ e);
        }

        catch(ArithmeticException e){
            System.out.println(e.getMessage());
        }
        catch(RuntimeException e){
            e.printStackTrace();
        }
        finally {
             scanner.close();
        }
    }
}
