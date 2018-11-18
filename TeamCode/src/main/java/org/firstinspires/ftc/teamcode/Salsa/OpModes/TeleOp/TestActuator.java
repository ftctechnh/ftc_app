package org.firstinspires.ftc.teamcode.Salsa.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Salsa.Hardware.Subcomponents.Motor;
import org.firstinspires.ftc.teamcode.Salsa.Methods.SalsaOpMode;

/**
 * Created by adityamavalankar on 11/15/18.
 */

@TeleOp(name = "Test Actuator")
public class TestActuator extends SalsaOpMode {

    Motor actuator;

    /**
     * An example class which demonstrates the actuator. It also shows how we can add a single object
     * like the Motor actuator
     */

    @Override
    public void init() {
        robot.initDrivetrain(hardwareMap);
        actuator.init("actuator", hardwareMap);
    }

    @Override
    public void loop() {

        drive();
        actuatorMove();

    }

    public void actuatorMove() {
        if(gamepad1.dpad_up) {
            actuator.setPower(1);
        }

        else if(gamepad1.dpad_down) {
            actuator.setPower(-1);
        }
        else {
            actuator.setPower(0);
        }
    }
}
