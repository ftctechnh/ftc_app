package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous (name = "Encoder test")
public class EncoderTest extends LinearOpMode {

    HardwareRobot robot = new HardwareRobot();

    public void runOpMode() throws InterruptedException{

        robot.init(hardwareMap);
        waitForStart();


        telemetry.addData("run mode: ",robot.rightDriveFront.getMode());





        //while (opModeIsActive())
        //{

            //robot.leftDriveBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        //robot.encoderSwitch();
        int current = robot.rightDriveFront.getCurrentPosition();
        int target = current + 10000;
        telemetry.addData("run mode: ",robot.rightDriveFront.getMode());
        telemetry.addData("current pos", robot.rightDriveFront.getCurrentPosition());
        telemetry.update();

        //robot.rightDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //robot.rightDriveFront.setMode(DcMotor.RunMode.RESET_ENCODERS);

        //robot.leftDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //robot.leftDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //robot.rightDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

//        telemetry.addData("leftPos1", robot.leftDriveBack.getCurrentPosition());
//        telemetry.update();



        robot.rightDriveFront.setTargetPosition(target);
        telemetry.addData("target pos: ",robot.rightDriveFront.getTargetPosition());

        robot.rightDriveFront.setPower(1);
        while(Math.abs(target - robot.rightDriveFront.getCurrentPosition()) > 1)
        {

        }
        //robot.rightDriveFront.
        robot.rightDriveFront.setPower(0);
        telemetry.addData("current pos", robot.rightDriveFront.getCurrentPosition());
        telemetry.update();
        Thread.sleep(10000);




    }
}
