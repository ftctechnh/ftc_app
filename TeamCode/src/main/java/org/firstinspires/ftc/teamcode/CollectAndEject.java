package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class CollectAndEject
{
    public DcMotor Intake   = null;
    public DcMotor Ejection = null;

    public Servo BallStop   = null;
    public Servo BlockDrop  = null;

    //All Encoder Math subject to change based on wheel size
    static final double     COUNTS_PER_MOTOR_REV    = 560 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    //values subject to change
    public static final double BALL_CLOSED = 0;
    public static final double BLOCK_CLOSED = 0;
    public static final double BALL_OPEN = 1;
    public static final double BLOCK_OPEN = 1;

    HardwareMap hwMap           =  null;

    public CollectAndEject(){

    }

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        Intake  = hwMap.get(DcMotor.class, "left_mid");
        Ejection = hwMap.get(DcMotor.class, "right_mid");

        //Directions subject to change when motor facing is identified
        Intake.setDirection(DcMotor.Direction.REVERSE);
        Ejection.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        Intake.setPower(0);
        Ejection.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Ejection.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Ejection.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Define and initialize ALL installed servos.
        BallStop = hwMap.get(Servo.class, "ball_stopper");
        BallStop.setPosition(BALL_CLOSED);
        BlockDrop = hwMap.get(Servo.class, "block_dropper");
        BlockDrop.setPosition(BLOCK_CLOSED);
    }
    //encoder drive method
    void encoderDrive(double speed,
                      double Distance, ToCollectOrToEject CollectOrEject) {
        if (CollectOrEject == ToCollectOrToEject.COLLECT){
            int newCMTarget;

            // Determine new target position, and pass to motor controller
            newCMTarget = Intake.getCurrentPosition() + (int)(Distance * COUNTS_PER_INCH);
            Intake.setTargetPosition(newCMTarget);
            // Turn On RUN_TO_POSITION
            Intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            Intake.setPower(Math.abs(speed));
            // keep looping while we are still active, and there is time left, and both motors are running.
            while (Intake.isBusy());
            //Stop all motion;
            Intake.setPower(0);
            // Turn off RUN_TO_POSITION
            Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }else if (CollectOrEject == ToCollectOrToEject.EJECT){
            int newEMTarget;

            // Determine new target position, and pass to motor controller
            newEMTarget = Ejection.getCurrentPosition() + (int)(Distance * COUNTS_PER_INCH);
            Ejection.setTargetPosition(newEMTarget);
            // Turn On RUN_TO_POSITION
            Ejection.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            Ejection.setPower(Math.abs(speed));
            // keep looping while we are still active, and there is time left, and both motors are running.
            while (Ejection.isBusy());
            //Stop all motion;
            Ejection.setPower(0);
            // Turn off RUN_TO_POSITION
            Ejection.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    } //end of encoder drive method
}//end of class
