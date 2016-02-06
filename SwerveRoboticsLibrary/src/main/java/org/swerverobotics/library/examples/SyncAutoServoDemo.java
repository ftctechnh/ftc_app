package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.*;

/**
 * This simple OpMode illustrates the basic use of a servo
 * from a synchronous OpMode. It expects a single servo, named
 * "servo". It works with both modern and legacy servo controllers.
 */
@Autonomous(name="Auto Servo (Sync)", group="Swerve Examples")
// @Disabled
public class SyncAutoServoDemo extends SynchronousOpMode
    {
    Servo servo;

    @Override protected void main() throws InterruptedException
        {
        this.servo = this.hardwareMap.servo.get("servo");

        configureDashboard();

        waitForStart();

        for (int i = 0; i < 10; i++)
            {
            this.servo.setPosition(0);
            delay();
            telemetry.updateNow();
            this.servo.setPosition(1);
            delay();
            telemetry.updateNow();
            }
        }

    void delay() throws InterruptedException
        {
        ElapsedTime elapsed = new ElapsedTime();
        while (elapsed.time() < 0.25)
            this.idle();
        }

    void configureDashboard()
        {
        telemetry.addLine(telemetry.item("position: ", new IFunc<Object>()
            {
            @Override public Object value()
                {
                return servo.getPosition();
                }
            }));
        }
    }
