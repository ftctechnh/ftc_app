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
    public boolean startPositionIsFront, DoTheWrongJewel;
    public double DriveSpeed;

    public double mode,distance1,distance2;

    public double BlueFrontDistance1Right,BlueFrontDistance1Center,BlueFrontDistance1Left,BlueFrontHeading1,BlueFrontTurn1,BlueFrontDistance2,BlueFrontHeading2;
    public double RedFrontDistance1Right,RedFrontDistance1Center,RedFrontDistance1Left,RedFrontHeading1,RedFrontTurn1,RedFrontDistance2,RedFrontHeading2;
    public double BlueBackDistance1,BlueBackHeading1,BlueBackDistance2Right,BlueBackDistance2Center,BlueBackDistance2Left,BlueBackHeading2,BlueBackTurn3,BlueBackDistance3,BlueBackHeading3 ;
    public double RedBackDistance1,RedBackHeading1,RedBackDistance2Right,RedBackDistance2Center,RedBackDistance2Left,RedBackHeading2,RedBackDistance3,RedBackHeading3 ;

    public double RedBlueFrontDistance3,BlueFrontDistance3,RedBackDistance4,RedBackHeading4,BlueBackDistance4,BlueBackHeading4;

    //    //Menu Variables
    public int      nitems = 41;    //
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
            menulabel[0] = "Team Color (1)";
            menuvalue[0] = 1;
            menulowerlimit[0] = 0;
            menuupperlimit[0] = 1;
            menuincrement[0] = 1;
            menuvaluetoken[0][0] = "Blue";
            menuvaluetoken[0][1] = "Red";
        }
        teamIsRed = (menuvalue[0] == 1);

        if (init) {
            menulabel[1] = "Start Position (1)";
            menuvalue[1] = 1;
            menulowerlimit[1] = 0;
            menuupperlimit[1] = 1;
            menuincrement[1] = 1;
            menuvaluetoken[1][0] = "Back";
            menuvaluetoken[1][1] = "Front";
        }
        startPositionIsFront = (menuvalue[1] == 1);

        if (init) {
            menulabel[2] = "Mode (0)";
            menuvalue[2] = 2;
            menulowerlimit[2] = 0;
            menuupperlimit[2] = 3;
            menuincrement[2] = 1;
            menuvaluetoken[2][0] = "Regular Glyph";
            menuvaluetoken[2][1] = "SuperMode Column";
            menuvaluetoken[2][2] = "Supermode Row";
            menuvaluetoken[2][3] = "Test ";
        }
        mode = menuvalue[2];

        if (init) {
            menulabel[3] = "Drive Speed (0.7) ";
            menuvalue[3] = 0.7;
            menulowerlimit[3] =  0;
            menuupperlimit[3] = 2;
            menuincrement[3] = 0.05;
        }
        DriveSpeed = menuvalue[3];

        if (init) {
            menulabel[4] = "RED Front Distance 1 RIGHT (-33.0)";
            menuvalue[4] = -33;
            menulowerlimit[4] = -70;
            menuupperlimit[4] =  70;
            menuincrement[4] = 0.5;
        }
        RedFrontDistance1Right = menuvalue[4];

        if (init) {
            menulabel[5] = "RED Front Distance 1 CENTER (-43.5)";
            menuvalue[5] = -43.5;
            menulowerlimit[5] = -70;
            menuupperlimit[5] =  70;
            menuincrement[5] = 0.5;
        }
        RedFrontDistance1Center = menuvalue[5];

        if (init) {
            menulabel[6] = "RED Front Distance 1 LEFT (-52.5)";
            menuvalue[6] = -52.5;
            menulowerlimit[6] = -70;
            menuupperlimit[6] =  70;
            menuincrement[6] = 0.5;
        }
        RedFrontDistance1Left = menuvalue[6];

        if (init) {
            menulabel[7] = "RED Front Heading 1 (0)";
            menuvalue[7] = 0;
            menulowerlimit[7] = -180;
            menuupperlimit[7] =  180;
            menuincrement[7] = 1.0;
        }
        RedFrontHeading1 = menuvalue[7];

        if (init) {
            menulabel[8] = "RED Front Turn 1 (-90.0)";
            menuvalue[8] = -90;
            menulowerlimit[8] = -180;
            menuupperlimit[8] =  180;
            menuincrement[8] = 0.5;
        }
        RedFrontTurn1 = menuvalue[8];

        if (init) {
            menulabel[9] = "RED Front Distance 2 (-9.0)";
            menuvalue[9] = -9;
            menulowerlimit[9] = -70;
            menuupperlimit[9] =  70;
            menuincrement[9] = 0.5;
        }
        RedFrontDistance2 = menuvalue[9];

        if (init) {
            menulabel[10] = "RED Front Heading 2 (-90)";
            menuvalue[10] = -90;
            menulowerlimit[10] = -180;
            menuupperlimit[10] =  180;
            menuincrement[10] = 1.0;
        }
        RedFrontHeading2 = menuvalue[10];   //fb 3-7

        if (init) {
            menulabel[11] = "BLUE Front Distance 1 RIGHT (56.0)";
            menuvalue[11] = 56;
            menulowerlimit[11] = -70;
            menuupperlimit[11] =  70;
            menuincrement[11] = 0.5;
        }
        BlueFrontDistance1Right = menuvalue[11];

        if (init) {
            menulabel[12] = "BLUE Front Distance 1 CENTER (46)";
            menuvalue[12] = 46;
            menulowerlimit[12] = -70;
            menuupperlimit[12] =  70;
            menuincrement[12] = 0.5;
        }
        BlueFrontDistance1Center = menuvalue[12];

        if (init) {
            menulabel[13] = "BLUE Front Distance 1 LEFT (36.0)";
            menuvalue[13] = 36;
            menulowerlimit[13] = -70;
            menuupperlimit[13] =  70;
            menuincrement[13] = 0.5;
        }
        BlueFrontDistance1Left = menuvalue[13];

        if (init) {
            menulabel[14] = "BLUE Front Heading 1 (0)";
            menuvalue[14] = 0;
            menulowerlimit[14] = -180;
            menuupperlimit[14] =  180;
            menuincrement[14] = 1.0;
        }
       BlueFrontHeading1 = menuvalue[14];

        if (init) {
            menulabel[15] = "BLUE Front Turn 1 (-90)";
            menuvalue[15] = -90;
            menulowerlimit[15] = -180;
            menuupperlimit[15] =  180;
            menuincrement[15] = 0.5;
        }
        BlueFrontTurn1 = menuvalue[15];

        if (init) {
            menulabel[16] = "BLUE Front Distance 2 (-9.0)";
            menuvalue[16] = -9;
            menulowerlimit[16] = -70;
            menuupperlimit[16] =  70;
            menuincrement[16] = 0.5;
        }
        BlueFrontDistance2 = menuvalue[16];

        if (init) {
            menulabel[17] = "BLUE Front Heading 2 (-90)";
            menuvalue[17] =  -90;
            menulowerlimit[17] = -180;
            menuupperlimit[17] =  180;
            menuincrement[17] = 1.0;
        }
       BlueFrontHeading2 = menuvalue[17];

        if (init) {
            menulabel[18] = "RED Back Distance 1 (-30.0)";
            menuvalue[18] = -30;
            menulowerlimit[18] = -70;
            menuupperlimit[18] =  70;
            menuincrement[18] = 0.5;
        }
       RedBackDistance1 = menuvalue[18];

        if (init) {
            menulabel[19] = "RED Back Heading 1 (0.0)";
            menuvalue[19] = 0;
            menulowerlimit[19] = -180;
            menuupperlimit[19] =  180;
            menuincrement[19] = 1.0;
        }
        RedBackHeading1 = menuvalue[19];

        if (init) {
            menulabel[20] = "RED Back Distance 2 RIGHT (8.5)";
            menuvalue[20] = 8.5;
            menulowerlimit[20] = -70;
            menuupperlimit[20] =  70;
            menuincrement[20] = 0.5;
        }
        RedBackDistance2Right = menuvalue[20];
       
        if (init) {
            menulabel[21] = "RED Back Distance 2 CENTER (20.0)";
            menuvalue[21] = 20.0;
            menulowerlimit[21] = -70;
            menuupperlimit[21] =  70;
            menuincrement[21] = 0.5;
        }
        RedBackDistance2Center = menuvalue[21];
        
        if (init) {
            menulabel[22] = "RED Back Distance 2 LEFT (36.0)";
            menuvalue[22] = 36;
            menulowerlimit[22] = -70;
            menuupperlimit[22] =  70;
            menuincrement[22] = 0.5;
        }
        RedBackDistance2Left = menuvalue[22];

        if (init) {
            menulabel[23] = "RED Back Heading 2 (0)";
            menuvalue[23] = 0;
            menulowerlimit[23] = -180;
            menuupperlimit[23] =  180;
            menuincrement[23] = 1.0;
        }
        RedBackHeading2 = menuvalue[23];

        if (init) {
            menulabel[24] = "RED Back Distance 3 Right (-9.0)";
            menuvalue[24] = -9;
            menulowerlimit[24] = -70;
            menuupperlimit[24] =  70;
            menuincrement[24] = 0.5;
        }
        RedBackDistance3 = menuvalue[24];

        if (init) {
            menulabel[25] = "RED Back Heading 3 (0)";
            menuvalue[25] = 0;
            menulowerlimit[25] = -180;
            menuupperlimit[25] =  180;
            menuincrement[25] = 1.0;
        }
        RedBackHeading3 = menuvalue[25];

        if (init) {
            menulabel[26] = "BLUE Back Distance 1 (32.0)";
            menuvalue[26] = 32;
            menulowerlimit[26] = -70;
            menuupperlimit[26] =  70;
            menuincrement[26] = 0.5;
        }
        BlueBackDistance1 = menuvalue[26];

        if (init) {
            menulabel[27] = "BLUE Back Heading 1 (0)";
            menuvalue[27] = 0;
            menulowerlimit[27] = -180;
            menuupperlimit[27] =  180;
            menuincrement[27] = 1.0;
        }
        BlueBackHeading1 = menuvalue[27];

        if (init) {
            menulabel[28] = "BLUE Back Distance 2 LEFT (11)";
            menuvalue[28] = 11;
            menulowerlimit[28] = -70;
            menuupperlimit[28] =  70;
            menuincrement[28] = 0.5;
        }
        BlueBackDistance2Left = menuvalue[28];

        if (init) {
            menulabel[29] = "BLUE Back Distance 2 CENTER (23.0)";
            menuvalue[29] = 23.0;
            menulowerlimit[29] = -70;
            menuupperlimit[29] =  70;
            menuincrement[29] = 0.5;
        }
        BlueBackDistance2Center = menuvalue[29];

        if (init) {
            menulabel[30]= "BLUE Back Distance 2 RIGHT (34.5)";
            menuvalue[30] = 34.5;
            menulowerlimit[30] = -70;
            menuupperlimit[30] =  70;
            menuincrement[30] = 0.5;
        }
        BlueBackDistance2Right = menuvalue[30];

        if (init) {
            menulabel[31]= "BLUE Back Heading 2 (0)";
            menuvalue[31]= 0;
            menulowerlimit[31]= -180;
            menuupperlimit[31]=  180;
            menuincrement[31]= 1.0;
        }
        BlueBackHeading2 = menuvalue[31];

        // need a turn180 here
        if (init) {
            menulabel[32] = "*BLUE Back Turn   3 (180)";
            menuvalue[32] = 180;
            menulowerlimit[32] = -180;
            menuupperlimit[32] =  180;
            menuincrement[32] = 0.5;
        }
        BlueBackTurn3 = menuvalue[32];


        if (init) {
            menulabel[33] = "BLUE Back Distance 3 (-10)";
            menuvalue[33] = -10;
            menulowerlimit[33] = -70;
            menuupperlimit[33] =  70;
            menuincrement[33] = 0.5;
        }
        BlueBackDistance3 = menuvalue[33];

        if (init) {
            menulabel[34] = "BLUE Back Heading 3 (-180)";
            menuvalue[34] = -180;
            menulowerlimit[34] = -180;
            menuupperlimit[34] =  180;
            menuincrement[34] = 1.0;
        }
        BlueBackHeading3 = menuvalue[34];

        if (init) {
            menulabel[35] = "SM RED&BLUE Front Distance 3 (50)";
            menuvalue[35] = 50;
            menulowerlimit[35] = -70;
            menuupperlimit[35] =  70;
            menuincrement[35] = 0.5;
        }
        RedBlueFrontDistance3 = menuvalue[35];

        if (init) {
            menulabel[36] = "SM RED Back Distance 4 (63)";
            menuvalue[36] = 63;
            menulowerlimit[36] = -80;
            menuupperlimit[36] =  80;
            menuincrement[36] = 0.5;
        }
        RedBackDistance4 = menuvalue[36];

        if (init) {
            menulabel[37] = "SM RED Back  Heading 4 (-30)";
            menuvalue[37] = -30 ;
            menulowerlimit[37] = -180;
            menuupperlimit[37] =  180;
            menuincrement[37] = 1.0;
        }
        RedBackHeading4 = menuvalue[37];

        if (init) {
            menulabel[38] = "SM Blue Back Distance 4 (65)";
            menuvalue[38] = 65;
            menulowerlimit[38] = -80;
            menuupperlimit[38] =  80;
            menuincrement[38] = 0.5;
        }
        BlueBackDistance4 = menuvalue[38];

        if (init) {
            menulabel[39] = "SM Blue Back Heading 4 (-150)";
            menuvalue[39] = -150;
            menulowerlimit[39] = -180;
            menuupperlimit[39] =  180;
            menuincrement[39] = 1.0;
        }
        BlueBackHeading4 = menuvalue[39];

        if (init) {
            menulabel[40] = "Jewel 4 RP";
            menuvalue[40] = 0;
            menulowerlimit[40] = 0;
            menuupperlimit[40] = 1;
            menuincrement[40] = 1;
            menuvaluetoken[40][0] = "Do RIGHT Jewel";
            menuvaluetoken[40][1] = "Do WRONG Jewel";
        }
        DoTheWrongJewel = (menuvalue[40] == 1);


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
            indexstart=4; indexend=10;
        } else if(!teamIsRed && startPositionIsFront ) {
            indexstart=11; indexend=17;
        } else if(teamIsRed && !startPositionIsFront ) {
            indexstart=18; indexend=25;
        } else if(!teamIsRed && !startPositionIsFront ) {
            indexstart=26; indexend=34;
        }
        for ( int i=indexstart; i <= indexend; i++){                     // write out the list of variables
            if (menuvaluetoken[i][1] != "" ) {                           // menu items that need tokens should be less than 5
//                        if (menuupperlimit[i] < 5 ) {                           // menu items that need tokens should be less than 5
                telemetry.addLine().addData(menulabel[i], menuvalue[i] + "  " + menuvaluetoken[i][(int)menuvalue[i]] );
            }else{
                telemetry.addLine().addData(menulabel[i], menuvalue[i]);
            }

        }
        // wrong jewel
        telemetry.addLine().addData(menulabel[40], menuvalue[40] + "  " + menuvaluetoken[40][(int)menuvalue[40]] );



    }



}