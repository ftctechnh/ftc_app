package org.firstinspires.ftc.teamcode.robot.peripherals;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Derek on 2/6/2018.
 */

public class SingleServoGripper implements Claw {
    ClawPositions positions = new ClawPositions();

    double position;
    Servo servo;
    private String name;

    public SingleServoGripper(String name, Servo servo) {
        this.name = name;
        this.servo = servo;
    }

    @Override
    public Claw setPosition(double position) {
        this.position = position;
        return this;
    }

/*    @Override
    public Claw setPosition(ClawPositions.Position position) {
        this.position = position.getPosition();
        return this;
    }*/



    @Override
    public double getPosition() {
        return 0;
    }

    @Override
    public ClawPositions getClampPositions() {
        return positions;
    }

    @Override
    public void update() {
        servo.setPosition(position);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean test() {
        return servo != null;
    }
}
