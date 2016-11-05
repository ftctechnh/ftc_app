package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.unknownelementsamples.HardwarePushbot_demo;

/**
 * Created by Fidvi on 11/4/2016.
 */

@TeleOp(name="TeleOp_TT", group="Pushbot")
//@Disabled
public class TeleOp_TT extends OpMode {

//    DcMotor BackRight;
//    DcMotor BackLeft;
//    DcMotor FrontRight;
//    DcMotor FrontLeft;
//    DcMotor FlyLeft;
//    DcMotor FlyRight;

    HardwarePushbot_TT         robot   = new HardwarePushbot_TT();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: ANDY Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.6;
    static final double     PUSH_SPEED             = 0.1;


    @Override
    public void init() {

    /* Declare OpMode members. */


        robot.init(hardwareMap); // function for init drivetrain/servos **does not handle any sensors!!**

//        BackLeft = hardwareMap.dcMotor.get("BackLeft");
//        BackRight = hardwareMap.dcMotor.get("BackRight");
//        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
//        FrontRight = hardwareMap.dcMotor.get("FrontRight");
//        FlyLeft = hardwareMap.dcMotor.get("FlyLeft");
//        FlyRight = hardwareMap.dcMotor.get("FlyRight");
//        FrontRight.setDirection(DcMotor.Direction.REVERSE);
//        BackLeft.setDirection(DcMotor.Direction.REVERSE);
//        FlyRight.setDirection(DcMotor.Direction.FORWARD);
//        FlyLeft.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void init_loop() {

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */

    @Override
    public void loop() {

        handleControls();       // function to read all input controls and set globals here

        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;

        BackRight.setPower(rightY);
        FrontRight.setPower(leftY);
        BackLeft.setPower(leftY);
        FrontLeft.setPower(rightY);

        //FlyRight.setPower(rightY);
        //FlyLeft.setPower(rightY);
    }
}

    private void handleControls() { // @todo add code to read joysticks
        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        double left = 0.0;
        double right = 0.0;

        left = -gamepad1.left_stick_y;   // (note: The joystick goes negative when pushed forwards, so negate it)
        right = -gamepad1.right_stick_y;
        if (debugmode) telemetry.addData("LJoystickRaw", "%.2f", left);
        if (debugmode) telemetry.addData("RJoystickRaw", "%.2f", right);

        left = scaleMotorPower(enforceDeadZone(left));   // don't move unless far enough from zero
        right = scaleMotorPower(enforceDeadZone(right));    // because physical 'dead stick' may not be seen as zero
        if (debugmode) telemetry.addData("LMotorSpeed", "%.2f", left);
        if (debugmode) telemetry.addData("RMotorSpeed", "%.2f", right);

        leftMotorSpeed = left;
        rightMotorSpeed = right;
    }