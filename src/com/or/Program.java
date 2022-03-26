package com.or;

public class Program {

    public static void main(String[] args) {

        //Creating the tester object
        TestingSystem test = new TestingSystem();

        //Running the system
        try {
            test.testAll();
          //Catching all exceptions
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}

