package org.firstinspires.ftc.teamcode.robot.peripherals.gripper;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.peripherals.PeripheralPosition;

/**
 * Created by Derek on 2/6/2018.
 */

public class SingleServoGripper implements Gripper {
    GripperClampPositions positions = new GripperClampPositions();

    double position;
    Servo servo;
    private String name;

    public SingleServoGripper(String name, Servo servo) {
        this.name = name;
        this.servo = servo;
    }

    @Override
    public Gripper setPosition(double position) {
        this.position = position;
        return this;
    }

    @Override
    public Gripper setPosition(PeripheralPosition position) {
        this.position = position.getPosition();
        return this;
    }

    @Override
    public double getPosition() {
        return 0;
    }

    @Override
    public GripperClampPositions getClampPositions() {
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
