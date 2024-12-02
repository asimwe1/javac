package collections;

public class Box<T,K> {

    private T width;
    private T length;
    private T depth;

    Box (T width, T length, T depth){
        this.width= width;
        this.length= width;
        this.depth= depth;
    }

    public T getWidth() {
        return width;
    }

    public void setWidth(T width) {
        this.width = width;
    }

    public T getLength() {
        return length;
    }

    public void setLength(T length) {
        this.length = length;
    }

    public T getDepth() {
        return depth;
    }

    public void setDepth(T depth) {
        this.depth = depth;
    }

//    public T volume(){
//        return this.depth * this.length * this.width;
//    }


}
