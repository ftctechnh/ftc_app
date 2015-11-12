package com.qualcomm.ftcrobotcontroller.opmodes.IntelitekSolutions;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

public class ExampleDriveWithEncoders extends OpMode {

    DcMotor rwa; // P0 port 1
    DcMotor rwb; // P0 port 2
    DcMotor liftL; // P1 port 1
    DcMotor liftR; // P1 port 2
    Servo leftComb; // P2 channel 1
    Servo rightComb; // P2 channel 2
    Servo trigL; // P2 channel 3
    Servo trigR; // P2 channel 4
    DcMotor scoopArm; // P3 port 1
    //DcMotor winch; - Winch not currently on robot will be //P3 port 2
    //Servo leftCR; // Not on robot- P4 channel 1
    //Servo rightCR; //Not on robot- P4 channel 2
    Servo wrist;
    Servo ddspivot; // P4 channel 3
    Servo ddsclaw; // P4 channel 4
    DcMotor lwa; // P5 port 1
    DcMotor lwb; // P5 port 2


    final static int ENCODER_CPR = 1440;     //Encoder Counts per Revolution
    final static double GEAR_RATIO = 2;      //Gear Ratio
    final static int WHEEL_DIAMETER = 4;     //Diameter of the wheel in inches
    final static int DISTANCE = 24;          //Distance in inches to drive

    final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
    final static double ROTATIONS = DISTANCE / CIRCUMFERENCE;
    final static double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;

    @Override
    public void init() {
        lwa = hardwareMap.dcMotor.get("leftwheelA");
        lwb = hardwareMap.dcMotor.get("leftwheelB");
        rwa = hardwareMap.dcMotor.get("rightwheelA");
        rwb = hardwareMap.dcMotor.get("rightwheelB");
        rwa.setDirection(DcMotor.Direction.REVERSE);
        rwb.setDirection(DcMotor.Direction.REVERSE);
        lwa.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rwa.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        lwb.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rwb.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        liftL = hardwareMap.dcMotor.get("liftL");
        liftR = hardwareMap.dcMotor.get("liftR");
        liftR.setDirection(DcMotor.Direction.REVERSE);

        scoopArm = hardwareMap.dcMotor.get("scoopArm");
        scoopArm.setDirection(DcMotor.Direction.REVERSE);

        wrist = hardwareMap.servo.get("wrist");

        //rightCR = hardwareMap.servo.get("rightCR");

        leftComb = hardwareMap.servo.get("leftComb");
        rightComb = hardwareMap.servo.get("rightComb");

        trigL = hardwareMap.servo.get("trigL");
        trigR = hardwareMap.servo.get("trigR");

        ddspivot = hardwareMap.servo.get("ddspivot");
        ddsclaw = hardwareMap.servo.get("ddsclaw");
    }

    @Override
    public void start() {
        lwa.setTargetPosition((int) COUNTS);
        rwa.setTargetPosition((int) COUNTS);
        lwb.setTargetPosition((int) COUNTS);
        rwb.setTargetPosition((int) COUNTS);

        lwa.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        lwb.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rwb.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rwa.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        lwa.setPower(0.5);
        rwa.setPower(0.5);
        lwb.setPower(0.5);
        rwb.setPower(0.5);
    }

    @Override
    public void loop() {
        telemetry.addData("Motor Target", COUNTS);
        telemetry.addData("Left Position", lwa.getCurrentPosition());
        telemetry.addData("Right Position", rwa.getCurrentPosition());
    }
}

