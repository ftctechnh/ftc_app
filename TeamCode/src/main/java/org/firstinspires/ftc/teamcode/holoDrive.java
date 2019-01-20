//Razzle Dazzle of Fantazzmagazzles Code


package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math.*;



@TeleOp(name = "holoDrive", group = "Tank")
public class holoDrive extends OpMode {

    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    DcMotor m4;
    DcMotor plow;
    DcMotor lift;
    DcMotor armIntake;


    @Override
    public void init() {
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        m4 = hardwareMap.dcMotor.get("m4");
        plow = hardwareMap.dcMotor.get("plow");
        lift = hardwareMap.dcMotor.get("lift");
        armIntake = hardwareMap.dcMotor.get("arm_Intake");
    }

    @Override
    public void loop() {
        double yPower = gamepad1.left_stick_y;  //power to spin holonomic
        double xPower = gamepad1.left_stick_x;  //power to spin holonomic
        double spinPower = -gamepad1.right_stick_x; //power to spin holonomic
        double liftPower = gamepad2.right_stick_y; //power for lift

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------

        if(Math.abs(spinPower)>0.1) {    //spin holonomic
            m1.setPower(spinPower);
            m2.setPower(spinPower);
            m3.setPower(spinPower);
            m4.setPower(spinPower);
        }
        else if(Math.abs(yPower)>0.1 && (xPower<0.2 && xPower>-0.2)){ //drive holonomic
            m1.setPower(yPower);
            m2.setPower(-yPower);
            m3.setPower(-yPower);
            m4.setPower(yPower);
        }
        else if(Math.abs(xPower)>0.1 && (yPower<0.2 && yPower>-0.2)){
            if(xPower>0.1){
                m1.setPower(-xPower);
                m2.setPower(-xPower);
                m3.setPower(xPower);
                m4.setPower(xPower);
            }
            else if(xPower<0){
                m1.setPower(-xPower);
                m2.setPower(-xPower);
                m3.setPower(xPower);
                m4.setPower(xPower);
            }

        }
        else  if(xPower>0.1 && yPower>0.1){
            m1.setPower(0);
            m2.setPower(yPower);
            m3.setPower(0);
            m4.setPower(-yPower);
        }
        else  if(xPower<0 && yPower>0.1){
            m1.setPower(-yPower);
            m2.setPower(0);
            m3.setPower(yPower);
            m4.setPower(0);
        }
        else  if(xPower<0 && yPower<0){
            m1.setPower(0);
            m2.setPower(yPower);
            m3.setPower(0);
            m4.setPower(-yPower);

        }
        else  if(xPower>0.1 && yPower<0){
            m1.setPower(-yPower);
            m2.setPower(0);
            m3.setPower(yPower);
            m4.setPower(0);
        }
        else {
            m1.setPower(0);
            m2.setPower(0);
            m3.setPower(0);
            m4.setPower(0);
        }

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------

        lift.setPower(liftPower/2);

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------

        armIntake.setPower((-gamepad2.left_stick_y/2));

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------

        telemetry.addData("xPower", "%.2f",  gamepad1.left_stick_x);
        telemetry.addData("yPower", "%.2f",  gamepad1.left_stick_y);
        telemetry.addData("liftPower", "%.2f",  gamepad2.right_stick_y/2);
        telemetry.addData("armPower", "%.2f",  -gamepad2.left_stick_y/2);

    }
}

