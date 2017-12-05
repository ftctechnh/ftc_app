package org.firstinspires.ftc.teamcode.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;

/**
 * Created by thund on 12/4/2017.
 */

@TeleOp(name="JeffsRealRun",group="Jeff" )

public class TestingTheNewMotors extends LinearOpMode {
    DcMotor extendArmMotor;
    DcMotor upArmMotor;
    DcMotor retractyWireMotor;
    DigitalChannel extendBackSensor;
    double extendArmPower = .1;
    double retractifyPower = .1;


    @Override
    public void runOpMode() throws InterruptedException {
        extendArmMotor = hardwareMap.dcMotor.get("extendArm");
        extendArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upArmMotor = hardwareMap.dcMotor.get("upArm");
        upArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        retractyWireMotor = hardwareMap.dcMotor.get("retract");
        retractyWireMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendBackSensor = hardwareMap.digitalChannel.get("armin");

        while (opModeIsActive()){
            upArmMotor.setPower(gamepad1.left_stick_y);

            //mapping of the gamepad to actions
            boolean reverseExtendMotor = gamepad1.dpad_left;
            boolean reverseRetractMotor = gamepad1.dpad_right;
            boolean increaseExtendPower = gamepad1.left_bumper;
            boolean decreaseExtendPower = gamepad1.right_bumper;
            boolean increaseRetractPower = gamepad1.dpad_up;
            boolean decreaseRetractPower = gamepad1.dpad_down;
            boolean moveArmOut = gamepad1.x;
            boolean stopArm = gamepad1.a;
            boolean moveArmIn = gamepad1.y;
            boolean floatRetractMotor = gamepad1.b;
            boolean brakeRetractMotor = gamepad1.b && gamepad1.a; //I know, I hate me too

            telemetry.addData("extend motor encoder position", extendArmMotor.getCurrentPosition());
            telemetry.addData("extend motor power", extendArmPower);
            telemetry.addData("retract motor encoder position", retractyWireMotor.getCurrentPosition());
            telemetry.addData("retract motor power", retractifyPower);
            telemetry.update();


            if (reverseExtendMotor){
                extendArmMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            }
            if (reverseRetractMotor){
                retractyWireMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            }

            if(increaseExtendPower){
                extendArmPower += .1;
            }
            if(decreaseExtendPower){
                extendArmPower -= .1;
                if (extendArmPower < 0){
                    extendArmPower = 0;
                }
            }
            if (increaseRetractPower){
                retractifyPower += .1;
            }

            if (decreaseRetractPower){
                retractifyPower -= .1;
                if(retractifyPower < 0){
                    retractifyPower = 0;
                }
            }

            if(moveArmOut){
                extendArmMotor.setPower(extendArmPower);
            }
            else if(moveArmIn && !extendBackSensor.getState()){
                extendArmMotor.setPower(-extendArmPower);
            }
            else{
                extendArmMotor.setPower(0);
            }
            if(stopArm){
                extendArmMotor.setPower(0);
            }


        }
    }

    private void floatTheMotor(DcMotor motor){
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }
    private void brakeTheMotor(DcMotor motor){
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
