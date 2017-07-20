package org.firstinspires.ftc.teamcode.PwrPuffGirls_Official;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by mabel on 7/16/2017.
 */

@Autonomous(name = "powerpuffgirlsdrive")
public class powerpuff_baisic extends LinearOpMode{

    @Override
    public void runOpMode(){

        DcMotor leftbackMotor;
        DcMotor rightbackMotor;
        DcMotor leftfrontmotor;
        DcMotor rightfrontmotor;

        rightbackMotor = hardwareMap.dcMotor.get("rightbackMotor");
        leftbackMotor = hardwareMap.dcMotor.get("leftbackmotor");
        rightfrontmotor= hardwareMap.dcMotor.get("rightfrontmotor");
        leftfrontmotor= hardwareMap.dcMotor.get("leftfrontmotor");

        rightbackMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightfrontmotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        leftfrontmotor.setPower(0.2);
        leftbackMotor.setPower(0.2);
        rightbackMotor.setPower(0.2);
        rightfrontmotor.setPower(0.2);
        sleep(10000);
        leftbackMotor.setPower(0);
    }
}
