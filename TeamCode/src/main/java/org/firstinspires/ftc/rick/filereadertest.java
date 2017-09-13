package org.firstinspires.ftc.rick;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.Examples.DataLogger;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.FileReader;

@Autonomous(name = "menucode", group = "zRick")
@Disabled

//@Disabled
public class filereadertest extends LinearOpMode {

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

        int nitems = 3;
        int index = 0;
        boolean yisreleased = true;
        boolean aisreleased = true;
        boolean xisreleased = true;
        boolean bisreleased = true;
        boolean settingsdone = false;

        String[] label = {"Blue Team", "Delay", "Speed", "Mode"};
        int[] values = {1,20,30,40};

        while (!isStarted()) {
            // output current settings  (1st token is red/blue treat it separately.
            if (values[0] == 1) {
                if (index == 0 ){telemetry.addLine("BLUE Alliance <===");} else {telemetry.addLine("BLUE Alliance");}
            } else {
                if (index == 0 ){telemetry.addLine("RED Alliance <===");} else {telemetry.addLine("RED Alliance");}
            }

            for (int i=1; i<=nitems; i++){
                //telemetry.addData("*",values[i]);
                if (i == index) {
                    telemetry.addLine().addData(label[i], values[i]+"  <====");
                }else{
                    telemetry.addLine().addData(label[i], values[i]);
                }
            }
            telemetry.addLine("");

            if (!settingsdone) {

            boolean even = (((int) (runtime.time()) & 0x01) == 0);             // Determine if we are on an odd or even second
                if ( values[0] == 1) {
                dim.setLED(BLUE_LED, even); // blue for true
                dim.setLED(RED_LED, false);
            } else {
                dim.setLED(RED_LED,  even); // Red for false
                dim.setLED(BLUE_LED, false);
            }

            //menu


                 // decrease/increase the index
                 if (gamepad1.a) {
                     if (aisreleased) {
                         aisreleased = false;
                         index += 1;
                     }
                 } else {
                     aisreleased = true;
                 }
                 if (gamepad1.y) {
                     if (yisreleased) {
                         yisreleased = false;
                         index -= 1;
                     }
                 } else {
                     yisreleased = true;
                 }
                 if (index > nitems) index = 0;          // limit the bounds of index
                 if (index < 0) index = nitems;

                 // increase or decrease the values
                 if (gamepad1.b) {
                     if (bisreleased) {
                         bisreleased = false;
                         values[index] += 1;
                     }
                 } else {
                     bisreleased = true;
                 }
                 if (gamepad1.x) {
                     if (xisreleased) {
                         xisreleased = false;
                         values[index] -= 1;
                     }
                 } else {
                     xisreleased = true;
                 }
                 if (values[0] > 1) values[0] = 0;          //team color is boolean
                 if (values[0] < 0) values[0] = 1;          //team color is boolean


//                telemetry.addData("Edit: "+label[index], values[index]);

                telemetry.addLine(" ");
                telemetry.addLine(" Press Right Bumper to EXIT Edit Mode");

                //telemetry.update();

                 if (gamepad1.right_bumper) settingsdone = true;


            } else {

// done with menu print settings
                if (values[0] == 1) {
                    dim.setLED(BLUE_LED, true); // blue for true
                    dim.setLED(RED_LED, false);
                //    telemetry.addLine("     BLUE Alliance");
                } else {
                    dim.setLED(RED_LED, true); // Red for false
                    dim.setLED(BLUE_LED, false);
                //    telemetry.addLine("     RED Alliance");
                }

                if (gamepad1.left_bumper) settingsdone = false;
                telemetry.addLine(" ");
                telemetry.addLine("   READY TO RUN!");
            }

            telemetry.update();
            idle();
        }

        // Running now
        telemetry.addData(">", "Press X for Blue, B for Red.");
        telemetry.update();



        RdataLogger log1 = new RdataLogger("ricksfile.txt");
        log1.log(String.format("*********************************"));

/*        File f = new File("/sdcard/FIRST/RDataLog/ricksfile2.txt");
        RdataLogger log2 = new RdataLogger(f);
        log2.log(String.format("*********************************"));
*/
        String filename = "RicksFile.txt";
        telemetry.log().add("saved to '%s'", filename);

        DbgLog.msg("this is the log");




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



        telemetry.addData(">", "REeading File");


        char[] chars = new char[100];
        char[] chars2 = new char[100];
        int nlines = 0;

        try {
            FileReader fr = new FileReader("/sdcard/FIRST/RDataLog/ricksfile.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null) {
               nlines = nlines + 1 ;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        sleep(50000);
        telemetry.addData("Lines in file %d", nlines);
        telemetry.update();
        sleep(5000);

        // Turn off LEDs;
        dim.setLED(BLUE_LED, false);
        dim.setLED(RED_LED,  false);
        telemetry.addData("Lines in file %d", nlines);
        telemetry.addData(">", "Done");
        telemetry.update();

        sleep(50000);
    }
}
