package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;

/**
 * Created by Shreyas on 10/27/17.
 */
@Autonomous
public class EncoderTest extends LinearOpMode{

    private DcMotor Motor1;
    private DcMotor Motor2;
    private DcMotor Motor3;
    private DcMotor Motor4;
    public void driveForward(int ticks, double power) {
        Motor1.setMode(RunMode.STOP_AND_RESET_ENCODER);
        Motor2.setMode(RunMode.STOP_AND_RESET_ENCODER);
        Motor3.setMode(RunMode.STOP_AND_RESET_ENCODER);
        Motor4.setMode(RunMode.STOP_AND_RESET_ENCODER);

        Motor1.setTargetPosition(-ticks);
        Motor2.setTargetPosition(-ticks);
        Motor3.setTargetPosition(-ticks);
        Motor4.setTargetPosition(-ticks);


        Motor1.setMode(RunMode.RUN_TO_POSITION);
        Motor2.setMode(RunMode.RUN_TO_POSITION);
        Motor3.setMode(RunMode.RUN_TO_POSITION);
        Motor4.setMode(RunMode.RUN_TO_POSITION);


        Motor1.setPower(power);
        Motor2.setPower(power);
        Motor3.setPower(power);
        Motor4.setPower(power);


        while (Motor2.isBusy() && Motor1.isBusy() && Motor3.isBusy() && Motor4.isBusy()) {
        }

        Motor1.setPower(0);
        Motor2.setPower(0);
        Motor3.setPower(0);
        Motor4.setPower(0);

        Motor1.setMode(RunMode.RUN_USING_ENCODER);
        Motor2.setMode(RunMode.RUN_USING_ENCODER);
        Motor3.setMode(RunMode.RUN_USING_ENCODER);
        Motor4.setMode(RunMode.RUN_USING_ENCODER);


    }




    @Override
    public void runOpMode() throws InterruptedException {
        Motor1 = hardwareMap.dcMotor.get("Motor1");
        Motor2 = hardwareMap.dcMotor.get("Motor2");
        Motor3 = hardwareMap.dcMotor.get("Motor3");
        Motor4 = hardwareMap.dcMotor.get("Motor4");
        Motor4.setDirection(Direction.REVERSE);
        Motor2.setDirection(Direction.REVERSE);
        waitForStart();
        driveForward(4278,0.5);






    }
}
