package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class DriveBaseHardwareMap {
    /* Public OpMode members. */
    public DcMotor  top_left   = null;
    public DcMotor  bot_left   = null;
    public DcMotor  top_right  = null;
    public DcMotor  bot_right  = null;
   // public DcMotor  sweeper = null;
    //public DcMotor  lifter  = null;
    public CRServo    dropper = null;
    public Servo    marker  = null;

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
        //sweeper = hwMap.get(DcMotor.class, "sweeper");
        //lifter = hwMap.get(DcMotor.class, "lifter");
        //sweeper.setDirection(DcMotor.Direction.FORWARD);
        //lifter.setDirection(DcMotor.Direction.FORWARD);
        top_left.setDirection(DcMotor.Direction.FORWARD);
        bot_left.setDirection(DcMotor.Direction.FORWARD);
        top_right.setDirection(DcMotor.Direction.FORWARD);
        bot_right.setDirection(DcMotor.Direction.FORWARD);// Set to REVERSE if using AndyMark motors

        // Set all motors to zero power
        top_left.setPower(0);
        bot_left.setPower(0);
        top_right.setPower(0);
        bot_right.setPower(0);
        //sweeper.setPower(0);
        //lifter.setPower(0);
        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        top_left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bot_left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        top_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bot_right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //lifter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //sweeper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        dropper  = hwMap.get(CRServo.class, "dropper");
        marker = hwMap.get(Servo.class, "marker");
        //intake_right = hwMap.get(Servo.class, "intake_right")
        marker.setPosition(0.5);
        //dropper.setDirection(CRServo.Direction.FORWARD);
        dropper.setPower(0.0);
        //intake_right.setPosition(0);
    }
}