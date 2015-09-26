package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.AnalogOutput;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.TelemetryDashboardAndLog;
import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.IFunc;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * SynchAnalogOutputDemo is a short demo of an AnalogOutput port.
 * We will look at the output signal in an oscilloscope, but we could also use a piezo buzzer to hear the output
 *
 * THIS CODE IS NOT WORKING YET...
 */
@TeleOp(name="AnalogOut demo")
@Disabled
public class SynchAnalogOutputDemo extends SynchronousOpMode {

    AnalogOutput analog;
    int frequency = 440; //this is A440.
    //the difference between notes on an even-tempered scale changes as you move up/down the scale,
    //but I'm keeping this simple (i.e. wrong) for now, at least until I can see some output on the Analog port
    int notediff = 26;
    byte mode = 0; //what are the valid values for mode???

    @Override public void main() throws InterruptedException {
        // We are expecting the analog output to be attached to a core device interface
        // module and named "analogout".
        analog = hardwareMap.analogOutput.get("analogout");

        // Set up our dashboard computations
        composeDashboard();

        // Wait until we're told to go
        waitForStart();

        analog.setAnalogOutputVoltage(4);
        analog.setAnalogOutputFrequency(frequency);
        //analog.setAnalogOutputMode(mode);  //what are the values for mode???

        // Loop and update the dashboard
        while (this.opModeIsActive()) {
            if (this.updateGamepads()) {
                if (this.gamepad1.a) {
                    frequency += notediff;
                    analog.setAnalogOutputFrequency(frequency);
                } else if (this.gamepad1.b && (frequency > notediff) ) { //don't let frequency get below the lowest notediff above zero
                    frequency -= notediff;
                    analog.setAnalogOutputFrequency(frequency);
                } else if (this.gamepad1.x) {//we don't know what the valid mode values are so we have to explore
                    mode++;
                    analog.setAnalogOutputMode(mode);
                } else if (this.gamepad1.y) { //for now, we don't care if mode goes "negative" since we don't know what values of mode are valid yet
                    mode--;
                    analog.setAnalogOutputMode(mode);
                } else if (this.gamepad1.left_bumper) { //a 'reset' to see if this will get the port working
                    analog.setAnalogOutputVoltage(4);
                    analog.setAnalogOutputFrequency(frequency);
                    analog.setAnalogOutputMode(mode);
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
                telemetry.item("help: ", new IFunc<Object>() {
                    @Override
                    public Object value() {
                        return "freq:a+ b-, mode:x+ y-";
                    }
                }));
        telemetry.addLine(
                telemetry.item("frequency: ", new IFunc<Object>() {
                    @Override
                    public Object value() {
                        return frequency;
                    }
                }));
        telemetry.addLine(
                telemetry.item("mode: ", new IFunc<Object>() {
                    @Override
                    public Object value() {
                        return mode;
                    }
                }));
    }
    
}
