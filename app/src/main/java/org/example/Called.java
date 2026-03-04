package org.example;

public class Called {
    public void doSomethingWithException() throws SimpleException{
        throw new SimpleException("Simple exception was thrown");
    }
}
