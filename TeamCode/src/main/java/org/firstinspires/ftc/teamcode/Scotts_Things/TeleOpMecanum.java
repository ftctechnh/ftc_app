package org.firstinspires.ftc.teamcode.Scotts_Things;

import android.widget.Switch;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.*;

import org.firstinspires.ftc.teamcode.Scotts_Things.HardwareFile2019;

import static org.firstinspires.ftc.teamcode.Scotts_Things.HardwareFile2019.*;

@TeleOp(name = "Mechanum")
@Disabled
public class TeleOpMecanum extends LinearOpMode {

    double frontR, frontL, backR, backL;

    double gamepadX, gamepadY, gamepadZ;
    int angle;
    HardwareFile2019 robot = new HardwareFile2019();


    @Override
    public void runOpMode() throws InterruptedException {

        robot.mapHardware(hardwareMap);

        telemetry.addData("Welcome Driver(s)", "Hardware Mapping Complete");
        telemetry.update();

        robot.setRightSideInversionModifier();

        while (opModeIsActive()) {

            getXYZ();
            frontL = gamepadX + gamepadY + gamepadZ;
            frontR = -gamepadX + gamepadY - gamepadZ;
            backL = -gamepadX + gamepadY + gamepadZ;
            backR = gamepadX + gamepadY - gamepadZ;

            frontLeft.setPower(frontL * maxOutput);
            frontRight.setPower(frontR * maxOutput * rightSideInversionModifier);
            backLeft.setPower(backL * maxOutput);
            backRight.setPower(backR * maxOutput * rightSideInversionModifier);


            telemetry.addData("Gamepad Values", "Left Stick X:" + gamepadX + "||"
                    + "Left Stick Y:" + gamepadY);
            telemetry.update();
        }
    }

    public void getXYZ() {
        gamepadX = robot.deadZone(gamepad1.left_stick_x, 0.2);
        gamepadY = robot.deadZone(gamepad1.left_stick_y, 0.2);
        gamepadZ = robot.zManipulation(gamepad1.left_trigger, gamepad1.right_trigger);
    }


}
