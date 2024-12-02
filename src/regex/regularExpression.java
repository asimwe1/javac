package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regularExpression {

    public static void main(String[] arg){

        Pattern pattern1 = Pattern.compile("test[\\.]");
        Matcher matcher1 = pattern1.matcher("test.");
        if (matcher1.find()){
            while (matcher1.find()) {
                String group = matcher1.group();
                int start = matcher1.start();
                int end = matcher1.end();
                System.out.println("Find: " + matcher1.find() );
                System.out.println("find " + matcher1.find() + "start is " + start + "end is " + end + " find: ");
            }
        } else{
            System.out.println("find: " + matcher1.find() );
        }
    }
}
