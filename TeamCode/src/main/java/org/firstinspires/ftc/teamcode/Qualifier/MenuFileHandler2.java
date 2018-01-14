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
public class MenuFileHandler2 {
    private Gamepad gamepad;



    Telemetry telemetry;
    public MenuFileHandler2(Telemetry telemetry, Gamepad g){
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
    public double DriveSpeed;

    public double mode,distance1,distance2;

    public double BlueFrontDistance1,BlueFrontHeading1,BlueFrontTurn1,BlueFrontDistance2,BlueFrontHeading2;
    public double RedFrontDistance1,RedFrontHeading1,RedFrontTurn1,RedFrontDistance2,RedFrontHeading2;
    public double BlueBackDistance1,BlueBackHeading1,BlueBackDistance2,BlueBackHeading2,BlueBackTurn3,BlueBackDistance3,BlueBackHeading3 ;
    public double RedBackDistance1,RedBackHeading1,RedBackDistance2,RedBackHeading2,RedBackDistance3,RedBackHeading3 ;



    //    //Menu Variables
    public int      nitems = 27;    //
    //public String[]  menulabel = {"waitTime","shooterWait","shooterForwardTime","shooterForwardAfterShoot","driveSpeed","whiteColor","driveBackTime"};
    public String       menulabel[] = new String[nitems];
    public double       menuvalue[] = new double[nitems];
    public double  menulowerlimit[] = new double[nitems];
    public double  menuupperlimit[] = new double[nitems];
    public double   menuincrement[] = new double[nitems];
    public String  menuvaluetoken[][] = new String[nitems][5];



    public void initArrays() {

        for (int i = 0; i<nitems; i++) {
        menulabel[i] = "";
        menuvalue[i] = 0;
        menulowerlimit[i] = 0;
        menuupperlimit[i] = 100;
        menuincrement[i] = 1;
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
            menuvalue[0] = 1;
            menulowerlimit[0] = 0;
            menuupperlimit[0] = 1;
            menuincrement[0] = 1;
            menuvaluetoken[0][0] = "Blue";
            menuvaluetoken[0][1] = "Red";
        }
        teamIsRed = (menuvalue[0] == 1);

        if (init) {
            menulabel[1] = "Start Position";
            menuvalue[1] = 1;
            menulowerlimit[1] = 0;
            menuupperlimit[1] = 1;
            menuincrement[1] = 1;
            menuvaluetoken[1][0] = "Back";
            menuvaluetoken[1][1] = "Front";
        }
        startPositionIsFront = (menuvalue[1] == 1);

        if (init) {
            menulabel[2] = "Mode";
            menuvalue[2] = 0;
            menulowerlimit[2] = 0;
            menuupperlimit[2] = 3;
            menuincrement[2] = 1;
            menuvaluetoken[2][0] = "Glyph";
            menuvaluetoken[2][1] = "Relic";
            menuvaluetoken[2][2] = "Demo ";
            menuvaluetoken[2][3] = "Test ";
        }
        mode = menuvalue[2];

        if (init) {
            menulabel[3] = "Drive Speed  ";
            menuvalue[3] = 0.5;
            menulowerlimit[3] =  0;
            menuupperlimit[3] = 2;
            menuincrement[3] = 0.05;
        }
        DriveSpeed = menuvalue[3];

        if (init) {
            menulabel[4] = "RED Front Distance 1";
            menuvalue[4] = -34;
            menulowerlimit[4] = -50;
            menuupperlimit[4] =  50;
            menuincrement[4] = 0.5;
        }
        RedFrontDistance1 = menuvalue[4];

        if (init) {
            menulabel[5] = "RED Front Heading 1";
            menuvalue[5] = 0;
            menulowerlimit[5] = -180;
            menuupperlimit[5] =  180;
            menuincrement[5] = 1.0;
        }
        RedFrontHeading1 = menuvalue[5];

        if (init) {
            menulabel[6] = "RED Front Turn    1";
            menuvalue[6] = -90;
            menulowerlimit[6] = -180;
            menuupperlimit[6] =  180;
            menuincrement[6] = 0.5;
        }
        RedFrontTurn1 = menuvalue[6];

        if (init) {
            menulabel[7] = "RED Front Distance 2";
            menuvalue[7] = -9;
            menulowerlimit[7] = -50;
            menuupperlimit[7] =  50;
            menuincrement[7] = 0.5;
        }
        RedFrontDistance2 = menuvalue[7];

        if (init) {
            menulabel[8] = "RED Front Heading 2";
            menuvalue[8] = -90;
            menulowerlimit[8] = -180;
            menuupperlimit[8] =  180;
            menuincrement[8] = 1.0;
        }
        RedFrontHeading2 = menuvalue[8];   //fb 3-7

        if (init) {
            menulabel[9] = "BLUE Front Distance 1";
            menuvalue[9] = 34;
            menulowerlimit[9] = -50;
            menuupperlimit[9] =  50;
            menuincrement[9] = 0.5;
        }
        BlueFrontDistance1 = menuvalue[9];

        if (init) {
            menulabel[10] = "BLUE Front Heading 1";
            menuvalue[10] = 0;
            menulowerlimit[10] = -180;
            menuupperlimit[10] =  180;
            menuincrement[10] = 1.0;
        }
       BlueFrontHeading1 = menuvalue[10];

        if (init) {
            menulabel[11] = "BLUE Front Turn    1";
            menuvalue[11] = -90;
            menulowerlimit[11] = -180;
            menuupperlimit[11] =  180;
            menuincrement[11] = 0.5;
        }
        BlueFrontTurn1 = menuvalue[11];

        if (init) {
            menulabel[12] = "BLUE Front Distance 2";
            menuvalue[12] = -9;
            menulowerlimit[12] = -50;
            menuupperlimit[12] =  50;
            menuincrement[12] = 0.5;
        }
        BlueFrontDistance2 = menuvalue[12];

        if (init) {
            menulabel[13] = "BLUE Front Heading 2";
            menuvalue[13] =  -90;
            menulowerlimit[13] = -180;
            menuupperlimit[13] =  180;
            menuincrement[13] = 1.0;
        }
       BlueFrontHeading2 = menuvalue[13];

        if (init) {
            menulabel[14] = "RED Back Distance 1";
            menuvalue[14] = -30;
            menulowerlimit[14] = -50;
            menuupperlimit[14] =  50;
            menuincrement[14] = 0.5;
        }
       RedBackDistance1 = menuvalue[14];

        if (init) {
            menulabel[15] = "RED Back Heading 1";
            menuvalue[15] = 2;
            menulowerlimit[15] = -180;
            menuupperlimit[15] =  180;
            menuincrement[15] = 1.0;
        }
        RedBackHeading1 = menuvalue[15];

        if (init) {
            menulabel[16] = "RED Back Distance 2";
            menuvalue[16] = 7;
            menulowerlimit[16] = -50;
            menuupperlimit[16] =  50;
            menuincrement[16] = 0.5;
        }
        RedBackDistance2 = menuvalue[16];

        if (init) {
            menulabel[17] = "RED Back Heading 2";
            menuvalue[17] = 0;
            menulowerlimit[17] = -180;
            menuupperlimit[17] =  180;
            menuincrement[17] = 1.0;
        }
        RedBackHeading2 = menuvalue[17];

        if (init) {
            menulabel[18] = "RED Back Distance 3";
            menuvalue[18] = -9;
            menulowerlimit[18] = -50;
            menuupperlimit[18] =  50;
            menuincrement[18] = 0.5;
        }
        RedBackDistance3 = menuvalue[18];

        if (init) {
            menulabel[19] = "RED Back Heading 3";
            menuvalue[19] = 0;
            menulowerlimit[19] = -180;
            menuupperlimit[19] =  180;
            menuincrement[19] = 1.0;
        }
        RedBackHeading3 = menuvalue[19];

        if (init) {
            menulabel[20] = "BLUE Back Distance 1";
            menuvalue[20] = 30;
            menulowerlimit[20] = -50;
            menuupperlimit[20] =  50;
            menuincrement[20] = 0.5;
        }
        BlueBackDistance1 = menuvalue[20];

        if (init) {
            menulabel[21] = "BLUE Back Heading 1";
            menuvalue[21] = 0;
            menulowerlimit[21] = -180;
            menuupperlimit[21] =  180;
            menuincrement[21] = 1.0;
        }
        BlueBackHeading1 = menuvalue[21];

        if (init) {
            menulabel[22] = "BLUE Back Distance 2";
            menuvalue[22] = 4;
            menulowerlimit[22] = -50;
            menuupperlimit[22] =  50;
            menuincrement[22] = 0.5;
        }
        BlueBackDistance2 = menuvalue[22];

        if (init) {
            menulabel[23] = "BLUE Back Heading 2";
            menuvalue[23] = 0;
            menulowerlimit[23] = -180;
            menuupperlimit[23] =  180;
            menuincrement[23] = 1.0;
        }
        BlueBackHeading2 = menuvalue[23];

        // need a turn180 here
        if (init) {
            menulabel[24] = "BLUE Turn   3";
            menuvalue[24] = 180;
            menulowerlimit[24] = -180;
            menuupperlimit[24] =  180;
            menuincrement[24] = 0.5;
        }
        BlueBackTurn3 = menuvalue[24];


        if (init) {
            menulabel[25] = "BLUE Back Distance 3";
            menuvalue[25] = -12;
            menulowerlimit[25] = -50;
            menuupperlimit[25] =  50;
            menuincrement[25] = 0.5;
        }
        BlueBackDistance3 = menuvalue[25];

        if (init) {
            menulabel[26] = "BLUE Back Heading 3";
            menuvalue[26] = 180;
            menulowerlimit[26] = -180;
            menuupperlimit[26] =  180;
            menuincrement[26] = 1.0;
        }
        BlueBackHeading3 = menuvalue[26];


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
//                menuvalue[i] = Integer.parseInt(br.readLine());                //values is our integer array, converts string to integer
                menuvalue[i] = Double.parseDouble(br.readLine());                //values is our integer array, converts string to integer

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
            telemetry.addLine("####    " + menuvaluetoken[0][((int)menuvalue[0])] +" " +menuvaluetoken[1][(int)menuvalue[1]]+" " +menuvaluetoken[2][(int)menuvalue[2]] +"   ####");
//            telemetry.addLine("#######    " + menuvaluetoken[0][menuvalue[0]] +"   " +  menuvaluetoken[1][menuvalue[1]] + "  " +  menuvaluetoken[3][menuvalue[3]] +"    ######");
            telemetry.addLine("");
            for ( int i=0; i < menulabel.length; i++){                     // write out the list of variables
                if ( i == index ) {                                        // if the index, add an arrow
                    if (menuvaluetoken[i][1] != "" ) {                           // menu items that need tokens should be less than 5
//                    if (menuupperlimit[i] < 5) {                           // menu items that need tokens should be less than 5
                    telemetry.addLine().addData(menulabel[i], menuvalue[i] + "  " + menuvaluetoken[i][(int)menuvalue[i]] + "  <<======" );
                    }else{
                    telemetry.addLine().addData(menulabel[i], menuvalue[i] + "  <<======");
                    }
                }else{
                    if (menuvaluetoken[i][1] != "" ) {                           // menu items that need tokens should be less than 5
//                        if (menuupperlimit[i] < 5 ) {                           // menu items that need tokens should be less than 5
                        telemetry.addLine().addData(menulabel[i], menuvalue[i] + "  " + menuvaluetoken[i][(int)menuvalue[i]] );
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
                    menuvalue[index] += menuincrement[index];
                }
            } else {
                bisreleased = true;
            }
            if (gamepad.x) {
                if (xisreleased) {
                    xisreleased = false;
                    menuvalue[index] -= menuincrement[index];
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


    public void displayValues() {

        int indexstart = 4, indexend = 26;

        for ( int i=0; i < 4; i++){                     // write out the list of variables
            if (menuvaluetoken[i][1] != "" ) {                           // menu items that need tokens should be less than 5
//                        if (menuupperlimit[i] < 5 ) {                           // menu items that need tokens should be less than 5
                    telemetry.addLine().addData(menulabel[i], menuvalue[i] + "  " + menuvaluetoken[i][(int)menuvalue[i]] );
                }else{
                    telemetry.addLine().addData(menulabel[i], menuvalue[i]);
                }

        }
        //rf 3-7  bf 8-12  rb 13 18 bb 19-25
        if(teamIsRed && startPositionIsFront) {
            indexstart=4; indexend=8;
        } else if(!teamIsRed && startPositionIsFront ) {
            indexstart=9; indexend=13;
        } else if(teamIsRed && !startPositionIsFront ) {
            indexstart=14; indexend=19;
        } else if(!teamIsRed && !startPositionIsFront ) {
            indexstart=20; indexend=26;
        }
        for ( int i=indexstart; i <= indexend; i++){                     // write out the list of variables
            if (menuvaluetoken[i][1] != "" ) {                           // menu items that need tokens should be less than 5
//                        if (menuupperlimit[i] < 5 ) {                           // menu items that need tokens should be less than 5
                telemetry.addLine().addData(menulabel[i], menuvalue[i] + "  " + menuvaluetoken[i][(int)menuvalue[i]] );
            }else{
                telemetry.addLine().addData(menulabel[i], menuvalue[i]);
            }

        }




    }



}