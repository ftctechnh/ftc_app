package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.AnalogOutput;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.TelemetryDashboardAndLog;
import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.IFunc;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * SynchAnalogOutputDemo is a short demo of an AnalogOutput port.
 * We could look at the output signal in an oscilloscope or we could use a piezo buzzer to hear the output.
 *
 * THIS CODE IS NOT WORKING YET.
 *
 * I don't know why the Analog Out port isn't showing any signal,
 * and I don't know what the valid values are for the output port's "mode"
 * ("mode" is a byte which supposedly sets the output waveform to sine, square, triangle, or DC).
 */
@TeleOp(name="AnalogOut demo", group="Swerve Examples")
@Disabled
public class SynchAnalogOutputDemo extends SynchronousOpMode {

    AnalogOutput analog;
    byte mode = 0x01; //what are the valid values for mode???

    /*
    * Frequencies of notes in a well-tempered scale based on A440 (the 49th key on a piano).
    * A code purist might prefer to calculate the notes on the fly, using the formula
    * ((12th root of 2) raised to the power of (n-49)) * 440Hz, where n is the nth key on a piano.
    * However, I will put pre-calculated frequencies into an array and avoid doing the math on the fly and to avoid floating point math imprecision..
    * I would prefer to use an array of doubles here because they are more precise,
    * but the Core Device Interface requires integer frequencies for Analog Out.
    * For reference, the double values would be:
    * double[] notesd = { 16.35, 17.32, 18.35, 19.45, 20.60, 21.83, 23.12, 24.50, 25.96, 27.50, 29.14, 30.87, 32.70, 34.65, 36.71, 38.89, 41.20, 43.65, 46.25, 49.00, 51.91, 55.00, 58.27, 61.74, 65.41, 69.30, 73.42, 77.78, 82.41, 87.31, 92.50, 98.00, 103.8, 110.0, 116.5, 123.5, 130.8, 138.6, 146.8, 155.6, 164.8, 174.6, 185.0, 196.0, 207.7, 220.0, 233.1, 246.9, 261.6, 277.2, 293.7, 311.1, 329.6, 349.2, 370.0, 392.0, 415.3, 440.0, 466.2, 493.9, 523.3, 554.4, 587.3, 622.3, 659.3, 698.5, 740.0, 784.0, 830.6, 880.0, 932.3, 987.8, 1047, 1109, 1175, 1245, 1319, 1397, 1480, 1568, 1661, 1760, 1865, 1976, 2093, 2217, 2349, 2489, 2637, 2794, 2960, 3136, 3322, 3520, 3729, 3951, 4186, 4435, 4699, 4978, 5274, 5588, 5920, 6272, 6645, 7040, 7459, 7902 };
    */
    int[] notes = { 16, 17, 18, 19, 21, 22, 23, 24, 26, 28, 29, 31, 33, 35, 37, 39, 41, 44, 46, 49, 52, 55, 58, 62, 65, 69, 73, 78, 82, 87, 92, 98, 104, 110, 116, 124, 131, 139, 147, 156, 165, 175, 185, 196, 208, 220, 233, 247, 262, 277, 294, 311, 330, 349, 370, 392, 415, 440, 466, 494, 523, 554, 587, 622, 659, 698, 740, 784, 831, 880, 932, 988, 1047, 1109, 1175, 1245, 1319, 1397, 1480, 1568, 1661, 1760, 1865, 1976, 2093, 2217, 2349, 2489, 2637, 2794, 2960, 3136, 3322, 3520, 3729, 3951, 4186, 4435, 4699, 4978, 5274, 5588, 5920, 6272, 6645, 7040, 7459, 7902};
    static final int A440_INDEX = 57; //the position of A440 in the array
    //Many devices can only play a limited range of notes.
    //For my purposes, I'll set somewhat reasonable limits here.
    static final int MAX_NOTE_INDEX = 108;
    static final int MIN_NOTE_INDEX = 14;

    int current_note_index = A440_INDEX; //start at A440.

    int getFrequency(int index)
    {
        //ASSERT ((index >= MIN_NOTE_INDEX) && (index <= MAX_NOTE_INDEX));
        return notes[index];
    }


    @Override public void main() throws InterruptedException {
        // We are expecting the analog output to be attached to a core device interface module
        // and named "aout".
        analog = hardwareMap.analogOutput.get("aout");

        // Set up our dashboard computations
        composeDashboard();

        // Wait until we're told to go
        waitForStart();

        analog.setAnalogOutputVoltage(4);
        analog.setAnalogOutputFrequency(getFrequency(current_note_index));
        //analog.setAnalogOutputMode(mode);  //what are the values for mode???

        // Loop and update the dashboard
        while (this.opModeIsActive()) {
            if (this.updateGamepads()) {
                if (this.gamepad1.a) {
                    if (current_note_index < MAX_NOTE_INDEX) current_note_index++;
                    analog.setAnalogOutputFrequency(getFrequency(current_note_index));
                } else if (this.gamepad1.b) {
                    if (current_note_index > MIN_NOTE_INDEX) current_note_index--;
                    analog.setAnalogOutputFrequency(getFrequency(current_note_index));
                } else if (this.gamepad1.x) {//we don't know what the valid mode values are so we have to explore
                    mode++;
                    analog.setAnalogOutputMode(mode);
                } else if (this.gamepad1.y) { //for now, we don't care if mode goes "negative" since we don't know what values of mode are valid yet
                    mode--;
                    analog.setAnalogOutputMode(mode);
                } else if (this.gamepad1.left_bumper) { //a 'reset' to see if this will get the port working
                    analog.setAnalogOutputVoltage(4);
                    current_note_index = A440_INDEX;
                    analog.setAnalogOutputFrequency(getFrequency(current_note_index));
                    analog.setAnalogOutputMode(mode);
                }
            }

            telemetry.update();
            idle();
        }
    }
    
    void composeDashboard() {
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
                        return getFrequency(current_note_index);
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
