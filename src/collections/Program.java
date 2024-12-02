package collections;

import java.util.ArrayList;

public class Program {
    public static void main(String[] args){
        Arithmetic a = new Arithmetic();
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(100);
        arr.add(200);
        arr.add(300);

        System.out.println(a.addition(arr));

    }
}
