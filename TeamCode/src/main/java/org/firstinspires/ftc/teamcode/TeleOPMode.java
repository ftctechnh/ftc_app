package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TeleOPMode")
public class TeleOPMode extends ComponentsInit {
    public void init(){
        super.servoInit();
        super.motorInit();
        hDrive=new HolonomicDrive(super.getMotors());
        tDrive=new TankDrive(super.getMotors());
        servoControl=new ServoControl(super.getServos());
    }
    public void loop(){
        //motor
        //hDrive.setValues(super.driveX(),super.driveLY(), super.driveR());
        tDrive.setValues(super.driveRY(),super.driveLY());
        //servo
        if(gamepad1.left_bumper)
            servoControl.open();
        else if(gamepad1.left_trigger>=0.8f)
            servoControl.close();
    }
    HolonomicDrive hDrive;
    TankDrive tDrive;
    ServoControl servoControl;
}
