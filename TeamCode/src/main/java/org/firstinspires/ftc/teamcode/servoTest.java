package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class servoTest extends LinearOpMode {
    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position
    double position = 0;
    Servo servo;

    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.servo.get("servoTest");
        waitForStart();
        while(opModeIsActive())
        {
            if (gamepad1.dpad_up && position < MAX_POS)
            {
                position += INCREMENT ;
                if (position > MAX_POS )
                {
                    position = MAX_POS;
                }
            }
            else if(gamepad2.dpad_down && position > 0)
            {
                position -= INCREMENT ;
                if (position < MIN_POS )
                {
                    position = MIN_POS;
                }
            }

            // Display the current value
            telemetry.addData("Servo Position", "%5.2f", position);
            telemetry.update();

            servo.setPosition(position);
            idle();
        }
    }
}
