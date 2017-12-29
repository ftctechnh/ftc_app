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
    public int      nitems = 15;    // easier to just include 0 as the first element so there are actually 5
    //public String[]  menulabel = {"waitTime","shooterWait","shooterForwardTime","shooterForwardAfterShoot","driveSpeed","whiteColor","driveBackTime"};
    public String  menulabel[] = new String[nitems];
    public int     menuvalue[] = new int[nitems];
    public int     menulowerlimit[] = new int[nitems];
    public int     menuupperlimit[] = new int[nitems];
    public String  menuvaluetoken[][] = new String[nitems][5];


    public void updateVariables() {
        teamIsRed = (menuvalue[0] == 1);
        startPositionIsFront = (menuvalue[1] == 1);
        mode = menuvalue[2];
        }

    public void initializeNTransferValues(Boolean init) {

        if (init) {
            menulabel[0] = "Team Color";
            menuvalue[0] = 1;
            menulowerlimit[0] = 0;
            menuupperlimit[0] = 1;
            menuvaluetoken[0][0] = "Blue";
            menuvaluetoken[0][1] = "Red";
        }
        teamIsRed = (menuvalue[0] == 1);

        if (init) {
            menulabel[1] = "Start Position";
            menuvalue[1] = 1;
            menulowerlimit[1] = 0;
            menuupperlimit[1] = 1;
            menuvaluetoken[1][0] = "Back";
            menuvaluetoken[1][1] = "Front";
        }
        startPositionIsFront = (menuvalue[1] == 1);

        if (init) {
            menulabel[2] = "Mode";
            menuvalue[2] = 0;
            menulowerlimit[2] = 0;
            menuupperlimit[2] = 3;
            menuvaluetoken[2][0] = "Glyph";
            menuvaluetoken[2][1] = "Relic";
            menuvaluetoken[2][2] = "Test";
            menuvaluetoken[2][3] = "Demo";
        }
        mode = menuvalue[2];

        if (init) {
            menulabel[3] = "Front BLUE Drive 1";
            menuvalue[3] = 13;
            menulowerlimit[3] = 0;
            menuupperlimit[3] = 100;
        }
        float FrontBlueDistance1 = menuvalue[3];

        if (init) {
            menulabel[4] = "Front BLUE Turn 1";
            menuvalue[4] = 0;
            menulowerlimit[4] = 80;
            menuupperlimit[4] = 100;
        }
        float FrontBlueTurn1 = menuvalue[4];

        if (init) {
            menulabel[5] = "Front BLUE Drive 2";
            menuvalue[5] = 15;
            menulowerlimit[5] = 0;
            menuupperlimit[5] = 100;
        }
        float FrontBlueDistance2 = menuvalue[5];

        if (init) {
            menulabel[6] = "Front RED Drive 1";
            menuvalue[6] = 16;
            menulowerlimit[6] = 0;
            menuupperlimit[6] = 100;
        }
        float FrontRedDistance1 = menuvalue[6];

        if (init) {
            menulabel[7] = "Front RED Turn 1";
            menuvalue[7] = 16;
            menulowerlimit[7] = 80;
            menuupperlimit[7] = 100;
        }
        float FrontRedTurn1 = menuvalue[7];

        if (init) {
            menulabel[8] = "Front RED Drive 2";
            menuvalue[8] = 16;
            menulowerlimit[8] = 0;
            menuupperlimit[8] = 100;
        }
        float FrontRedDistance2 = menuvalue[8];

        if (init) {
            menulabel[9] = "Back BLUE Drive 1";
            menuvalue[9] = 13;
            menulowerlimit[9] = 0;
            menuupperlimit[9] = 100;
        }
        float BackBlueDistance1 = menuvalue[9];

        if (init) {
            menulabel[10] = "Back BLUE Drive 2";
            menuvalue[10] = 0;
            menulowerlimit[10] = 80;
            menuupperlimit[10] = 100;
        }
        float BackBlueDistance2 = menuvalue[10];

        if (init) {
            menulabel[11] = "Back BLUE Drive 3";
            menuvalue[11] = 15;
            menulowerlimit[11] = 0;
            menuupperlimit[11] = 100;
        }
        float BackBlueDistance3 = menuvalue[11];

        if (init) {
            menulabel[12] = "Back RED Drive 1";
            menuvalue[12] = 13;
            menulowerlimit[12] = 0;
            menuupperlimit[12] = 100;
        }
        float BackRedDistance1 = menuvalue[12];

        if (init) {
            menulabel[13] = "Back RED Drive 2";
            menuvalue[13] = 0;
            menulowerlimit[13] = 80;
            menuupperlimit[13] = 100;
        }
        float BackRedDistance2 = menuvalue[13];

        if (init) {
            menulabel[14] = "Back RED Drive 3";
            menuvalue[14] = 15;
            menulowerlimit[14] = 0;
            menuupperlimit[14] = 100;
        }
        float BackRedDistance3 = menuvalue[14];

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

        menulabel[3] = "Front BLUE Drive 1";
        menuvalue[3] = 13;
        menulowerlimit[3] = 0;
        menuupperlimit[3] = 100;

        menulabel[4] = "Front BLUE Turn 1";
        menuvalue[4] = 0;
        menulowerlimit[4] = 80;
        menuupperlimit[4] = 100;

        menulabel[5] = "Front BLUE Drive 2";
        menuvalue[5] = 15;
        menulowerlimit[5] = 0;
        menuupperlimit[5] = 100;

        menulabel[6] = "Front RED Drive 1";
        menuvalue[6] = 16;
        menulowerlimit[6] = 0;
        menuupperlimit[6] = 100;

        menulabel[7] = "Front RED Turn 1";
        menuvalue[7] = 16;
        menulowerlimit[7] = 80;
        menuupperlimit[7] = 100;

        menulabel[8] = "Front RED Drive 2";
        menuvalue[8] = 16;
        menulowerlimit[8] = 0;
        menuupperlimit[8] = 100;

        menulabel[9] = "Back BLUE Drive 1";
        menuvalue[9] = 13;
        menulowerlimit[9] = 0;
        menuupperlimit[9] = 100;

        menulabel[10] = "Back BLUE Drive 2";
        menuvalue[10] = 0;
        menulowerlimit[10] = 80;
        menuupperlimit[10] = 100;

        menulabel[11] = "Back BLUE Drive 3";
        menuvalue[11] = 15;
        menulowerlimit[11] = 0;
        menuupperlimit[11] = 100;

        menulabel[12] = "Back RED Drive 1";
        menuvalue[12] = 13;
        menulowerlimit[12] = 0;
        menuupperlimit[12] = 100;

        menulabel[13] = "Back RED Drive 2";
        menuvalue[13] = 0;
        menulowerlimit[13] = 80;
        menuupperlimit[13] = 100;

        menulabel[14] = "Back RED Drive 3";
        menuvalue[14] = 15;
        menulowerlimit[14] = 0;
        menuupperlimit[14] = 100;

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

        initializeNTransferValues(false);   // transfer new values to variables
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