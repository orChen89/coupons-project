package com.or;

public class Program {

    public static void main(String[] args) {

        //Creating tester object
        Tester test = new Tester();

        //Running the system with catching all exceptions
        try {
            test.testAll();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
