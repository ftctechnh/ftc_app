package org.firstinspires.ftc.teamcode.framework;

public abstract class AbstractRobot {

    public AbstractRobot(){

    }

    public abstract void stop();

    public void delay(int time){
        AbstractOpMode.delay(time);
    }
}
