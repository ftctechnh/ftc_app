package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class CollectSystem
{
    public DcMotor intake   = null;

    //All Encoder Math subject to change based on wheel size
    static final double     COUNTS_PER_MOTOR_REV    = 560 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    HardwareMap hwMap           =  null;

    public CollectSystem(){

    }

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        intake  = hwMap.get(DcMotor.class, "Sweeper");

        //Directions subject to change when motor facing is identified
        intake.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        intake.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    //encoder drive method
    void encoderDrive(double speed,
                      double Distance, ToCollectOrToEject CollectOrEject) {
        if (CollectOrEject == ToCollectOrToEject.COLLECT){
            int newCMTarget;

            // Determine new target position, and pass to motor controller
            newCMTarget = intake.getCurrentPosition() + (int)(Distance * COUNTS_PER_INCH);
            intake.setTargetPosition(newCMTarget);
            // Turn On RUN_TO_POSITION
            intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            intake.setPower(Math.abs(speed));
            // keep looping while we are still active, and there is time left, and both motors are running.
            while (intake.isBusy());
            //Stop all motion;
            intake.setPower(0);
            // Turn off RUN_TO_POSITION
            intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }else if (CollectOrEject == ToCollectOrToEject.EJECT){
            int newEMTarget;

            // Determine new target position, and pass to motor controller
            newEMTarget = intake.getCurrentPosition() + (int)(Distance * COUNTS_PER_INCH);
            intake.setTargetPosition(newEMTarget);
            // Turn On RUN_TO_POSITION
            intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            intake.setPower(Math.abs(speed));
            // keep looping while we are still active, and there is time left, and both motors are running.
            while (intake.isBusy());
            //Stop all motion;
            intake.setPower(0);
            // Turn off RUN_TO_POSITION
            intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    } //end of encoder drive method
    //Drive Method
    public void sweeperDrive (Double speed, Double direction, ToCollectOrToEject CollectOrEject){
        encoderDrive(speed, direction, CollectOrEject);
    }
    //Control Drive
    public void controlDrive (double speed){
        //make sure to set mode to run without encoders in init of TeleOP
        intake.setPower(speed);
    }
}//end of class
