package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@SuppressWarnings("WeakerAccess")
@TeleOp(name = "TeleOp", group = "Nessie")

public class TeleOpCore extends OpMode {

    private static final float Control_Threshold = 0.05F;
    private boolean lockout;

    @Override
    public void init() {
        Hardware.initAllHardware(hardwareMap);
        telemetry.addData("sos or sauce?","1");
        telemetry.update();
    }

    @Override
    public void loop()
    {
        //Forward and Backward
        Hardware.backLeftDrive.setPower(gamepad1.left_stick_y);
        Hardware.frontLeftDrive.setPower(gamepad1.left_stick_y);

        Hardware.backRightDrive.setPower(gamepad1.right_stick_y);
        Hardware.frontRightDrive.setPower(gamepad1.right_stick_y);
        //Turning In-Place
        if(gamepad1.right_trigger > 0){
            Hardware.backLeftDrive.setPower(1);
            Hardware.backRightDrive.setPower(-1);
            Hardware.frontLeftDrive.setPower(1);
            Hardware.frontRightDrive.setPower(-1);
        }
        if(gamepad1.left_trigger > 0){
            Hardware.backLeftDrive.setPower(1);
            Hardware.backRightDrive.setPower(-1);
            Hardware.frontLeftDrive.setPower(1);
            Hardware.frontRightDrive.setPower(-1);
        }
        

        //<DELETE> !!!!!Everything bellow this needs to be deleted!!!!!
        //Will need to get a new TeleOp for controller 2 (pester build about getting detailed blueprints with motors)


        //Controller 2 stuff
        if(gamepad2.left_stick_y != 0) {
            Hardware.armLiftMotorBottom.setPower(gamepad2.left_stick_y);
            Hardware.armLiftMotorTop.setPower(gamepad2.left_stick_y);
        }
        else{
            Hardware.armLiftMotorBottom.setPower(0);
            Hardware.armLiftMotorTop.setPower(0);

        }

       //Tilt lock
        if(Hardware.magLimitSwitchTilt.getState() && gamepad2.right_stick_y >= 0 || gamepad2.right_stick_y < -Control_Threshold){
            if (Hardware.armTiltMotor.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) Hardware.armTiltMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            Hardware.armTiltMotor.setPower(gamepad2.right_stick_y);
            lockout = false;
        } else if (Hardware.magLimitSwitchTilt.getState() && !lockout){
            Hardware.armTiltMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Hardware.armTiltMotor.setTargetPosition(Hardware.armTiltMotor.getCurrentPosition());
            lockout = true;
        }


        //left trigger closes the hand and right trigger opens it
        if(gamepad2.left_trigger > 0){
            Hardware.finger1.setPower(-1);
            Hardware.finger2.setPower(1);
        } else if(gamepad2.right_trigger > 0){
            Hardware.finger1.setPower(1);
            Hardware.finger2.setPower(-1);
        } else{
            Hardware.finger1.setPower(0);
            Hardware.finger2.setPower(0);
        }


        //right bumper on controller 2 makes wrist go up and left goes down
        if(gamepad2.right_bumper){
          Hardware.wrist.setPower(0.25);
        }else if(gamepad2.left_bumper){
        Hardware.wrist.setPower(-0.25);
    } else{
            Hardware.wrist.setPower(0);
        }


    }
}
