package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.TelemetryDashboardAndLog;
import org.swerverobotics.library.interfaces.IAction;
import org.swerverobotics.library.interfaces.IFunc;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * SynchLEDDemo is a short demo on using the digital output pins to light an LED.
 * It assumes that you have 1 led connected to a Core Device Interface
 * and that the led is named "led" in your robot configuration file.
 */
@TeleOp(name="LED")
public class SynchLEDDemo extends SynchronousOpMode {

    DigitalChannel led;

    @Override public void main() throws InterruptedException {
        // We are expecting the LED to be attached to a digital port on a core device interface
        // module and named "led".
        led = hardwareMap.digitalChannel.get("led");
        led.setMode(DigitalChannelController.Mode.OUTPUT);

        // Set up our dashboard computations
        composeDashboard();

        // Wait until we're told to go
        waitForStart();
               
        // Loop and update the dashboard
        while (this.opModeIsActive()) {
            if (this.updateGamepads()) {
                if (this.gamepad1.a) {
                    led.setState(true);
                } else if (this.gamepad1.b) {
                    led.setState(false);
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
                        return led.getState();
                    }
                }));
    }

}
