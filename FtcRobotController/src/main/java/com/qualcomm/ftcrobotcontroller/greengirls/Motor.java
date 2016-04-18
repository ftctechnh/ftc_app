package com.greengirls;

/**
 * Created by G201956 on 9/23/2015.
 */
public class Motor {
    private int power = 0;
    public int stopMotor() {
        power = 0;
        System.out.println ("hi");
        return  0;
    }
    public void setPower(int motorPower){
        power = motorPower;
    }
    public int getPower(){
        return power;
    }
}
