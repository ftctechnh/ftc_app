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
        int target = current + (int)(robot.COUNTS_PER_INCH * 10);
        telemetry.addData("run mode: ",robot.rightDriveFront.getMode());
        telemetry.addData("current pos", robot.rightDriveFront.getCurrentPosition());
        telemetry.update();

        robot.rightDriveFront.setTargetPosition(target);
        robot.rightDriveBack.setTargetPosition(target);
        robot.leftDriveFront.setTargetPosition(target);
        robot.leftDriveBack.setTargetPosition(target);
        telemetry.addData("target pos: ",robot.rightDriveFront.getTargetPosition());

        robot.setAllLeftDrivePower(1);
        robot.setAllRightDrivePower(1);
        while( Math.abs(target - robot.leftDriveFront.getCurrentPosition()) > 1 && Math.abs(target - robot.rightDriveFront.getCurrentPosition()) > 1 && Math.abs(target - robot.leftDriveBack.getCurrentPosition()) > 1 && Math.abs(target - robot.rightDriveBack.getCurrentPosition()) > 1)
        {

        }
        //robot.rightDriveFront.
        //robot.rightDriveFront.setPower(0);
        robot.setAllLeftDrivePower(0);
        robot.setAllRightDrivePower(0);
        telemetry.addData("current pos", robot.rightDriveFront.getCurrentPosition());
        telemetry.update();
        Thread.sleep(10000);




    }
}
