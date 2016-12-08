package org.firstinspires.ftc.team9374.team9374;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/*
 * Created by darwin on 10/29/16.
 */
@TeleOp(name = "Exponent drive", group = "null")
//@Disabled0
public class NineK_MainBotT_Potential extends OpMode {
    //Drivin
    DcMotor left_f;
    DcMotor right_f;
    DcMotor left_b;
    DcMotor right_b;
    //Shooter
    DcMotor shooter_l;
    DcMotor shooter_r;
    //Speeds
    /*
    boolean Sspeed;
    boolean Nspeed = true;
    boolean Fspeed;
    */
    final double Exponent = 1.5;

    Servo elevator;

    public ElapsedTime runTime = new ElapsedTime();

    double lStickY;
    double lStickX;
    double rStickY;

    public void init()  {
        //Driving motors
        left_f = hardwareMap.dcMotor.get("Eng1-left");
        right_f = hardwareMap.dcMotor.get("Eng1-right");
        left_b = hardwareMap.dcMotor.get("Eng2-left");
        right_b = hardwareMap.dcMotor.get("Eng2-right");
        //Shooter motors
        shooter_r = hardwareMap.dcMotor.get("Eng3-left");
        shooter_l = hardwareMap.dcMotor.get("Eng3-right");

        elevator = hardwareMap.servo.get("Ser1-center");

        //This might not be true for all motors on the right side
        right_b.setDirection(DcMotorSimple.Direction.REVERSE);
        right_f.setDirection(DcMotorSimple.Direction.REVERSE);

        //shooter_r.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter_l.setDirection(DcMotorSimple.Direction.REVERSE);

        runTime.reset();

    }

    @Override
    public void loop() {
        //All Driving code//


        float leftDC = gamepad1.left_stick_y;
        float rightDC =  gamepad1.right_stick_y;

        double rTrigger = gamepad2.right_trigger;
        double lTrigger = gamepad2.left_trigger;

        boolean lBumper = gamepad2.left_bumper;
        boolean rBumper = gamepad2.right_bumper;

        left_f.setPower(Range.clip(Math.pow(rightDC,Exponent),-1,1));
        left_b.setPower(Range.clip(Math.pow(rightDC,Exponent),-1,1));
        right_b.setPower(Range.clip(Math.pow(leftDC,Exponent),-1,1));
        right_f.setPower(Range.clip(Math.pow(leftDC,Exponent),-1,1));

        //End of Driving code//
        //All Servo Code//





        telemetry.addData("Right Bumper",rBumper);
        telemetry.addData("Left Bumper", lBumper);
        telemetry.addData("Servo Position", elevator.getPosition());
        telemetry.addData("Total Runtime:", runTime);



    }
}
