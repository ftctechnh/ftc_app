package org.firstinspires.ftc.team9374.team9374;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.team9374.team9374.Hardware9374;

/*
 * Created by darwin on 10/29/16.
 */
@Autonomous(name = "9374_MAIN_AUTONOMOUS_CENTER",group = "null")

public class NineK_MainBotA extends LinearOpMode {

    Hardware9374 robot = new Hardware9374();


    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);
        super.waitForStart();

        while (super.opModeIsActive()) {
            /*

            //Dont quite know what this is?
            int clicks = calcClicksForInches(5);

            setALLpower(.5);

            setALLposition(clicks);

            while (opModeIsActive()) {
                telemetry.addData("Target:", clicks);
                telemetry.addData("Left Position", left_f.getCurrentPosition());
                if (left_f.getCurrentPosition() > clicks) {
                    break;
                }
            }
            */
            //Spin up the motors, then shoot after  secounds.
            while (true) {
                robot.shooter_l.setPower(1);
                robot.shooter_r.setPower(1);
                if (robot.runTime.time() > 5) {
                    robot.elevator.setPower(.2);
                    if (robot.runTime.time() > 10) {
                        break;
                    }
                }
            }
            //move 55 inches
            int clicks = robot.calcClicksForInches(55);
            //GOGOOG
            robot.setALLpower(.5);

            robot.setALLposition(clicks);
            //Printing out our
            while (opModeIsActive()) {
                telemetry.addData("Target:", clicks);
                telemetry.addData("Left Position", robot.left_f.getCurrentPosition());
                if (robot.left_f.getCurrentPosition() > clicks) {
                    break;
                }
            }
            break;
        }

    }
}
