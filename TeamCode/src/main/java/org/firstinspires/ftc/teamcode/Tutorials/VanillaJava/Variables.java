package org.firstinspires.ftc.teamcode.Tutorials.VanillaJava;


/**
 * Class used to demonstrate variables
 *
 * Run this in jGrasp
 */
@SuppressWarnings("all")
public class Variables
{
    public static void main(String[] args)
    {
        int someInteger;                    // An integer whose name is "someInteger"

        int integerA , integerB;            // I can also declare like this

        someInteger = 0;                    // someInteger has a value of "0" now

        System.out.println("The value of someInteger is: " + someInteger);

        someInteger = someInteger + 5;      // someInteger = 5
        
        System.out.println("The new value of someInteger is: " + someInteger);
        
        float someFloat = 1.2f;             // A floating-point number (decimal) whose value is 1.2
                                            // You have to use "f" to denote "float"
        
        someFloat = 2 / 3;                  // What does this give me?
        
        System.out.println("2 / 3 is: " + someFloat);
        
        someFloat = (float)2 / 3;           // This is called Type Casting- you are casting an int to a float
        
        System.out.println("2 / 3 actually is: " + someFloat);
        
        // You can also type cast like this:
        someFloat = 2.0f / 3;
        
        // Or even this:
        someFloat = 2 / 3.0f;
        
        // Maybe both?
        someFloat = 2.0f / 3.0f;
        
        
        // Some other data types:
        
        char someCharacter = 'a';         // Stores a single character, denoted by single quotes     

        short someShort = 2;              // Short, stores small numbers, nobody uses these
        
        double someDouble = 2.3;          // Double precision, stores bigger decimals. Unlike float, you don't need to add extra characters
        
        long someLong = 5;                // Long integer, stores really big numbers
        
        boolean someBoolean = true;       // Boolean (often "bool"), stores either true or false
        
        // Capital String
        String someString = "Hello";      // String (often "str"), stores a string of characters
        
        // I can add two strings together
        
        someString = someString + " world";
        
        System.out.println(someString);
    }
}
