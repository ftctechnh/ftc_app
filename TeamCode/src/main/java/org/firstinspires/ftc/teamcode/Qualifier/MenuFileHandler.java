package org.firstinspires.ftc.teamcode.Qualifier;

import android.content.Context;
import android.graphics.Color;

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
    public float FrontBlueDistance1,FrontBlueHeading1,FrontBlueTurn1,FrontBlueDistance2,FrontBlueHeading2;
    public float FrontRedDistance1,FrontRedHeading1,FrontRedTurn1,FrontRedDistance2,FrontRedHeading2;
    public float BackBlueDistance1,BackBlueHeading1,BackBlueDistance2,BackBlueHeading2,BackBlueDistance3,BackBlueHeading3 ;
    public float BackRedDistance1,BackRedHeading1,BackRedDistance2,BackRedHeading2,BackRedDistance3,BackRedHeading3 ;



    //    //Menu Variables
    public int      nitems = 25;    //
    //public String[]  menulabel = {"waitTime","shooterWait","shooterForwardTime","shooterForwardAfterShoot","driveSpeed","whiteColor","driveBackTime"};
    public String  menulabel[] = new String[nitems];
    public int     menuvalue[] = new int[nitems];
    public int     menulowerlimit[] = new int[nitems];
    public int     menuupperlimit[] = new int[nitems];
    public String  menuvaluetoken[][] = new String[nitems][5];



    public void initArrays() {

        for (int i = 0; i<nitems; i++) {
        menulabel[i] = "";
        menuvalue[i] = 0;
        menulowerlimit[i] = 0;
        menuupperlimit[i] = 100;
            menuvaluetoken[i][0] = "";
            menuvaluetoken[i][1] = "";
            menuvaluetoken[i][2] = "";
            menuvaluetoken[i][3] = "";
            menuvaluetoken[i][4] = "";
    }

        }

    public void initializeNTransferValues(Boolean init) {

        if (init) {
            menulabel[0] = "Team Color";
            menuvalue[0] = 0;
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
            menuvalue[3] = -6;
            menulowerlimit[3] = -20;
            menuupperlimit[3] =  20;
        }
        FrontBlueDistance1 = menuvalue[3];

        if (init) {
            menulabel[4] = "Front BLUE Heading 1";
            menuvalue[4] = 0;
            menulowerlimit[4] = -100;
            menuupperlimit[4] = 100;
        }
        FrontBlueHeading1 = menuvalue[4];

        if (init) {
            menulabel[5] = "Front BLUE Turn 1";
            menuvalue[5] = 90;
            menulowerlimit[5] = -180;
            menuupperlimit[5] = 180;
        }
        FrontBlueTurn1 = menuvalue[5];

        if (init) {
            menulabel[6] = "Front BLUE Drive 2";
            menuvalue[6] = -5;
            menulowerlimit[6] = -20;
            menuupperlimit[6] =  20;
        }
        FrontBlueDistance2 = menuvalue[6];

        if (init) {
            menulabel[7] = "Front BLUE Heading 2";
            menuvalue[7] = 90;
            menulowerlimit[7] = -180;
            menuupperlimit[7] = 180;
        }
        FrontBlueHeading2 = menuvalue[7];

        if (init) {
            menulabel[8] = "Front RED Drive 1";
            menuvalue[8] = 6;
            menulowerlimit[8] = -20;
            menuupperlimit[8] =  20;
        }
       FrontRedDistance1 = menuvalue[8];

        if (init) {
            menulabel[9] = "Front RED Heading 1";
            menuvalue[9] = 0;
            menulowerlimit[9] = -180;
            menuupperlimit[9] =  180;
        }
       FrontRedHeading1 = menuvalue[9];

        if (init) {
            menulabel[10] = "Front RED Turn 1";
            menuvalue[10] = -90;
            menulowerlimit[10] = -180;
            menuupperlimit[10] =  180;
        }
        FrontRedTurn1 = menuvalue[10];

        if (init) {
            menulabel[11] = "Front RED Drive 2";
            menuvalue[11] = 6;
            menulowerlimit[11] = 0;
            menuupperlimit[11] = 20;
        }
        FrontRedDistance2 = menuvalue[11];

        if (init) {
            menulabel[12] = "Front RED Heading 2";
            menuvalue[12] = -90;
            menulowerlimit[12] = -180;
            menuupperlimit[12] =  180;
        }
       FrontRedHeading2 = menuvalue[12];

        if (init) {
            menulabel[13] = "Back BLUE Drive 1";
            menuvalue[13] = -5;
            menulowerlimit[13] = -20;
            menuupperlimit[13] =  20;
        }
       BackBlueDistance1 = menuvalue[13];

        if (init) {
            menulabel[14] = "Back BLUE Heading 1";
            menuvalue[14] = 0;
            menulowerlimit[14] = -180;
            menuupperlimit[14] =  180;
        }
        BackBlueHeading1 = menuvalue[14];

        if (init) {
            menulabel[15] = "Back BLUE Drive 2";
            menuvalue[15] = 5;
            menulowerlimit[15] = -20;
            menuupperlimit[15] =  20;
        }
        BackBlueDistance2 = menuvalue[15];

        if (init) {
            menulabel[16] = "Back BLUE Heading 2";
            menuvalue[16] = 0;
            menulowerlimit[16] = -180;
            menuupperlimit[16] =  180;
        }
        BackBlueHeading2 = menuvalue[16];

        if (init) {
            menulabel[17] = "Back BLUE Drive 3";
            menuvalue[17] = -5;
            menulowerlimit[17] = -20;
            menuupperlimit[17] =  20;
        }
        BackBlueDistance3 = menuvalue[17];

        if (init) {
            menulabel[18] = "Back BLUE Heading 3";
            menuvalue[18] = 0;
            menulowerlimit[18] = -180;
            menuupperlimit[18] =  180;
        }
        BackBlueHeading3 = menuvalue[18];

        if (init) {
            menulabel[19] = "Back RED Drive 1";
            menuvalue[19] = -5;
            menulowerlimit[19] = -20;
            menuupperlimit[19] =  20;
        }
        BackRedDistance1 = menuvalue[19];

        if (init) {
            menulabel[20] = "Back RED Heading 1";
            menuvalue[20] = 0;
            menulowerlimit[20] = -180;
            menuupperlimit[20] =  180;
        }
        BackRedHeading1 = menuvalue[20];

        if (init) {
            menulabel[21] = "Back RED Drive 2";
            menuvalue[21] = 5;
            menulowerlimit[21] = -20;
            menuupperlimit[21] =  20;
        }
        BackRedDistance2 = menuvalue[21];

        if (init) {
            menulabel[22] = "Back RED Heading 2";
            menuvalue[22] = 0;
            menulowerlimit[22] = -180;
            menuupperlimit[22] =  180;
        }
        BackRedHeading2 = menuvalue[22];

        if (init) {
            menulabel[23] = "Back RED Drive 3";
            menuvalue[23] = -5;
            menulowerlimit[23] = -20;
            menuupperlimit[23] =  20;
        }
        BackRedDistance3 = menuvalue[23];

        if (init) {
            menulabel[24] = "Back RED Heading 3";
            menuvalue[24] = 0;
            menulowerlimit[24] = -180;
            menuupperlimit[24] =  180;
        }
        BackRedHeading3 = menuvalue[24];


    }


//    public void initializeValues() {
//
//        menulabel[0] = "Team Color";
//        menuvalue[0] = 1;
//        menulowerlimit[0] = 0;
//        menuupperlimit[0] = 1;
//        menuvaluetoken[0][0] = "Blue";
//        menuvaluetoken[0][1] = "Red";
//        teamIsRed = (menuvalue[0] == 1);
//
//        menulabel[1] = "Start Position";
//        menuvalue[1] = 1;
//        menulowerlimit[1] = 0;
//        menuupperlimit[1] = 1;
//        menuvaluetoken[1][0] = "Back";
//        menuvaluetoken[1][1] = "Front";
//        startPositionIsFront = (menuvalue[1] == 1);
//
//        menulabel[2] = "Mode";
//        menuvalue[2] = 0;
//        menulowerlimit[2] = 0;
//        menuupperlimit[2] = 3;
//        menuvaluetoken[2][0] = "Glyph";
//        menuvaluetoken[2][1] = "Relic";
//        menuvaluetoken[2][2] = "Test";
//        menuvaluetoken[2][3] = "Demo";
//        mode = menuvalue[2];
//
//        menulabel[3] = "Front BLUE Drive 1";
//        menuvalue[3] = 6;
//        menulowerlimit[3] = 0;
//        menuupperlimit[3] = 20;
//
//        menulabel[4] = "Front BLUE Heading 1";
//        menuvalue[4] = 0;
//        menulowerlimit[4] = -100;
//        menuupperlimit[4] = 100;
//
//        menulabel[5] = "Front BLUE Turn 1";
//        menuvalue[5] = 90;
//        menulowerlimit[5] = 80;
//        menuupperlimit[5] = 100;
//
//        menulabel[6] = "Front BLUE Drive 2";
//        menuvalue[6] = 5;
//        menulowerlimit[6] = 0;
//        menuupperlimit[6] = 20;
//
//        menulabel[7] = "Front BLUE Heading 2";
//        menuvalue[7] = 0;
//        menulowerlimit[7] = -100;
//        menuupperlimit[7] = 100;
//
//        menulabel[8] = "Front RED Drive 1";
//        menuvalue[8] = 6;
//        menulowerlimit[8] = 0;
//        menuupperlimit[8] = 20;
//
//        menulabel[9] = "Front RED Heading 1";
//        menuvalue[9] = 0;
//        menulowerlimit[9] = -100;
//        menuupperlimit[9] = 100;
//
//        menulabel[10] = "Front RED Turn 1";
//        menuvalue[10] = -90;
//        menulowerlimit[10] = -100;
//        menuupperlimit[10] =  -80;
//
//        menulabel[11] = "Front RED Drive 2";
//        menuvalue[11] = 6;
//        menulowerlimit[11] = 0;
//        menuupperlimit[11] = 20;
//
//        menulabel[12] = "Front RED Heading 2";
//        menuvalue[12] =  0;
//        menulowerlimit[12] = 0;
//        menuupperlimit[12] = 100;
//
//        menulabel[13] = "Back BLUE Drive 1";
//        menuvalue[13] = 5;
//        menulowerlimit[13] = 0;
//        menuupperlimit[13] = 2;
//
//        menulabel[14] = "Back BLUE Heading 1";
//        menuvalue[14] = 0;
//        menulowerlimit[14] = -100;
//        menuupperlimit[14] =  100;
//
//        menulabel[15] = "Back BLUE Drive 2";
//        menuvalue[15] = 0;
//        menulowerlimit[15] = 80;
//        menuupperlimit[15] = 100;
//
//        menulabel[16] = "Back BLUE Heading 2";
//        menuvalue[16] = 0;
//        menulowerlimit[16] =-100;
//        menuupperlimit[16] = 100;
//
//        menulabel[17] = "Back BLUE Drive 3";
//        menuvalue[17] = 5;
//        menulowerlimit[17] = 0;
//        menuupperlimit[17] = 2;
//
//        menulabel[18] = "Back BLUE Heading 3";
//        menuvalue[18] = 0;
//        menulowerlimit[18] = -100;
//        menuupperlimit[18] =  100;
//
//        menulabel[19] = "Back RED Drive 1";
//        menuvalue[19] = 5;
//        menulowerlimit[19] = 0;
//        menuupperlimit[19] = 20;
//
//        menulabel[20] = "Back RED Heading 1";
//        menuvalue[20] = 0;
//        menulowerlimit[20] = -100;
//        menuupperlimit[20] =  100;
//
//        menulabel[21] = "Back RED Drive 2";
//        menuvalue[21] = 5;
//        menulowerlimit[21] =  0;
//        menuupperlimit[21] = 20;
//
//        menulabel[22] = "Back RED Heading 2";
//        menuvalue[22] = 0;
//        menulowerlimit[22] = -100;
//        menuupperlimit[22] =  100;
//
//        menulabel[23] = "Back RED Drive 3";
//        menuvalue[23] =  5;
//        menulowerlimit[23] =  0;
//        menuupperlimit[23] = 20;
//
//        menulabel[24] = "Back RED Heading 3";
//        menuvalue[24] = 0;
//        menulowerlimit[24] = -100;
//        menuupperlimit[24] =  100;
//
//    }


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
//            initializeNTransferValues();Values();
        }

    }

    public void editParameters() {
        int index = 0;
        boolean aisreleased = true, yisreleased = true, bisreleased = true, xisreleased = true;


        while ( !gamepad.right_stick_button) {
            telemetry.addLine("===> Press Right Joystick to exit EDIT mode <===" );
            telemetry.addLine("####    " + menuvaluetoken[0][menuvalue[0]]+" " +menuvaluetoken[1][menuvalue[1]]+" " +menuvaluetoken[2][menuvalue[2]] +"   ####");
//            telemetry.addLine("#######    " + menuvaluetoken[0][menuvalue[0]] +"   " +  menuvaluetoken[1][menuvalue[1]] + "  " +  menuvaluetoken[3][menuvalue[3]] +"    ######");
            telemetry.addLine("");
            for ( int i=0; i < menulabel.length; i++){                     // write out the list of variables
                if ( i == index ) {                                        // if the index, add an arrow
                    if (menuvaluetoken[i][1] != "" ) {                           // menu items that need tokens should be less than 5
//                    if (menuupperlimit[i] < 5) {                           // menu items that need tokens should be less than 5
                    telemetry.addLine().addData(menulabel[i], menuvalue[i] + "  " + menuvaluetoken[i][menuvalue[i]] + " <----" );
                    }else{
                    telemetry.addLine().addData(menulabel[i], menuvalue[i] + " <----");
                    }
                }else{
                    if (menuvaluetoken[i][1] != "" ) {                           // menu items that need tokens should be less than 5
//                        if (menuupperlimit[i] < 5 ) {                           // menu items that need tokens should be less than 5
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

            telemetry.addLine("===> Press Right Joystick to exit EDIT mode <===" );
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