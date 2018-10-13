package org.firstinspires.ftc.teamcode.test_programs;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by AshQuinn on 10/6/18.
 */
@TeleOp(name="LinearActTest", group="Test")
//@Disabled

public class LinearActTest extends LinearOpMode {
    public DcMotor LinAct;
    double Power = 0.5;

    @Override
    public void runOpMode() {
        LinAct = hardwareMap.dcMotor.get("LinAct");
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                LinAct.setPower(-Power);
            } else if (gamepad1.y) {
                LinAct.setPower(Power);
            } else {
                LinAct.setPower(0);
            }

        }
    }

}
