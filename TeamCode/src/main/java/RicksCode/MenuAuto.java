/*
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package RicksCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


@Autonomous(name="MenuTest", group="Team")
@Disabled


public class MenuAuto extends LinearOpMode {



    /* Declare OpMode members. */
    //Hardware_8045Worlds gromit    = new Hardware_8045Worlds();   // Use a 8045 hardware
 //   MenuData autoFileHandler;

    //DeviceInterfaceModule dim;

    // Create timer to toggle LEDs
    public ElapsedTime runtime = new ElapsedTime();
    public boolean targetisfront;
    public static String fileName ="8045config.txt";
    public static String directoryPath = "/sdcard/FIRST/" ;
    public static String filePath;
    //Menu Variables
    public static String[] menulabel;
    public static int[]    menuvalue;

    public static String[] teamname;
    public static String[] modename;
    public static int      programmode,secondstodelay,particles,heading0,distance0,heading1,distance1,heading2,distance2,heading3,distance3,heading4,distance4,heading5,distance5,rightaveragecolor,leftaveragecolor, supermodeturnblue,supermodeturnred;
    public static boolean  TeamisBlue,PickUpPartnerBall, hitcapball;



    //Variables moved to Hardware_8045

    @Override
    public void runOpMode() {

        /**********************************************************************************************\
         |--------------------------------- Pre Init Loop ----------------------------------------------|
         \**********************************************************************************************/
        telemetry.addLine("Back Button reverts to code values");
        telemetry.update();

        telemetry.addData(">", "Robot Ready.");//Send Telemetry that the robot is ready to run
        telemetry.update();


        InitializeMenu(fileName,menuvalue,menulabel);


        telemetry.addLine("----------------------------------------");
        if (TeamisBlue) { telemetry.addLine("BLUE Alliance");   }   //If Blue Highlight
        else            { telemetry.addLine("RED Alliance");    }   //Otherwise must be Red
        telemetry.update();
        sleep(2000);

        // display the program mode
       // telemetry.addLine().addData(menulabel[1], menuvalue[1] + "  " + modename[menuvalue[1]]);

        //  loop to display the configured items  if the index matches add arrow.
        for ( int i=2; i< menuvalue.length; i++){ telemetry.addLine().addData(menulabel[i], menuvalue[i]); }
        telemetry.addLine("---------------------------temp--------------");

        telemetry.update();
        sleep(2000);

        // edit menu values
        OurMenu(fileName);

        //writeDataToFile(fileName,menulabel,menuvalue);

        /**********************************************************************************************\
         |--------------------------------------Init Loop-----------------------------------------------|
         \**********************************************************************************************/
             while (!isStarted()) {

            //Maxbotix Sensor
            //telemetry.addData("", "Maxbotix volts, Distance(cm): %4.2f  %4.1f", voltage, MaxbotixDistance);


            // display the menu values that are set
            telemetry.addLine("----------------------------READY TO RUN!--------------");
            if (TeamisBlue) { telemetry.addLine("BLUE Alliance");   }   //If Blue Highlight
            else            { telemetry.addLine("RED Alliance");    }   //Otherwise must be Red

            // display the program mode
            telemetry.addLine().addData(menulabel[1], menuvalue[1] + "  " + modename[menuvalue[1]]);

            //  loop to display the configured items  if the index matches add arrow.
            for ( int i=2; i< menuvalue.length; i++){ telemetry.addLine().addData(menulabel[i], menuvalue[i]); }
            telemetry.addLine("----------------------------READY TO RUN!--------------");

            telemetry.update();

            idle();
        }
        telemetry.clear();
        /**********************************************************************************************\
         |-------------------------------------START OF GAME--------------------------------------------|
         \**********************************************************************************************/
        resetStartTime();      // start timer


        if( (programmode == 0 || programmode == 2) ) {                                           /**Beacon Route mode 0*/
            sleep(1000*secondstodelay);   // delay for other team;

            sleep(2000);
        }

      while (opModeIsActive()) {

            idle();
        }

    }


    /**MENUIING JOYSTICK CONTROL IN INIT
     * Menu to change variables corresponding to gyrodrives and turns and delays etc
     * * ***********************************************************************************************/

    public void OurMenu(String fileName) {

        int      programmode,secondstodelay,particles,heading0,distance0,heading1,distance1,heading2,distance2,heading3,distance3,heading4,distance4,heading5,distance5,rightaveragecolor,leftaveragecolor, supermodeturnblue,supermodeturnred;
        boolean  TeamisBlue,PickUpPartnerBall, hitcapball;


//        String[] menulabel= {"Team","Mode", "Particles","Delay","Heading 0", "Distance 0","Heading 1", "Distance 1","Heading 2","Distance 2","Heading 3","Distance 3","Heading 4","Distance 4","Heading 5","Distance 5","Right Color Threshold","Left Color Threshold","SuperMode Turn Blue","SuperMode Turn Red","Hit Cap Ball","Pick Up Partner Ball"};
//        String[] teamname = {"Red", "Blue"};
//        String[] modename = {"Beacon Route","Judging Mode","Beacon Route SUPER Mode"," Simple Capball","Simple Ramp", "Defense Mode","Super Defense Mode"};
//        int[]    menuvalue = {1,0,2,0, 0,8,  -45,55,   20,-14,   0,13,   0,30   ,130,40, 121,121, -6,-7,1,0};

//        String[] menulabel;
//        String[] teamname;
//        String[] modename;
//        int[]    menuvalue;

        //String directoryPath;
        String filePath = directoryPath + fileName ;

        int i;


//        if (!gamepad1.back) {
//
//            /** if the back button is down skip reading the file!
//             * read labels & values from file before we start the init loop
//             * ============================================================
//            // String filePath    = "/sdcard/FIRST/8045_Config.txt";   */
//
//           readDataFromFile(fileName);
//
//
//            i = 0;
////            try {
////                FileReader fr = new FileReader(filePath);
////                BufferedReader br = new BufferedReader(fr);
////                String s;
////                while ((s = br.readLine()) != null && i < menuvalue.length) {                // read the label then the value they are on separate lines
////                    menulabel[i] = s;                                   //Updates the label "string" of our array
////                    s = br.readLine();
////                    menuvalue[i] = Integer.parseInt(s);                //values is our integer array, converts string to integer
////                    //System.out.println(s);
////                    i += 1;                                         //Only to do with our array
////                }
////                fr.close();
////
////            } catch (IOException ex) {
////                System.err.println("Couldn't read this: " + filePath);//idk where this is printing
////            }
//        } else{
//            telemetry.addLine("****** File NOT read from PHONE ********");
//            telemetry.update();
//            sleep(2000);
//        }


        int index = 0;

        boolean yisreleased = true;
        boolean aisreleased = true;
        boolean xisreleased = true;
        boolean bisreleased = true;
        boolean rbumperisreleased = true;
        boolean rjoyisreleased = true;
        boolean editmode = false;

        /** This while loop is the "editing" loop   */
        while (!isStarted() && !gamepad1.right_stick_button ) {

            // update the values we want to use
            TeamisBlue     = (menuvalue[0] != 0);
            programmode    = menuvalue[1];
            particles      = menuvalue[2];
            secondstodelay = menuvalue[3];
            heading0       = menuvalue[4];
            distance0      = menuvalue[5];
            heading1       = menuvalue[6];
            distance1      = menuvalue[7];
            heading2       = menuvalue[8];
            distance2      = menuvalue[9];
            heading3       = menuvalue[10];
            distance3      = menuvalue[11];
            heading4       = menuvalue[12];
            distance4      = menuvalue[13];
            heading5       = menuvalue[14];
            distance5      = menuvalue[15];
            rightaveragecolor = menuvalue[16];
            leftaveragecolor = menuvalue[17];
            supermodeturnblue      = menuvalue[18];
            supermodeturnred       = menuvalue[19];
            PickUpPartnerBall      = (menuvalue[21] != 0);

            //telemetry.addLine().addData(mode[programmode]);
            telemetry.addLine("===> Press Joystick to exit MENU mode <===" );
            telemetry.addLine("#######    " + teamname[menuvalue[0]] +"    " + modename[programmode] + "    #######");

            // output current settings to the screen  (1st two have labels... token is red/blue treat it separately.

//           if (menuvalue[0] == 1) {       //If Blue Highlight if index is 0
//               if (index == 0 && editmode ){telemetry.addLine("BLUE Alliance  <=======");} else {telemetry.addLine("BLUE Alliance");}
//            } else {                    //Otherwise must be Red Highlight if index is 0
//                if (index == 0 && editmode ){telemetry.addLine("RED Alliance  <=======");} else {telemetry.addLine("RED Alliance");}
//            }


            /**          loop to display the configured items  if the index matches add arrow.
            *          First two entries are treate separately with additional strings  */

            if (0 == index && editmode) {
                telemetry.addLine().addData(menulabel[0], menuvalue[0] + "  " + teamname[menuvalue[0]]+"  <=======");
            }else{
                telemetry.addLine().addData(menulabel[0], menuvalue[0] + "  " + teamname[menuvalue[0]]);
            }

            if (1 == index && editmode) {
                telemetry.addLine().addData(menulabel[1], menuvalue[1] + "  " + modename[menuvalue[1]]+"  <=======");
            }else{
                telemetry.addLine().addData(menulabel[1], menuvalue[1] + "  " + modename[menuvalue[1]]);
            }



            /**    loop to display the configured items  if the index matches add arrow.   */
            for ( i=2; i < menuvalue.length; i++){
                //telemetry.addData("*",values[i]);
                if (i == index && editmode) {
                    telemetry.addLine().addData(menulabel[i], menuvalue[i]+"  <=======");

                }else{
                    telemetry.addLine().addData(menulabel[i], menuvalue[i]);
                }

            }
            telemetry.addLine("");



//            // blink lights for red/blue  while in the menu method
//            boolean even = (((int) (runtime.time()) & 0x01) == 0);             // Determine if we are on an odd or even second
//            if ( TeamisBlue) {
//                dim.setLED(BLUE_LED, even); // blue for true
//                dim.setLED(RED_LED, false);
//                if(even) {
//                    lightsblue.setPower(0.5);
//                }
//                else{
//                    lightsblue.setPower(0.0);
//                }
//            } else {
//                dim.setLED(RED_LED,  even); // Red for false
//                dim.setLED(BLUE_LED, false);
//            }


            /**THIS IS EDIT MODE=============================================
            *         if we haven't pressed settings done bumper, look for button presses, blink the LED  */
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
                if (index >= menuvalue.length) index = 0;          // limit the bounds of index
                if (index < 0) index = menuvalue.length - 1 ;

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
                if (menuvalue[0] >  1) menuvalue[0] = 0;          //team color is boolean
                if (menuvalue[0] <  0) menuvalue[0] = 1;          //team color is boolean
                if (menuvalue[1] >= 7) menuvalue[1] = 0;          //modes 0,1,2,3,4,5,6
                if (menuvalue[1] <  0) menuvalue[1] = 6;          //
                if (menuvalue[2] >= 3) menuvalue[2] = 0;          //Particles, 0,1,2
                if (menuvalue[2] <  0) menuvalue[2] = 2;          //
                if (menuvalue[3] <  0) menuvalue[3] = 0;          // Delay can't be negative
                if (menuvalue[20] >  1) menuvalue[20] = 0;          // Cap Ball boolean
                if (menuvalue[20] <  0) menuvalue[20] = 1;          //
                if (menuvalue[21] >  1) menuvalue[21] = 0;          //Pick up Partner Ball boolean
                if (menuvalue[21] <  0) menuvalue[21] = 1;          //


                //telemetry.addLine(" ");
                telemetry.addLine(" EDIT MODE: Right Bumper to EXIT");


                // check to exit the edit mode
                if (gamepad1.right_bumper) {
                    if (rbumperisreleased) {
                        rbumperisreleased = false;
                        editmode = false;

                    }
                }else {
                    rbumperisreleased = true;
                }
            }
                /** END OF EDIT MODE===================================================
                /** in ready mode check if you'd like to return to editing, add telemetry line.  */
            else {


                if (gamepad1.right_bumper) {
                    if (rbumperisreleased) {
                        editmode = true;
                        rbumperisreleased = false;
                    }
                }else {
                    rbumperisreleased = true;
                }


                telemetry.addLine("SAVE MODE: Right Bumper to Edit ");
                telemetry.addLine("Joystick to SAVE Setup ");

            }

            telemetry.update();
            idle();
        }


        /**  Init loop finished, move to running now  First write the config file
        //==========================================  */

//        writeDataToFile(fileName);
        try {
              FileWriter fw = new FileWriter(filePath);
              for ( i=0; i<=menuvalue.length-1; i++){
                fw.write((menulabel[i]));
                fw.write(System.lineSeparator());
                fw.write(Integer.toString(menuvalue[i]));
                fw.write(System.lineSeparator());
              }
//            fw.write(values);
//            fw.write(System.lineSeparator());
            fw.close();

        } catch (IOException ex) {
            System.err.println("Couldn't write this file: "+filePath);
           }
        telemetry.addData("ConfigFile saved to ","filePath");
        telemetry.update();
        sleep(500);



    }

    public void InitializeMenu(String fileName,int[] menuvalue, String[] menulabel) {

        //Menu Variables


        //String directoryPath;
        String filePath = directoryPath + fileName ;


        // setup initial configuration parameters here
        //initializeValues();

        new File(directoryPath).mkdir() ;        // Make sure that the directory exists

        int i = 0;

        // read configuration data from file
        if (!gamepad1.back) {

            /** if the back button is down skip reading the file!
             * read labels & values from file before we start the init loop
             * ============================================================
             // String filePath    = "/sdcard/FIRST/8045Config.txt";   */

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
                fr.close();

            } catch (IOException ex) {
                System.err.println("Couldn't read this: " + filePath);//idk where this is printing
                sleep(2000);
            }

        } else{
//            String[] menulabel = {"Team","Mode", "Particles","Delay","Heading 0", "Distance 0","Heading 1", "Distance 1","Heading 2","Distance 2","Heading 3","Distance 3","Heading 4","Distance 4","Heading 5","Distance 5","Right Color Threshold","Left Color Threshold","SuperMode Turn Blue","SuperMode Turn Red","Hit Cap Ball","Pick Up Partner Ball"};
//            menuvalue = {1,0,2,0, 0,8,  -45,55,   20,-14,   0,13,   0,30   ,130,40, 121,121, -6,-7,1,0};

            telemetry.addLine("****** File NOT read from PHONE ********");
            telemetry.update();
            sleep(2000);
        }




    }


    public void writeDataToFile(String fileName, String[] labels, int[] values) {

        //Menu Variables
//       String[] menulabel ;
        //String[] teamname ;
        //String[] modename ;
//        int[]    menuvalue ;
        //int      programmode,secondstodelay,particles,heading0,distance0,heading1,distance1,heading2,distance2,heading3,distance3,heading4,distance4,heading5,distance5,rightaveragecolor,leftaveragecolor, supermodeturnblue,supermodeturnred;
        //boolean  TeamisBlue,PickUpPartnerBall, hitcapball;

        //String directoryPath;

        String filePath = directoryPath + fileName ;


        // setup initial configuration parameters here
        //initializeValues();

        new File(directoryPath).mkdir() ;        // Make sure that the directory exists

        int i = 0;

         // may want to write configuration parameters to a file here if they are needed for teleop too!
        try {
            FileWriter fw = new FileWriter(filePath);
            //for ( i=0; i<=10; i++){
            for ( i=0; i<=values.length-1; i++){
                fw.write((labels[i]));                           // Label
                fw.write(System.lineSeparator());                   // NewLine
                fw.write(Integer.toString(values[i]));           // Value
                fw.write(System.lineSeparator());                   // Newline
            }

            fw.close();

        } catch (IOException ex) {
            System.err.println("Couldn't write this file: "+filePath);
        }
        telemetry.addData("ConfigFile saved to ",filePath);
        telemetry.update();
        sleep(500);

    }

}
