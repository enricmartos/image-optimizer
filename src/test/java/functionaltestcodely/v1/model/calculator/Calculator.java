package functionaltestcodely.v1.model.calculator;

public class Calculator {
    private int value;
    public void sum(int aNumber, int anotherNumber){
        value = aNumber + anotherNumber;
    }
    public void subtract(int aNumber, int anotherNumber) {
        value = aNumber - anotherNumber;
    }

    public int currentValue(){
        return value;
    }

    public void multiply(int aNumber, int anotherNumber) {
        value = aNumber * anotherNumber;
    }

    public void divide(int aNumber, int anotherNumber) {
        value = aNumber / anotherNumber;
    }
}
