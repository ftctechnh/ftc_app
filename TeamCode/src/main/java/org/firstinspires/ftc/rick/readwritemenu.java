package org.firstinspires.ftc.rick;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.Examples.DataLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Autonomous(name = "fwrtest", group = "Ricks")
@Disabled

//@Disabled
public class readwritemenu extends LinearOpMode {

    static final int    BLUE_LED    = 0;     // Blue LED channel on DIM
    static final int    RED_LED     = 1;     // Red LED Channel on DIM

    // Create timer to toggle LEDs
    private ElapsedTime runtime = new ElapsedTime();

    // Define class members
    DeviceInterfaceModule   dim;

    private DataLogger ltDl; // Linear Time Data Logger


    @Override
    public void runOpMode() {

         dim = this.hardwareMap.deviceInterfaceModule.get("dim");

        // Toggle LEDs while Waiting for the start button
        telemetry.addData(">", "Press Play to test LEDs.");
        telemetry.update();

        int rtest;
        int nitems = 0;
        int index = 0;
        boolean yisreleased = true;
        boolean aisreleased = true;
        boolean xisreleased = true;
        boolean bisreleased = true;
        boolean settingsdone = true;
//These label strings are overwritten below from the file
        String[] label = {"Blue Team", "Delay", "Speed", "Mode"};
        int[] values = {1,20,30,40};

       // String[] label2 = new String[4];


        // read labels & values from file before we start the init loop
        //==========================================
        String filePath    = "/sdcard/FIRST/8045_Config.txt";
        int j = 0;

        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String s;
            while ((s = br.readLine()) != null){                // read the label then the value they are on separate lines
                label[j] = s;                                   //Updates the label "string" of our array
                s = br.readLine();
                values[j] = Integer.parseInt(s);                //values is our integer array, converts string to integer
                //System.out.println(s);
                j += 1;                                         //Only to do with our array
            }
            fr.close();

        } catch (IOException ex) {
            System.err.println("Couldn't read this: "+filePath);//idk where this is printing
        }
        if (j <=3) nitems=3; else nitems=j;

//==========================================
//This while loop is the "init" loop

        while (!isStarted()) {
            // output current settings to the screen  (1st token is red/blue treat it separately.
            if (values[0] == 1) {       //If Blue Highlight if index is 0
                if (index == 0 ){telemetry.addLine("BLUE Alliance <===");} else {telemetry.addLine("BLUE Alliance");}
            } else {                    //Otherwise must be Red Highlight if index is 0
                if (index == 0 ){telemetry.addLine("RED Alliance <===");} else {telemetry.addLine("RED Alliance");}
            }
//          loop to display the configured items  if the index matches add arrow.
            for (int i=1; i<=nitems-1; i++){
                //telemetry.addData("*",values[i]);
                if (i == index) {
                    telemetry.addLine().addData(label[i], values[i]+"  <====");

                }else{
                    telemetry.addLine().addData(label[i], values[i]);

                }
            }
            telemetry.addLine("");
            //THIS IS EDIT MODE=============================================
            //          if we haven't pressed settings done bumper, look for button presses, blink the LED
            if (!settingsdone) {

            boolean even = (((int) (runtime.time()) & 0x01) == 0);             // Determine if we are on an odd or even second
                if ( values[0] == 1) {
                dim.setLED(BLUE_LED, even); // blue for true
                dim.setLED(RED_LED, false);
            } else {
                dim.setLED(RED_LED,  even); // Red for false
                dim.setLED(BLUE_LED, false);
            }

            //menu buttons,  Y,A increase/decrease the index   X,B increase/decrease the value  (debounce logic )
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

                 // limit the values of some buttons
                 if (values[0] > 1) values[0] = 0;          //team color is boolean
                 if (values[0] < 0) values[0] = 1;          //team color is boolean

                telemetry.addLine(" ");
                telemetry.addLine(" Press Right Bumper to EXIT Edit Mode");


                // check to exit the edit mode
                if (gamepad1.right_bumper) settingsdone = true;


            }
            //END OF EDIT MODE===================================================
            else {

//          in ready mode check if you'd like to return to editing, add telemetry line.
            if (gamepad1.left_bumper) settingsdone = false;
            telemetry.addLine(" ");
            telemetry.addLine("   READY TO RUN!");
            }

            telemetry.update();
            idle();
        }

        // Init loop finished, move to running now  First write the config file
//==========================================
        try {
            FileWriter fw = new FileWriter(filePath);
            for (int i=0; i<=nitems; i++){
                fw.write((label[i]));
                fw.write(System.lineSeparator());
                fw.write(Integer.toString(values[i]));
                fw.write(System.lineSeparator());
            }
//            fw.write(values);
//            fw.write(System.lineSeparator());
            fw.close();

        } catch (IOException ex) {
            System.err.println("Couldn't log this: "+filePath);
        }
        telemetry.log().add("ConfigFile saved to '%s'", filePath);
//==========================================
        telemetry.addData(">", "Press X for Blue, B for Red.");
        telemetry.update();


        RdataLogger log1 = new RdataLogger("ricksfile.txt");
        log1.log(String.format("*********************************"));

/*        File f = new File("/sdcard/FIRST/RDataLog/ricksfile2.txt");
        RdataLogger log2 = new RdataLogger(f);
        log2.log(String.format("*********************************"));
*/
        String filename = "RicksFile.txt";
        telemetry.log().add("DataLog saved to '%s'", filename);

        DbgLog.msg("this is the log");




        boolean redlight = true;
        boolean bluelight = true;
        boolean buttonXIsReleased = true;
        boolean buttonBIsReleased = true;

        // Now just use red and blue buttons to set red and blue LEDs  Add logfile and debug log examples
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

        telemetry.update();

        // Turn off LEDs;
        dim.setLED(BLUE_LED, false);
        dim.setLED(RED_LED,  false);

        telemetry.addData(">", "Done");
        telemetry.update();

    }
}
