package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class DriveBaseHardwareMap {
    /* Public OpMode members. */
    public DcMotor  top_left     = null;
    public DcMotor  bot_left   = null;
    public DcMotor  top_right  = null;
    public DcMotor  bot_right    = null;
    public Servo    intake_left    = null;
    //public Servo    intake_right   = null;

    //public static final double MID_SERVO       =  0.5 ;
    //public static final double ARM_UP_POWER    =  0.45 ;
    //public static final double ARM_DOWN_POWER  = -0.45 ;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public DriveBaseHardwareMap(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        top_left  = hwMap.get(DcMotor.class, "top_left");
        bot_left  = hwMap.get(DcMotor.class, "bot_left");
        top_right = hwMap.get(DcMotor.class, "top_right");
        bot_right = hwMap.get(DcMotor.class, "bot_right");
        top_left.setDirection(DcMotor.Direction.FORWARD);
        bot_left.setDirection(DcMotor.Direction.FORWARD);
        top_right.setDirection(DcMotor.Direction.FORWARD);
        bot_right.setDirection(DcMotor.Direction.FORWARD);// Set to REVERSE if using AndyMark motors

        // Set all motors to zero power
        top_left.setPower(0);
        bot_left.setPower(0);
        top_right.setPower(0);
        bot_right.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        top_left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bot_left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        top_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bot_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //leftArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        intake_left  = hwMap.get(Servo.class, "intake_left");
        //intake_right = hwMap.get(Servo.class, "intake_right");
        intake_left.setPosition(0.70);
        //intake_right.setPosition(0);
    }
}