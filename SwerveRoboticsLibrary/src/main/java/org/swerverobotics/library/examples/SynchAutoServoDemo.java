package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Func;
import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.*;

/**
 * This simple OpMode illustrates the basic use of a servo
 * from a synchronous OpMode. It expects a single servo, named
 * "servo". It works with both modern and legacy servo controllers.
 */
@Autonomous(name="Auto Servo (Synch)", group="Swerve Examples")
// @Disabled
public class SynchAutoServoDemo extends SynchronousOpMode
    {
    Servo servo;

    @Override protected void main() throws InterruptedException
        {
        this.servo = this.hardwareMap.servo.get("servo");

        configureDashboard();

        waitForStart();

        for (int i = 0; opModeIsActive() && i < 10; i++)
            {
            this.servo.setPosition(0);
            delay();
            telemetry.update();
            this.servo.setPosition(1);
            delay();
            telemetry.update();
            }
        }

    void delay() throws InterruptedException
        {
        Thread.sleep(250);
        }

    void configureDashboard()
        {
        telemetry.addData("position: ", new Func<Object>()
            {
            @Override public Object value()
                {
                return servo.getPosition();
                }
            });
        }
    }
