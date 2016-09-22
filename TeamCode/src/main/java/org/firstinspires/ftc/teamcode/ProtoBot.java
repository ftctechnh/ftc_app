package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by jonathonmangan on 9/21/16.
 */
@Autonomous(name="Protobot Tank", group="Protobot")
public class ProtoBot extends OpMode {

    private DcMotor LT;
    private DcMotor LB;
    private DcMotor RT;
    private DcMotor RB;

    public void init(){
        LT = hardwareMap.dcMotor.get("l_front");
        LB = hardwareMap.dcMotor.get("l_back");
        RT = hardwareMap.dcMotor.get("r_front");
        RB = hardwareMap.dcMotor.get("r_back");

        LT.setDirection(DcMotor.Direction.REVERSE);
        RT.setDirection(DcMotor.Direction.REVERSE);
    }
    public void loop(){


        LT.setPower(gamepad1.left_stick_y);
        LB.setPower(gamepad1.left_stick_y);

        RT.setPower(gamepad1.right_stick_y);
        RB.setPower(gamepad1.right_stick_y);

        if(Math.abs(gamepad1.left_stick_y) < .2 || Math.abs(gamepad1.right_stick_y) < .2){
            LT.setPower(0);
            RT.setPower(0);
            LB.setPower(0);
            RB.setPower(0);
        }

        telemetry.addData("Left Stick Y", gamepad1.left_stick_y);
        telemetry.addData("Right Stick Y", gamepad1.right_stick_y);
    }
}
