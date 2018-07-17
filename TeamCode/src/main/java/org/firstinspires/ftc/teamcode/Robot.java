package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot {

    MecanumDrivetrain drivetrain;

    private HardwareMap hwMap =  null;
    OpMode opMode;
    private DcMotor lIntake, rIntake, lift;
    private Servo hopper, gripper, jewelPivot, jewelArm;

    public void init(HardwareMap ahwMap, LinearOpMode linOp) {
        // Save reference to Hardware map
        hwMap = ahwMap;
        opMode = linOp;

        lIntake = hwMap.get(DcMotor.class, "LI");
        rIntake = hwMap.get(DcMotor.class, "RI");
        lift = hwMap.get(DcMotor.class, "lift");

        hopper = hwMap.get(Servo.class, "hopper");
        gripper = hwMap.get(Servo.class, "gripper");
        jewelPivot = hwMap.get(Servo.class, "jewelPivot");
        jewelArm = hwMap.get(Servo.class, "jewelArm");

        hopper.setPosition(0.5);
        gripper.setPosition(0.65);
        jewelPivot.setPosition(1);
        jewelArm.setPosition(0.98);
        drivetrain = new MecanumDrivetrain(this);

        drivetrain.init((ahwMap));
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, OpMode op) {
        // Save reference to Hardware map
        hwMap = ahwMap;
        opMode = op;

        lIntake = hwMap.get(DcMotor.class, "LI");
        rIntake = hwMap.get(DcMotor.class, "RI");
        lift = hwMap.get(DcMotor.class, "lift");

        hopper = hwMap.get(Servo.class, "hopper");
        gripper = hwMap.get(Servo.class, "gripper");
        jewelPivot = hwMap.get(Servo.class, "jewelPivot");
        jewelArm = hwMap.get(Servo.class, "jewelArm");

        hopper.setPosition(0.5);
        gripper.setPosition(0.65);
        jewelPivot.setPosition(1);
        jewelArm.setPosition(0.98);

        drivetrain.init((ahwMap));
    }


    // Moves the drive train using the given x, y, and rotational velocities
    public void drive(double xVelocity, double yVelocity, double wVelocity){
        drivetrain.drive(xVelocity, yVelocity, wVelocity);
    }

    public void encoderDrive(double yDist, double maxSpeed){
        drivetrain.encoderDrive(yDist, maxSpeed);
    }

    public void gyroTurn(int wDist, double maxSpeed){
        drivetrain.gyroTurn(wDist, maxSpeed);
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

    public void setGripperPosition(double position){
        gripper.setPosition(position);
    }

    public void setJewelPivotPosition(double position){
        jewelPivot.setPosition(position);
    }

    public void setJewelArmPosition(double position){
        jewelArm.setPosition(position);
    }





}
