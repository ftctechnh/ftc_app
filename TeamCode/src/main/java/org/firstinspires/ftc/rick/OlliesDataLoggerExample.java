package org.firstinspires.ftc.rick;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.Examples.DataLogger;

import java.io.File;


/**
 * This OpMode illustrates using the Device Interface Module as a signalling device.
 * The code is structured as a LinearOpMode
 *
 * This code assumes a DIM name "dim".
 *
 * There are many examples where the robot might like to signal the driver, without requiring them
 * to look at the driver station.  This might be something like a "ball in hopper" condition or a
 * "ready to shoot" condition.
 *
 * The DIM has two user settable indicator LEDs (one red one blue).  These can be controlled
 * directly from your program.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name = "Ricks Test: DIM As Indicator", group = "Ricks")
@Disabled
public class OlliesDataLoggerExample extends LinearOpMode {

    static final int    BLUE_LED    = 0;     // Blue LED channel on DIM
    static final int    RED_LED     = 1;     // Red LED Channel on DIM

    // Create timer to toggle LEDs
    private ElapsedTime runtime = new ElapsedTime();

    // Define class members
    DeviceInterfaceModule   dim;

    private DataLogger ltDl; // Linear Time Data Logger


    @Override
    public void runOpMode() {

        // Connect to motor (Assume standard left wheel)
        // Change the text in quotes to match any motor name on your robot.
        dim = this.hardwareMap.deviceInterfaceModule.get("dim");

        // Toggle LEDs while Waiting for the start button
        telemetry.addData(">", "Press Play to test LEDs.");
        telemetry.update();


            ltDl = new DataLogger("DlDemo_lt");
            ltDl.addField("Clr");
            ltDl.addField("Red");
            ltDl.addField("Blue");
            ltDl.addField("Green");
            ltDl.addField("Dist");
            ltDl.newLine();


        while (!isStarted()) {
            // Determine if we are on an odd or even second
            boolean even = (((int) (runtime.time()) & 0x01) == 0);
            dim.setLED(RED_LED, even); // Red for even
            dim.setLED(BLUE_LED, !even); // Blue for odd
            idle();
        }

        // Running now
        telemetry.addData(">", "Press X for Blue, B for Red.");
        telemetry.update();



        RdataLogger log1 = new RdataLogger("ricksfile.txt");
        File f = new File("/sdcard/FIRST/DataLog/ricksfile2.txt");
        RdataLogger log2 = new RdataLogger(f);
        log1.log(String.format("*********************************"));
        log2.log(String.format("*********************************"));

        String filename = "RicksFile.txt";
        telemetry.log().add("saved to '%s'", filename);

        DbgLog.msg("this is the log");



        ltDl.addField(111);
        ltDl.addField(222);
        ltDl.addField(333);
        ltDl.addField(444);
        ltDl.addField(555);
        ltDl.newLine();
        ltDl.addField(222);
        ltDl.addField(333);
        ltDl.addField(444);
        ltDl.addField(555);
        ltDl.newLine();
        ltDl.closeDataLogger();

        boolean redlight = true;
        boolean bluelight = true;
        boolean buttonXIsReleased = true;
        boolean buttonBIsReleased = true;

        // Now just use red and blue buttons to set red and blue LEDs
        while (opModeIsActive()) {

            if (gamepad1.x) {
                // Only toggle  when the button was released and it is now pressed.
                if (buttonXIsReleased) {
                    bluelight = !bluelight;
                    buttonXIsReleased = false;
                    dim.setLED(BLUE_LED, bluelight);
                    log1.log(String.format("blue %B at "+ runtime.toString(),bluelight));
                    DbgLog.msg("Blue Boolean %B",bluelight);

                }
            } else {
                buttonXIsReleased = true;
            }

        if (gamepad1.b) {
            // Only toggle  when the button was released and it is now pressed.
            if (buttonBIsReleased) {
                redlight = !redlight;
                buttonBIsReleased = false;
                dim.setLED(RED_LED, redlight);
                log1.log(String.format("red  %B at "+ runtime.toString(),redlight));
           }
        } else {
            buttonBIsReleased = true;
        }




            idle();
        }

        // Turn off LEDs;
        dim.setLED(BLUE_LED, false);
        dim.setLED(RED_LED,  false);
        telemetry.addData(">", "Done");
        telemetry.update();
    }
}
