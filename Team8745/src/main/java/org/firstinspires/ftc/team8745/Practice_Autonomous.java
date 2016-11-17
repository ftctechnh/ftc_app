package org.firstinspires.ftc.team8745;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
/**
 * Created by some guy "named" Nintendo8 on 10/30/2016.
 */
@Disabled
@Autonomous(name="Noah's First Autonomous")

public class Practice_Autonomous extends OpMode {
    DcMotor leftFRONT;
    DcMotor rightFRONT;
    DcMotor leftBACK;
    DcMotor rightBACK;
    DcMotor shooterLeft;
    DcMotor shooterRight;
    int wheelDiameter = 4;
    // 4 Inches
    public void init() {
        //Front Motors
        leftFRONT = hardwareMap.dcMotor.get("motor-left");
        rightFRONT = hardwareMap.dcMotor.get("motor-right");

        //Back Motors
        leftBACK = hardwareMap.dcMotor.get("motor-leftBACK");
        rightBACK = hardwareMap.dcMotor.get("motor-rightBACK");

        rightFRONT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBACK.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFRONT.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBACK.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //Encoder
        rightFRONT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBACK.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFRONT.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBACK.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFRONT.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBACK.setDirection(DcMotorSimple.Direction.REVERSE);



    }

    @Override
    public void loop() {
    if (leftFRONT.getMode()!=DcMotor.RunMode.RUN_TO_POSITION){

        rightFRONT.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBACK.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFRONT.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBACK.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rightFRONT.setTargetPosition(1000);
        rightBACK.setTargetPosition(1000);
        leftFRONT.setTargetPosition(1000);
        leftBACK.setTargetPosition(1000);

        rightFRONT.setPower(1);
        rightBACK.setPower(1);
        leftFRONT.setPower(1);
        leftBACK.setPower(1);

    }

telemetry.addData("ticks",rightFRONT.getCurrentPosition());
        telemetry.addData("target",rightFRONT.getTargetPosition());
        telemetry.update();

    }


}
