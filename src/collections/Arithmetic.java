package collections;

import java.util.ArrayList;

public class Arithmetic {
    public Object addition (ArrayList<?>numbers) {
        Object sum = null;
        for ( Object o : numbers ){
            sum += o.toString();
        };

        return sum;
    }
}
