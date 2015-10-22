package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.LED;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.IFunc;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * SynchLEDDemo is a short demo on using the digital output pins to light an LED.
 * It assumes that you have 1 led connected to a Core Device Interface
 * and that the led is named "led" in your robot configuration file.
 */
@TeleOp(name="LED demo", group="Swerve Examples")
@Disabled
public class SynchLEDDemo extends SynchronousOpMode {

    LED led;
    boolean led_state = false; //unlike DigitalChannel, leds don't let you read their state, so we need to store it elsewhere.

    @Override public void main() throws InterruptedException {
        // We are expecting the LED to be attached to a digital port on a core device interface
        // module and named "led".
        led = hardwareMap.led.get("led");

        // Set up our dashboard computations
        composeDashboard();

        // Wait until we're told to go
        waitForStart();
               
        // Loop and update the dashboard
        while (this.opModeIsActive()) {
            if (this.updateGamepads()) {
                if (this.gamepad1.a) {
                    led_state = true;
                    led.enable(led_state);
                } else if (this.gamepad1.b) {
                    led_state = false;
                    led.enable(led_state);
                }
            }
            telemetry.update();

            idle();
        }
    }
    
    void composeDashboard() {
        telemetry.addLine(
                telemetry.item("loop count: ", new IFunc<Object>() {
                    @Override
                    public Object value() {
                        return getLoopCount();
                    }
                }));
        telemetry.addLine(
                telemetry.item("controls: ", new IFunc<Object>() {
                    @Override
                    public Object value() {
                        return "a:on, b:off";
                    }
                }));
        telemetry.addLine(
                telemetry.item("led state: ", new IFunc<Object>() {
                    @Override
                    public Object value() {
                        //return led.getState(); //unlike DigitalChannel, leds don't let you read their state, so use the variable here instead.
                        return led_state;
                    }
                }));
    }

}
