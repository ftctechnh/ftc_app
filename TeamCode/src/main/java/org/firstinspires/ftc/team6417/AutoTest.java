package org.firstinspires.ftc.team6417;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="AutoTest", group="Autonomous")
public class AutoTest extends LinearOpMode {

    Hardware6417 robot = new Hardware6417();
    private ElapsedTime runtime = new ElapsedTime();

    public void runOpMode() {

        robot.init(hardwareMap);

        telemetry.addData("Status", "Ready to run");
        telemetry.update();
        waitForStart();

        drivetoPosition(1, 0.2);

        /***
        robot.drivetoPosition(60, 0.2);

        //turn

        robot.dragLeft.setPosition(robot.dragLeft.getPosition() + 0.5);
        robot.dragRight.setPosition(robot.dragRight.getPosition() + 0.5);

        robot.dragLeft.setPosition(robot.dragLeft.getPosition() - 0.5);
        robot.dragRight.setPosition(robot.dragRight.getPosition() - 0.5);
        robot.strafeToPosition(-40, 0.2);
         ***/

    }

    public void drivetoPosition(int d, double power){

        int distance = (int)(robot.CPR / (robot.DIAMETER * Math.PI) * d);
        telemetry.addData("Distance: ", distance);
        telemetry.update();

        robot.leftFront.setTargetPosition(robot.leftFront.getCurrentPosition() + distance);
        robot.rightFront.setTargetPosition(robot.rightFront.getCurrentPosition() + distance);
        robot.leftBack.setTargetPosition(robot.leftBack.getCurrentPosition() + distance);
        robot.rightBack.setTargetPosition(robot.rightBack.getCurrentPosition() + distance);

        robot.leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftFront.setPower(power);
        robot.rightFront.setPower(power);
        robot.leftBack.setPower(power);
        robot.rightBack.setPower(power);

        while(robot.leftFront.isBusy() || robot.rightFront.isBusy() || robot.leftBack.isBusy() || robot.rightBack.isBusy()){
            telemetry.addData("Position:", robot.leftFront.getCurrentPosition());
            telemetry.update();
        }

        robot.leftFront.setPower(0);
        robot.rightFront.setPower(0);
        robot.leftBack.setPower(0);
        robot.rightBack.setPower(0);

        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }


    public void strafeToPosition(int d, double power){

        robot.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int distance = (int)(robot.CPR / (robot.DIAMETER * Math.PI) * d);

        robot.leftFront.setTargetPosition(distance);
        robot.rightFront.setTargetPosition(-distance);
        robot.leftBack.setTargetPosition(-distance);
        robot.rightBack.setTargetPosition(distance);

        robot.leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftFront.setPower(power);
        robot.rightFront.setPower(power);
        robot.leftBack.setPower(power);
        robot.rightBack.setPower(power);

        while(robot.leftFront.isBusy() || robot.rightFront.isBusy() || robot.leftBack.isBusy() || robot.rightBack.isBusy()){ }

        robot.leftFront.setPower(0);
        robot.rightFront.setPower(0);
        robot.leftBack.setPower(0);
        robot.rightBack.setPower(0);

        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void turnWithEncoder(double input){
        robot.leftFront.setPower(input);
        robot.leftBack.setPower(input);
        robot.rightFront.setPower(-input);
        robot.rightBack.setPower(-input);
    }




}