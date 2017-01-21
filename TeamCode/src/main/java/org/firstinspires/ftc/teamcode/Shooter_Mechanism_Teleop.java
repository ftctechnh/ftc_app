/*
Copyright (c) 2016 Robert Atkinson

*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name="Shooter_Mechanism_Teleop", group="PushBot")  // @Autonomous(...) is the other common choice
// @Disabled
public class Shooter_Mechanism_Teleop extends OpMode {
    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    HardwarePushbot_TT robot = new HardwarePushbot_TT();   // Use a Pushbot's hardware

    static final double COUNTS_PER_MOTOR_REV = 1120;    // eg: ANDY Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
//    static final double     DRIVE_SPEED             = 0.6;
//    static final double     TURN_SPEED              = 0.6;
//    static final double     PUSH_SPEED             = 0.1;

    private double flyLeft = 0.0;
    private double flyRight = 0.0;

    private double minimumDeadZone = 0.05;             // adjust this value to increase or descrease the deadzone
    private double maxMotorSpeed = 0.95;             // adjust this value to set the maximum motor speed, depends on motor type
    private double flyMotorSpeed = 0.6 ;
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        /* eg: Initialize the hardware variables.          */
        // @todo add all additional initalization for hardware here
        robot.init(hardwareMap); // function for init drivetrain/servos **does not handle any sensors!!**

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();

        // @todo add all one time 'get ready to run' here

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        telemetry.addData("Status", "Running: " + runtime.toString());

        handleControls();    // function to read all input controls and set globals here
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

        // @todo add function to clean up status of robot

    }


    private void handleControls() { // @todo add code to read joysticks
        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        double left = 1.0;
        double right = 1.0;
        double armUp = 0.0;

        // ClawState = 0 for Home, 1 for Open, 2 for holding the ball
        int iPrevStateClaw = 0;
        int iCurrStateClaw = 0;

        boolean bPrevState = false;
        boolean bCurrState = false;

        flyLeft = gamepad1.left_trigger;   // (note: The joystick goes negative when pushed forwards, so negate it)
        flyRight = gamepad1.right_trigger;
//            robot.flyLeft.setDirection(DcMotor.Direction.FORWARD);
//            robot.flyRight.setDirection(DcMotor.Direction.REVERSE);
        bCurrState = gamepad1.x;

        // check for button state transitions.
        if ((bCurrState == true) && (bCurrState != bPrevState)) {

            robot.armMotor.setDirection(DcMotor.Direction.FORWARD);
        } else {
            robot.armMotor.setDirection(DcMotor.Direction.REVERSE);
        }

        // update previous state variable.
        bPrevState = bCurrState;

        if (flyRight != 0.0 && flyLeft!=0.0 ) {
            robot.flyLeft.setPower(flyMotorSpeed);
            robot.flyRight.setPower(flyMotorSpeed);
        }

        left = enforceDeadZone(left);   // don't move unless far enought from zero
        right = enforceDeadZone(right);    // because physical 'dead stick' mayn not be seen as zero
        armUp = enforceDeadZone(armUp);
//        leftServo = enforceDeadZone(leftServo);
//        rightServo = enforceDeadZone(rightServo);

        flyRight = right;
        flyLeft = left;
    }



    private double enforceDeadZone(double joystickPos) {

        //  physical 'dead stick' mayn not be seen as zero, so need to eliminate anything below a threshhold

        double minimumDeadZone = 0.05;      // adjust this value to increase or descrease the deadzone

        if (joystickPos >= 0) {      // handle positive and negative separately

            if (joystickPos < minimumDeadZone) {
                joystickPos = 0;        // less than minimum from zero, so set to zero
            }

        }   else    {               // not positive, so must be negative

            if (joystickPos > -1 * minimumDeadZone) {
                joystickPos = 0;        // more than minimum from zero, so set to zero
            }
        }

        return (joystickPos);
    }

    private double scaleMotorPower(double motorpower) {

        //  need to compensate for deadzone
        // and use an acceleration curve

        if (motorpower >= 0) {      // handle positive and negative separately

            motorpower -= minimumDeadZone;  // remove deadzone offzet, otherwise can't represent a power less than deadzone
            motorpower = motorpower * motorpower;    // square motorpower to generate the acceleration curve

        }   else    {               // not positive, so must be negative

            motorpower += minimumDeadZone;              // remove deadzone offzet, otherwise can't represent a power less than deadzone
            motorpower = motorpower * motorpower;       // square motorpower to generate the acceleration curve
            motorpower = -1 * motorpower;               // put back the sign lostg when squaring the value
        }

        return (motorpower);
    }
}

