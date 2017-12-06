package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="linearActuator", group="Testing")
public class servoTest extends LinearOpMode {
    static final double INCREMENT   = 0.1;     // amount to slew servo each CYCLE_MS cycle
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.2;     // Minimum rotational position
    double position1 = 0;
    double position2 = 0;
    Servo servo1;
    Servo servo2;

    @Override
    public void runOpMode() throws InterruptedException {
        servo1 = hardwareMap.servo.get("servoTest");
        servo2 = hardwareMap.servo.get("servoTest2");
        waitForStart();
        while(opModeIsActive())
        {
            if (gamepad1.dpad_up && position1 < MAX_POS)
            {
                position1 = MAX_POS;
            }
            else if(gamepad1.dpad_down && position1 > 0)
            {
                position1 = MIN_POS;
            }
            if (gamepad2.dpad_up && position2 < MAX_POS)
            {
                position2 = MAX_POS;
            }
            else if(gamepad2.dpad_down && position2 > 0)
            {
                position2 = MIN_POS;
            }
            // Display the current value
            telemetry.addData("Servo Position 1", "%5.2f", position1);
            telemetry.addData("Servo Position 2", "%5.2f", position2);
            telemetry.update();

            servo1.setPosition(position1);
            servo2.setPosition(position2);
            idle();
        }
    }
}
