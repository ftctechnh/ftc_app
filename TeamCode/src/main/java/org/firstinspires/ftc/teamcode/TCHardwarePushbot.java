/*
 Created by Nightmaze on 10/17/17.
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//Defines motors and servos
public class TCHardwarePushbot {
    public DcMotor rDrive;
    public DcMotor lDrive;
    public DcMotor cDrive;
    public DcMotor fLift;
    public DcMotor claw1;
    public DcMotor claw2;
    public Servo fs1;
    public Servo fs2;
    //Creates the hardware map
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    //Maps the hardware to ahwMap
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;
        // Define and Initialize Motors
        rDrive = hwMap.dcMotor.get("rDrive");
        lDrive = hwMap.dcMotor.get("lDrive");
        cDrive = hwMap.dcMotor.get("cDrive");
        fLift = hwMap.dcMotor.get("fLift");
        claw1 = hwMap.dcMotor.get("claw1");
        claw2 = hwMap.dcMotor.get("claw2");
        lDrive.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rDrive.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Set all motors to zero power to get ready for start
        rDrive.setPower(0);
        lDrive.setPower(0);
        cDrive.setPower(0);
        fLift.setPower(0);
        claw1.setPower(0);
        claw2.setPower(0);
    }


}
