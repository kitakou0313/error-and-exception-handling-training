package org.example;

public class Caller {
    void doSomethingWithMethodThrowingException(){
        Called called = new Called();

        try {
            called.doSomethingWithException();
        } catch (SimpleException e) {
            System.err.println("Exception was thrown: " + e.getMessage());
        }
    }
}
