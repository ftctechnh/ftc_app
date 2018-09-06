package RicksCode.Bill_Adapted;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Tele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by jmgu3 on 11/7/2016.
 */
public class ConfigFileHandler {
    private Gamepad gamepad;


    Telemetry telemetry;
    public ConfigFileHandler(Telemetry telemetry, Gamepad g){
        this.telemetry = telemetry;
        gamepad = g;
        }

    // variables used during the configuration process
    private String configFileName = "8045Relic.txt";
    private String directoryPath = "/sdcard/FIRST/";
    private String filePath = directoryPath + configFileName;


    // all data here
    public Integer teamIsRed;
    public Integer startPosition;
    public Integer mode;
//    public Integer shooterForwardAfterShoot;
//    public Integer driveSpeed;
//    public Integer whiteColor;
//    public Integer drivmBackTime;


//    //Menu Variables
    public int      nitems = 7;    // easier to just include 0 as the first element so there are actually 5
    //public String[]  menulabel = {"waitTime","shooterWait","shooterForwardTime","shooterForwardAfterShoot","driveSpeed","whiteColor","driveBackTime"};
    public String  menulabel[] = new String[nitems];
    public int     menuvalue[] = new int[nitems];
    public int     menuvaluelimit[][] = new int[nitems][2];
    public String  menuvaluetoken[][] = new String[nitems][5];


//    public int[]     menuvalue  = {0,1,2,3,4,5,6};
//    public String[] teamname = {"Red", "Blue"};
//    public String[] modename = {"Beacon Route","Judging Mode","Beacon Route SUPER Mode"," Simple Capball","Simple Ramp", "Defense Mode","Super Defense Mode"};
//    public int      programmode,secondstodelay,particles,heading0,distance0,heading1,distance1,heading2,distance2,heading3,distance3,heading4,distance4,heading5,distance5,rightaveragecolor,leftaveragecolor, supermodeturnblue,supermodeturnred;
//    public boolean  TeamisBlue,PickUpPartnerBall, hitcapball;


    public void testGamepad(){
        telemetry.addData("x",gamepad.x);
        telemetry.addData("Right Trigger",gamepad.right_trigger);
        telemetry.update();
    }

    public void initializeValues() {

//        waitTime = 0;
//        shooterWait = 7;
//        shooterForwardTime = 8;
//        shooterForwardAfterShoot = 9;
//        driveSpeed = 5;
//        whiteColor = 5;
//        driveBackTime = 750;

        menulabel[0] = "teamIsRed";
        menuvalue[0] = 1;
        menuvaluelimit[0][0] = 0;
        menuvaluelimit[0][1] = 1;
        menuvaluetoken[0][0] = "Blue";
        menuvaluetoken[0][1] = "Red";
        teamIsRed = menuvalue[0];

        menulabel[1] = "Position";
        menuvalue[1] = 1;
        menuvaluelimit[1][0] = 0;
        menuvaluelimit[1][1] = 1;
        menuvaluetoken[1][0] = "Back";
        menuvaluetoken[1][1] = "Front";
        startPosition = menuvalue[1];

        menulabel[2] = "Mode";
        menuvalue[2] = 0;
        menuvaluelimit[2][0] = 0;
        menuvaluelimit[2][1] = 3;
        menuvaluetoken[2][0] = "Glyph";
        menuvaluetoken[2][1] = "Relic";
        menuvaluetoken[2][0] = "Test";
        menuvaluetoken[2][1] = "Demo";
        mode = menuvalue[2];

        menulabel[3] = "test3";
        menuvalue[3] = 13;
        menulabel[4] = "test4";
        menuvalue[4] = 14;
        menulabel[5] = "test5";
        menuvalue[5] = 15;
        menulabel[6] = "test6";
        menuvalue[6] = 16;



//        String[] menulabel = {"waitTime","one","two","three","four","five","six"};
//        int[]    menuvalue = {0,1,2,3,4,5,6};
//        menulabel[1] = "shooterWait";
//        menuvalue[1] = shooterWait;
//        menulabel[2] = "shooterForwardTime";
//        menuvalue[2] = shooterForwardTime;
//        menulabel[3] = "shooterForwardAfterShoot";
//        menuvalue[3] = shooterForwardAfterShoot;
//        menulabel[4] = "driveSpeed";
//        menuvalue[4] = driveSpeed;
//        menulabel[5] = "whiteColor";
//        menuvalue[5] = whiteColor;
//        menulabel[6] = "driveBackTime";
//        menuvalue[6] = driveBackTime;

    }

//    public void readDataFromFile(Context context) {
//        // setup initial configuration parameters here
//        initializeValues();
//
//        // read configuration data from file
//        try {
//            InputStream inputStream = context.openFileInput(configFileName);
//
//            if (inputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                // read data here
//                waitTime = Integer.valueOf(bufferedReader.readLine());
//                shooterWait = Double.valueOf(bufferedReader.readLine());
//                shooterForwardTime = Double.valueOf(bufferedReader.readLine());
//                shooterForwardAfterShoot = Double.valueOf(bufferedReader.readLine());
//                driveSpeed = Double.valueOf(bufferedReader.readLine());
//                whiteColor = Double.valueOf(bufferedReader.readLine());
//                driveBackTime = Double.valueOf(bufferedReader.readLine());
//                //shooterSpeed = Double.valueOf(bufferedReader.readLine());
//
//                inputStream.close();
//            }
//        } catch (Exception e) {
//            // values here, for first time or in case there's a problem reading.
//            initializeValues();
//        }
//
//    }


    public void readDataFromTxtFile(Context context) {
        // setup initial configuration parameters here
        initializeValues();
//        telemetry.addData("x", gamepad.x);
//        telemetry.addData("joyrx", gamepad.right_stick_x);
//        telemetry.update();

        // read configuration data from file
//        String fileName = "test.txt";
//        String directoryPath = "/sdcard/FIRST/";
//        String filePath = directoryPath + fileName;

        int i = 0;
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String s;
            while ((s = br.readLine()) != null && i < menuvalue.length) {                // read the label then the value they are on separate lines
                menulabel[i] = s;                                   //Updates the label "string" of our array
                s = br.readLine();
                menuvalue[i] = Integer.parseInt(s);                //values is our integer array, converts string to integer
                //System.out.println(s);
                i += 1;                                         //Only to do with our array
            }

//                waitTime = Integer.valueOf(br.readLine());
//                shooterWait = Double.valueOf(br.readLine());
//                shooterForwardTime = Double.valueOf(br.readLine());
//                shooterForwardAfterShoot = Double.valueOf(br.readLine());
//                driveSpeed = Double.valueOf(br.readLine());
//                whiteColor = Double.valueOf(br.readLine());
//                driveBackTime = Double.valueOf(br.readLine());



            fr.close();

        } catch (IOException ex) {
            System.err.println("Couldn't read this: " + filePath);//idk where this is printing
            initializeValues();
        }

//        try {
//            InputStream inputStream = context.openFileInput(configFileName);
//
//            if (inputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                // read data here
//                waitTime = Integer.valueOf(bufferedReader.readLine());
//                shooterWait = Double.valueOf(bufferedReader.readLine());
//                shooterForwardTime = Double.valueOf(bufferedReader.readLine());
//                shooterForwardAfterShoot = Double.valueOf(bufferedReader.readLine());
//                driveSpeed = Double.valueOf(bufferedReader.readLine());
//                whiteColor = Double.valueOf(bufferedReader.readLine());
//                driveBackTime = Double.valueOf(bufferedReader.readLine());
//                //shooterSpeed = Double.valueOf(bufferedReader.readLine());
//
//                inputStream.close();
//            }
//        } catch (Exception e) {
//            // values here, for first time or in case there's a problem reading.
//            initializeValues();
//        }

    }

//    public boolean writeDataToFile(Context context) {
//        // may want to write configuration parameters to a file here if they are needed for teleop too!
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(configFileName, Context.MODE_PRIVATE));
//
//            // write data here, as a string on its own line. "\n" puts a new line at the end of the write, like hitting "enter"
//
//            outputStreamWriter.write(Integer.toString(waitTime) + "\n");
//            outputStreamWriter.write(Double.toString(shooterWait) + "\n");
//            outputStreamWriter.write(Double.toString(shooterForwardTime) + "\n");
//            outputStreamWriter.write(Double.toString(shooterForwardAfterShoot) + "\n");
//            outputStreamWriter.write(Double.toString(driveSpeed) + "\n");
//            outputStreamWriter.write(Double.toString(whiteColor) + "\n");
//            outputStreamWriter.write(Double.toString(driveBackTime) + "\n");
//            outputStreamWriter.close();
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//
//    }


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