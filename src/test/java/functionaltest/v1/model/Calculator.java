package functionaltest.v1.model;

public class Calculator {
    private int value;
    public void sum(int aNumber, int anotherNumber){
        value = aNumber + anotherNumber;
    }

    public int currentValue(){
        return value;
//        return 5;
    }
}
