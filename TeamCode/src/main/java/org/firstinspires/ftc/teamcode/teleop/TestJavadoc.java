package org.firstinspires.ftc.teamcode.teleop;

/**
 * @author Alex Migala
 * @since 8/30/17
 */
public class TestJavadoc {

    /**
     * This is test method
     */
    public void testMethod() {
        System.out.println("this is a test method");
    }

    /**
     * Tests a boolean
     * @param test the boolean to be tested
     * @return returns if param is true or not
     */
    public boolean testBool(boolean test) {
        if (test) {
            return true;
        }
        else {
            return false;
        }
    }
}
