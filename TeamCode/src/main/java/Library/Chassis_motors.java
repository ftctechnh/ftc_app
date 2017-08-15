package Library;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import  com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Uz as a starter
 */

public class Chassis_motors
{
    private static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    private static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    private static final double     WHEEL_DIAMETER_CM       = 9.15 ;     // For figuring circumference
    private static final double     COUNTS_PER_CM           = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_CM * Math.PI );


    private static final double     WHEELS_SPACING_CM       = 40.8;     // spacing between wheels for turns

    private DcMotor motorLeft;
    private DcMotor motorRight;
    private ElapsedTime chassis_runtime = new ElapsedTime();

    public Chassis_motors(HardwareMap hardwareMap){    // constructor to create object
        motorLeft = hardwareMap.dcMotor.get("LeftDrive");
        motorRight = hardwareMap.dcMotor.get("RightDrive");

    }

    // set direction forward
    public void set_Direction_Forward () {
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorLeft.setDirection(DcMotor.Direction.FORWARD);
    }

    // set direction reverse
    public void set_Direction_Reverse () {
        motorLeft.setDirection(DcMotor.Direction.FORWARD);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
    }

    // To move the chassis forward, power -1 to 1
    public void move_Forward (double power) {
        set_Direction_Forward();
        run_Motors_no_encoder(power,power);
    }

    // To move the chassis reverse, power -1 to 1
    public void move_Reverse (double power) {
        set_Direction_Reverse();
        run_Motors_no_encoder(power,power);
    }

    public void run_Motors_no_encoder(double leftPower, double rightPower) {
        motorLeft.setPower(leftPower);
        motorRight.setPower(rightPower);
    }

    // Run motors with encoders, specify the power, distance in CM (centimeter)
    public void run_Motors_encoder_CM(double power, double leftDistance, double rightDistance, double timeout) {
        int newLeftTarget  = motorLeft.getCurrentPosition()  + (int)(leftDistance * COUNTS_PER_CM);
        int newRightTarget = motorRight.getCurrentPosition() + (int)(rightDistance * COUNTS_PER_CM);
        motorLeft.setTargetPosition(newLeftTarget);
        motorRight.setTargetPosition(newRightTarget);

        // Turn On RUN_TO_POSITION
        motorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        chassis_runtime.reset();

        // set the power to start the motors
        motorLeft.setPower(power);
        motorRight.setPower(power);

        // keep looping while we are still active, and there is time left, and both motors are running.
        while ((chassis_runtime.seconds() < timeout) &&
                (is_motors_busy())) {

        }

        // Stop all motion;
        motorLeft.setPower(0);
        motorRight.setPower(0);

        // Turn off RUN_TO_POSITION
        motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private boolean is_motors_busy(){
        return motorLeft.isBusy() || motorRight.isBusy();
    }

}
