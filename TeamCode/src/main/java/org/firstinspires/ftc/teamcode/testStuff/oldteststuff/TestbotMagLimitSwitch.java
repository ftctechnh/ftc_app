package org.firstinspires.ftc.teamcode.testStuff.oldteststuff;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp(name = "TestbotMagLimitSwitch")
public class TestbotMagLimitSwitch extends LinearOpMode {


    DigitalChannel limitSwitch;

    @Override

    public void runOpMode() throws InterruptedException {

        limitSwitch = hardwareMap.digitalChannel.get("digital_0");

        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("Limit Switch", limitSwitch.getState());
            telemetry.update();
        }
    }
}