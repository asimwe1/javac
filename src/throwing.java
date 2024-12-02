class ThrowDemo{
    static void demoproc(String message){
        try{
            if(message==null)
//                throw new IllegalAccessException("demo");
            System.out.println("Message " +message);
        } catch (NullPointerException e){
            System.out.println("Caught inside demoproc");
            throw e;
        }
    }
    public static void main(String[] args){
        try{
            String name=null;
            demoproc(name);
        }catch (NullPointerException e){
            System.out.println("Recaught: " + e);
        }
    }
}