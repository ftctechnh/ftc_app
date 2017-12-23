package org.firstinspires.ftc.teamcode.Qualifier;

import android.content.Context;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Menu parameters are stored in a text file on the robot phone as viewable txt
 * To add additiona entries:
 *  you must inccreas the array sizes (nitems variable)
 *  you must add a 'understandable' variable name to equate the new matrix entry too
 *  set the initial parameters for the new variable  (initializeValues method)
 *  set the transfer to the 'usable' name  (updateVariables method)
 *
 */
public class MenuFileHandler {
    private Gamepad gamepad;


    Telemetry telemetry;
    public MenuFileHandler(Telemetry telemetry, Gamepad g){
        this.telemetry = telemetry;
        gamepad = g;
        }

    // variables used during the configuration process
    private String configFileName = "8045Relic.txt";
    private String directoryPath = "/sdcard/FIRST/";
    private String filePath = directoryPath + configFileName;


    // all data here
    public boolean teamIsRed;
    public boolean startPositionIsFront;
    public Integer mode,distance1,heading1,distance2,heading2;


//    //Menu Variables
    public int      nitems = 7;    // easier to just include 0 as the first element so there are actually 5
    //public String[]  menulabel = {"waitTime","shooterWait","shooterForwardTime","shooterForwardAfterShoot","driveSpeed","whiteColor","driveBackTime"};
    public String  menulabel[] = new String[nitems];
    public int     menuvalue[] = new int[nitems];
    public int     menulowerlimit[] = new int[nitems];
    public int     menuupperlimit[] = new int[nitems];
    public String  menuvaluetoken[][] = new String[nitems][5];


//    public int[]     menuvalue  = {0,1,2,3,4,5,6};
//    public String[] teamname = {"Red", "Blue"};
//    public String[] modename = {"Beacon Route","Judging Mode","Beacon Route SUPER Mode"," Simple Capball","Simple Ramp", "Defense Mode","Super Defense Mode"};
//    public int      programmode,secondstodelay,particles,heading0,distance0,heading1,distance1,heading2,distance2,heading3,distance3,heading4,distance4,heading5,distance5,rightaveragecolor,leftaveragecolor, supermodeturnblue,supermodeturnred;
//    public boolean  TeamisBlue,PickUpPartnerBall, hitcapball;


    public void updateVariables() {
        teamIsRed = (menuvalue[0] == 1);
        startPositionIsFront = (menuvalue[1] == 1);
        mode = menuvalue[2];
        distance1 = menuvalue[3];
        heading1  = menuvalue[4];
        distance2 = menuvalue[5];
        heading2  = menuvalue[6];
    }
    public void initializeValues() {

        menulabel[0] = "Team Color";
        menuvalue[0] = 1;
        menulowerlimit[0] = 0;
        menuupperlimit[0] = 1;
        menuvaluetoken[0][0] = "Blue";
        menuvaluetoken[0][1] = "Red";
        teamIsRed = (menuvalue[0] == 1);

        menulabel[1] = "Start Position";
        menuvalue[1] = 1;
        menulowerlimit[1] = 0;
        menuupperlimit[1] = 1;
        menuvaluetoken[1][0] = "Back";
        menuvaluetoken[1][1] = "Front";
        startPositionIsFront = (menuvalue[1] == 1);

        menulabel[2] = "Mode";
        menuvalue[2] = 0;
        menulowerlimit[2] = 0;
        menuupperlimit[2] = 3;
        menuvaluetoken[2][0] = "Glyph";
        menuvaluetoken[2][1] = "Relic";
        menuvaluetoken[2][2] = "Test";
        menuvaluetoken[2][3] = "Demo";
        mode = menuvalue[2];

        menulabel[3] = "Drive1";
        menuvalue[3] = 13;
        menulowerlimit[3] = 0;
        menuupperlimit[3] = 100;

        menulabel[4] = "heading1";
        menuvalue[4] = 0;
        menulowerlimit[4] = -180;
        menuupperlimit[4] = 180;

        menulabel[5] = "Drive2";
        menuvalue[5] = 15;
        menulowerlimit[5] = 0;
        menuupperlimit[5] = 100;

        menulabel[6] = "test6";
        menuvalue[6] = 16;
        menulowerlimit[6] = 0;
        menuupperlimit[6] = 100;
    }


    public void readDataFromTxtFile(Context context) {
        // setup initial configuration parameters here
        //initializeValues();

        telemetry.addLine("Reading from file method");
        telemetry.update();

        int i = 0;
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String s;
            while ((s = br.readLine()) != null && i < menuvalue.length) {                // read the label then the value they are on separate lines
                menulabel[i] = s;                                   //Updates the label "string" of our array
                //s = br.readLine();
                menuvalue[i] = Integer.parseInt(br.readLine());                //values is our integer array, converts string to integer

                i += 1;                                         //Only to do with our array
            }

            fr.close();                                            // close the file

        } catch (IOException ex) {
            System.err.println("Couldn't read this: " + filePath);//idk where this is printing
            initializeValues();
        }

    }

    public void editParameters() {
        int index = 0;
        boolean aisreleased = true, yisreleased = true, bisreleased = true, xisreleased = true;

        while ( !gamepad.right_stick_button ) {
            telemetry.addLine("===> Press Joystick to exit MENU mode <===" );
            telemetry.addLine("####    " + menuvaluetoken[0][menuvalue[0]]+" " +menuvaluetoken[1][menuvalue[1]]+" " +menuvaluetoken[2][menuvalue[2]] +"   ####");
//            telemetry.addLine("#######    " + menuvaluetoken[0][menuvalue[0]] +"   " +  menuvaluetoken[1][menuvalue[1]] + "  " +  menuvaluetoken[3][menuvalue[3]] +"    ######");
            telemetry.addLine("");
            for ( int i=0; i < menulabel.length; i++){                     // write out the list of variables
                if ( i == index ) {                                        // if the index, add an arrow
                    if (menuupperlimit[i] < 5) {                           // menu items that need tokens should be less than 5
                    telemetry.addLine().addData(menulabel[i], menuvalue[i] + "  " + menuvaluetoken[i][menuvalue[i]] + " <----" );
                    }else{
                    telemetry.addLine().addData(menulabel[i], menuvalue[i] + " <----");
                    }
                }else{
                    if (menuupperlimit[i] < 5) {                           // menu items that need tokens should be less than 5
                        telemetry.addLine().addData(menulabel[i], menuvalue[i] + "  " + menuvaluetoken[i][menuvalue[i]] );
                    }else{
                        telemetry.addLine().addData(menulabel[i], menuvalue[i]);
                    }
                }
            }
            if (gamepad.a) {                             // adjust the index for a & Y when button is released...
                if (aisreleased) {
                    aisreleased = false;
                    index += 1;
                }
            } else {
                aisreleased = true;
            }
            if (gamepad.y) {
                if (yisreleased) {
                    yisreleased = false;
                    index -= 1;
                }
            } else {
                yisreleased = true;
            }
            if (index >= menuvalue.length) index = 0;          // limit the bounds of index
            if (index < 0) index = menuvalue.length - 1 ;

            // increase or decrease the values
            if (gamepad.b) {
                if (bisreleased) {
                    bisreleased = false;
                    menuvalue[index] += 1;
                }
            } else {
                bisreleased = true;
            }
            if (gamepad.x) {
                if (xisreleased) {
                    xisreleased = false;
                    menuvalue[index] -= 1;
                }
            } else {
                xisreleased = true;
            }

            // limit the values by the upper & lower limits:
            if (menuvalue[index] >  menuupperlimit[index]) menuvalue[index] = menulowerlimit[index];
            if (menuvalue[index] <  menulowerlimit[index]) menuvalue[index] = menuupperlimit[index];

            telemetry.addLine("===> Press Joystick to exit EDIT mode <===" );
            telemetry.update();
        }
        telemetry.clear();
        telemetry.addLine("  *** Finished editing  ***");
        telemetry.update();
    }


    public boolean writeDataToTxtFile(Context context) {
        // may want to write configuration parameters to a file here if they are needed for teleop too!
//        String fileName = "test.txt";
//        String directoryPath = "/sdcard/FIRST/";
//        String filePath = directoryPath + fileName;
        new File(directoryPath).mkdir();        // Make sure that the directory exists

        int i=0;
        try {
            FileWriter fw = new FileWriter(filePath);
            for ( i=0; i < menulabel.length; i++){
//                for ( i=0; i <= menuvalue.length-1; i++){
                fw.write((menulabel[i] + "\n"));
//                fw.write(System.lineSeparator());
                fw.write((menuvalue[i] + "\n"));
//                fw.write(System.lineSeparator());

//            fw.write(Integer.toString(waitTime) + "\n");
//            fw.write(Double.toString(shooterWait) + "\n");
//            fw.write(Double.toString(shooterForwardTime) + "\n");
//            fw.write(Double.toString(shooterForwardAfterShoot) + "\n");
//            fw.write(Double.toString(driveSpeed) + "\n");
//            fw.write(Double.toString(whiteColor) + "\n");
//            fw.write(Double.toString(driveBackTime) + "\n");

            //            fw.close();

            }
//            fw.write(values);
//            fw.write(System.lineSeparator());
            fw.close();
            return true;

        } catch (IOException ex) {
            System.err.println("Couldn't write this file: "+filePath);
            return false;
        }
        //telemetry.addData("ConfigFile saved to", filePath);
        //telemetry.update();
        //sleep(500);

    }


}