package org.firstinspires.ftc.team9374.team9374;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.team9374.team9374.Hardware9374;

/*
 * Created by darwin on 10/29/16.
 */
@TeleOp(name = "9KT", group = "null")
//@Disabled0
public class NineK_MainBotT extends OpMode {
    //Drivin
    Hardware9374 robot = new Hardware9374();
    public ElapsedTime runTime = new ElapsedTime();

    public void init()  {
        //Driving motors
    robot.init(hardwareMap);

    }

    @Override
    public void loop() {
        //All Driving code//
        //Driver

        double lStickY = gamepad1.left_stick_y;
        double lStickX = gamepad1.left_stick_x;
        double rStickY = gamepad1.right_stick_y;

        //Operator
        double rTrigger = gamepad2.right_trigger;
        double lTrigger = gamepad2.left_trigger;
        boolean lBumper = gamepad2.left_bumper;
        boolean rBumper = gamepad2.right_bumper;

        double LFpower;
        double LBpower;
        double RFpower;
        double RBpower;

        //left.setDirection(DcMotorSimple.Direction.REVERSE);//Or .FORWARD
        //Mecahnim wheels



        LFpower = lStickY + lStickX - rStickY;
        LBpower = lStickY - lStickX - rStickY;

        RFpower = lStickY - lStickX + rStickY;
        RBpower = lStickY + lStickX + rStickY;

        robot.left_f.setPower(Range.clip(LFpower,-1,1));
        robot.right_f.setPower(Range.clip(RFpower,-1,1));
        robot.left_b.setPower(Range.clip(LBpower,-1,1));
        robot.right_b.setPower(Range.clip(RBpower,-1,1));

        //End of Mechanim wheel
        rTrigger = Range.clip(rTrigger,0.5,1);
        //Controlls right side
        lTrigger = lTrigger/-5 + 0.5;          //Controlls left side
        /*
        if (rTrigger > 0.5){
            elevator.setPower(rTrigger);
        } else if (lTrigger > 0){
            elevator.getPower(lTrigger);
        }
        */
        robot.elevator.setPower(gamepad2.left_stick_y);
        //Shooter code

        if (lBumper)  {
            robot.shooter_l.setPower(1);
        } else {
            robot.shooter_l.setPower(0);
        }

        if (rBumper)   {
            robot.shooter_r.setPower(-1);
        } else {
            robot.shooter_r.setPower(0);
        }




    }
}
