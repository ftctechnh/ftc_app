package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by sak5222 on 10/23/18.
 */

public class ErnieHardware {
    public DcMotor leftDrive = null;
    public DcMotor rightDrive = null;

    HardwareMap hwMap = null;
    private ElapsedTime period  = new ElapsedTime();

    public ErnieHardware(){

    }

    public void init(HardwareMap ahwMap){
        hwMap = ahwMap;

        leftDrive  = hwMap.get(DcMotor.class, "left_drive");
        rightDrive = hwMap.get(DcMotor.class, "right_drive");

        leftDrive.setPower(0);
        rightDrive.setPower(0);

        leftDrive.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightDrive.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
