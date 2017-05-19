package org.firstinspires.ftc.teamcode.Libs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HardWareMaps.Hardware_Omnidirectional_Platform;

/**
 * 全向平台探索实验用
 */
public abstract class Core_Omnidirectional_Platform_Exp extends LinearOpMode {
    public Hardware_Omnidirectional_Platform robot = new Hardware_Omnidirectional_Platform();

    private double ForwardPower = 0.25;
    private double ReversePower = - ForwardPower;

    protected void keyToDirection(){
        if(gamepad1.dpad_up){
            setBasePower(ForwardPower,ForwardPower,ForwardPower,ForwardPower);
        }else if(gamepad1.dpad_down){
            setBasePower(ReversePower,ReversePower,ReversePower,ReversePower);
        }else if(gamepad1.dpad_left){
            setBasePower(ForwardPower,ReversePower,ReversePower,ForwardPower);
        }else if(gamepad1.dpad_right){
            setBasePower(ReversePower,ForwardPower,ForwardPower,ReversePower);
        }else if(gamepad1.x){
            setBasePower(ForwardPower,ReversePower,ForwardPower,ReversePower);
        }else if(gamepad1.b){
            setBasePower(ReversePower,ForwardPower,ReversePower,ForwardPower);
        }
    }

    protected boolean isAnyKedDown(){
        return gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_right || gamepad1.x || gamepad1.b;
    }

    protected void setBasePower(double BLPower,double BRPower,double FLPower,double FRPower){
        robot.Base_BL.setPower(BLPower);
        robot.Base_BR.setPower(BRPower);
        robot.Base_FL.setPower(FLPower);
        robot.Base_FR.setPower(FRPower);
    }
}
