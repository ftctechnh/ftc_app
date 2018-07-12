package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot {

    MecanumDrivetrain drivetrain = new MecanumDrivetrain(this);

    private HardwareMap hwMap =  null;
    OpMode opMode;
    private DcMotor lIntake, rIntake, lift;
    private Servo hopper;

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, OpMode op) {
        // Save reference to Hardware map
        hwMap = ahwMap;
        opMode = op;

        lIntake = hwMap.get(DcMotor.class, "LI");
        rIntake = hwMap.get(DcMotor.class, "RI");
        lift = hwMap.get(DcMotor.class, "lift");
        hopper = hwMap.get(Servo.class, "hopper");


        drivetrain.init((ahwMap));
    }

    // Moves the drive train using the given x, y, and rotational velocities
    public void drive(double xVelocity, double yVelocity, double wVelocity){
        drivetrain.drive(xVelocity, yVelocity, wVelocity);
    }

    public void encoderDrive(double yDist, double maxSpeed){
        drivetrain.encoderDrive(0, yDist, 0, maxSpeed);
    }

    public void runIntake(double power){
        lIntake.setPower(-power);
        rIntake.setPower(power);
    }

    public void runLift(double power){
        lift.setPower(power);
    }

    public void setHopperPosition(double position){
        hopper.setPosition(position);
    }

}
