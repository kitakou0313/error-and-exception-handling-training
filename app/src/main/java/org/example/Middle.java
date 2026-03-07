package org.example;

public class Middle {
    void execCalledMethod() {
        Called called = new Called();

        try {
            called.doSomethingWithException();
        } catch (SimpleException e) {
            throw new RuntimeException("Exception was thrown: " + e.getMessage());
        }
    }
}
