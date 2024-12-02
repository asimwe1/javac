import javax.swing.text.Style;

public class Hello {

    public void printMe(){

        System.out.println("Hello world");
    }

    public static void main(String[] args){

        try{
            int a=50;
            int b=10;
            int c=a/b;
            int []numbers = {1,2,20};

            System.out.println("number 5 is: "+ numbers[5]);
            System.out.println(" the result is: "+ c);
        }
        catch (ArithmeticException exc) {
            System.out.println("Arithmetic error: "+ exc.getMessage());
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid index"+ e.getMessage());
        }
        catch (RuntimeException e){
            System.out.println("Runtime Exception: " + e.getMessage());
        }
        finally {
            System.out.println("Another Statement");
        }
    }
}
