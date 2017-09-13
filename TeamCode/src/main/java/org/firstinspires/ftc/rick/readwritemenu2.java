package org.firstinspires.ftc.rick;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.Examples.DataLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Autonomous(name = "fwrtest2", group = "Ricks")
@Disabled

//@Disabled
public class readwritemenu2 extends LinearOpMode {

    static final int    BLUE_LED    = 0;     // Blue LED channel on DIM
    static final int    RED_LED     = 1;     // Red LED Channel on DIM

    public int      nitems = 4;    // easier to just include 0 as the first element!
    public String[] menulabel = {"Blue Team","Mode", "Delay", "Distance 1","Turn 1"};

    public int[]    menuvalue = {1,1,10,20,45};
    public int      programmode,secondstodelay,distancetowall,turn1;
    public boolean  TeamisBlue;


    //float[]  menuvaluef = {1,1,10,22}
    // Create timer to toggle LEDs
    public ElapsedTime runtime = new ElapsedTime();

    // Define class members
    public DeviceInterfaceModule   dim;

    private DataLogger ltDl; // Linear Time Data Logger


    @Override
    public void runOpMode() {

         dim = this.hardwareMap.deviceInterfaceModule.get("dim");

        RdataLogger log1 = new RdataLogger("ricksfile.txt");
        log1.log(String.format("*********************************"));

/*        File f = new File("/sdcard/FIRST/RDataLog/ricksfile2.txt");
        RdataLogger log2 = new RdataLogger(f);
        log2.log(String.format("*********************************"));
*/
        String filename = "RicksFile.txt";
        //telemetry.log().add("DataLog saved to '%s'", filename);

        DbgLog.msg("this is the log");
        // Toggle LEDs while Waiting for the start button


        // get menu values
        OurMenu("8045config.txt");


        while (!isStarted())  {

            telemetry.addLine("----------------------------READY TO RUN!--------------");

            if (menuvalue[0] == 1) {       //If Blue Highlight if index is 0
                 telemetry.addLine("BLUE Alliance");
            } else {                    //Otherwise must be Red Highlight if index is 0
                 telemetry.addLine("RED Alliance");
            }
//          loop to display the configured items  if the index matches add arrow.
            for ( int i=1; i<=nitems; i++){
                     telemetry.addLine().addData(menulabel[i], menuvalue[i]);
                }
            telemetry.addLine("----------------------------READY TO RUN!--------------");
            telemetry.update();
        }

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


//****************************************************************************************************************************************

    public void OurMenu(String fileName) {



        String directoryPath    = "/sdcard/FIRST/";
        String filePath         = directoryPath + fileName ;
        new File(directoryPath).mkdir();        // Make sure that the directory exists
        //logFile = new File(filePath);

        //menulabel = new String[];
        //menuvalue = new int[];

// read labels & values from file before we start the init loop
        //==========================================
        //String filePath    = "/sdcard/FIRST/8045_Config.txt";

        int i = 0;
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String s;
            while ((s = br.readLine()) != null  && i <= nitems){                // read the label then the value they are on separate lines
                menulabel[i] = s;                                   //Updates the label "string" of our array
                s = br.readLine();
                menuvalue[i] = Integer.parseInt(s);                //values is our integer array, converts string to integer
                //System.out.println(s);
                i += 1;                                         //Only to do with our array
            }
            fr.close();

        } catch (IOException ex) {
            System.err.println("Couldn't read this: "+filePath);//idk where this is printing
        }


//==========================================


//This while loop is the "editing" loop
        int index = 0;

        boolean yisreleased = true;
        boolean aisreleased = true;
        boolean xisreleased = true;
        boolean bisreleased = true;
        boolean rbumperisreleased = true;
        boolean rjoyisreleased = true;
        boolean editmode = false;

        while (!isStarted() && !gamepad1.right_stick_button ) {

            // update the values we want to use
            TeamisBlue = (menuvalue[0] != 0);
            programmode    = menuvalue[1];
            secondstodelay = menuvalue[2];
            distancetowall = menuvalue[3];
            turn1   = menuvalue[4];

            // output current settings to the screen  (1st token is red/blue treat it separately.
            if (menuvalue[0] == 1) {       //If Blue Highlight if index is 0
                if (index == 0 && editmode ){telemetry.addLine("BLUE Alliance  <=======");} else {telemetry.addLine("BLUE Alliance");}
            } else {                    //Otherwise must be Red Highlight if index is 0
                if (index == 0 && editmode ){telemetry.addLine("RED Alliance  <=======");} else {telemetry.addLine("RED Alliance");}
            }
//          loop to display the configured items  if the index matches add arrow.
            for ( i=1; i<=nitems; i++){
                //telemetry.addData("*",values[i]);
                if (i == index && editmode) {
                    telemetry.addLine().addData(menulabel[i], menuvalue[i]+"  <=======");

                }else{
                    telemetry.addLine().addData(menulabel[i], menuvalue[i]);
                }

            }
            telemetry.addLine("");

            // blink lights for red/blue  while in the menu method
            boolean even = (((int) (runtime.time()) & 0x01) == 0);             // Determine if we are on an odd or even second
            if ( TeamisBlue) {
                dim.setLED(BLUE_LED, even); // blue for true
                dim.setLED(RED_LED, false);
            } else {
                dim.setLED(RED_LED,  even); // Red for false
                dim.setLED(BLUE_LED, false);
            }

            //THIS IS EDIT MODE=============================================
            //          if we haven't pressed settings done bumper, look for button presses, blink the LED
            if (editmode) {



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
                        menuvalue[index] += 1;
                    }
                } else {
                    bisreleased = true;
                }
                if (gamepad1.x) {
                    if (xisreleased) {
                        xisreleased = false;
                        menuvalue[index] -= 1;
                    }
                } else {
                    xisreleased = true;
                }

                // limit the values of some buttons
                if (menuvalue[0] > 1) menuvalue[0] = 0;          //team color is boolean
                if (menuvalue[0] < 0) menuvalue[0] = 1;          //team color is boolean

                //telemetry.addLine(" ");
                telemetry.addLine(" EDIT MODE: Right Bumper to EXIT");


                // check to exit the edit mode
                if (gamepad1.right_bumper) {
                    if (rbumperisreleased) {
                        editmode = false;
                        rbumperisreleased = false;
                    }
                }
                rbumperisreleased = true;
            }
            //END OF EDIT MODE===================================================
            else {

//          in ready mode check if you'd like to return to editing, add telemetry line.

                if (gamepad1.right_bumper) {
                    if (rbumperisreleased) {
                    editmode = true;
                    rbumperisreleased = false;
                    }
                }
                rbumperisreleased = true;


                telemetry.addLine("SAVE MODE: Right Bumper to Edit ");
                telemetry.addLine("Joystick to SAVE Setup ");

            }

            telemetry.update();
            idle();
        }


        // Init loop finished, move to running now  First write the config file
//==========================================
        try {
            FileWriter fw = new FileWriter(filePath);
            for ( i=0; i<=nitems; i++){
                fw.write((menulabel[i]));
                fw.write(System.lineSeparator());
                fw.write(Integer.toString(menuvalue[i]));
                fw.write(System.lineSeparator());
            }
//            fw.write(values);
//            fw.write(System.lineSeparator());
            fw.close();

        } catch (IOException ex) {
            System.err.println("Couldn't log this: "+filePath);
        }
        telemetry.addData("ConfigFile saved to", filePath);
        telemetry.update();
        sleep(500);
//==========================================


    }


}
