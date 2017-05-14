package org.firstinspires.ftc.teamcode.Libs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HardWareMaps.Hardware_Omnidirectional_Platform;

public abstract class Core_Omnidirectional_Platform_Exp extends LinearOpMode {
    public Hardware_Omnidirectional_Platform robot = new Hardware_Omnidirectional_Platform();

    public void keyToDirection(){
        if(gamepad1.dpad_up){
            setBasePower(1.00,1.00,1.00,1.00);
        }else if(gamepad1.dpad_down){
            setBasePower(-1.00,-1.00,-1.00,-1.00);
        }else if(gamepad1.dpad_left){
            setBasePower(1.00,1.00,-1.00,1.00);
        }else if(gamepad1.dpad_right){
            setBasePower(1.00,1.00,1.00,-1.00);
        }
    }

    private void setBasePower(double BLPower,double BRPower,double FLPower,double FRPower){
        robot.Base_BL.setPower(BLPower);
        robot.Base_BR.setPower(BRPower);
        robot.Base_FL.setPower(FLPower);
        robot.Base_FR.setPower(FRPower);
    }
}
