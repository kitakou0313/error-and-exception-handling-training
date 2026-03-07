package org.example;

public class Caller {
    void doSomethingWithMethodThrowingException(){
        Middle middle = new Middle();
        middle.execCalledMethod();
    }
}
