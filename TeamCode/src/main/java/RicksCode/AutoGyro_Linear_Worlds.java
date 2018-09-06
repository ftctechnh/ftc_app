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

import static java.lang.Math.abs;


@Autonomous(name="OriginalMenu", group="Team")
@Disabled


public class AutoGyro_Linear_Worlds extends LinearOpMode {



    /* Declare OpMode members. */
    Hardware_8045Worlds gromit    = new Hardware_8045Worlds();   // Use a 8045 hardware
    //DeviceInterfaceModule dim;

    // Create timer to toggle LEDs
    public ElapsedTime runtime = new ElapsedTime();
    public boolean targetisfront;
    //Variables moved to Hardware_8045

    @Override
    public void runOpMode() {
        /**********************************************************************************************\
         |--------------------------------- Pre Init Loop ----------------------------------------------|
         \**********************************************************************************************/
        telemetry.addLine("Back Button reverts to code values");
        telemetry.update();

        gromit.init(hardwareMap,true);

        telemetry.addData(">", "Robot Ready.");//Send Telemetry that the robot is ready to run
        telemetry.update();

        // get menu values
        OurMenu("8045config.txt");


        /**********************************************************************************************\
         |--------------------------------------Init Loop-----------------------------------------------|
         \**********************************************************************************************/
             while (!isStarted()) {


            // display the menu values that are set
            telemetry.addLine("----------------------------READY TO RUN!--------------");
            if (gromit.TeamisBlue) { telemetry.addLine("BLUE Alliance");   }   //If Blue Highlight
            else            { telemetry.addLine("RED Alliance");    }   //Otherwise must be Red

            // display the program mode
            telemetry.addLine().addData(gromit.menulabel[1], gromit.menuvalue[1] + "  " + gromit.modename[gromit.menuvalue[1]]);

            //  loop to display the configured items  if the index matches add arrow.
            for ( int i=2; i< gromit.menuvalue.length; i++){ telemetry.addLine().addData(gromit.menulabel[i], gromit.menuvalue[i]); }
            telemetry.addLine("----------------------------READY TO RUN!--------------");

            telemetry.update();
            idle();
        }
        telemetry.clear();
        /**********************************************************************************************\
         |-------------------------------------START OF GAME--------------------------------------------|
         \**********************************************************************************************/
        resetStartTime();      // start timer


        if( (gromit.programmode == 0 || gromit.programmode == 2) ) {                                           /**Beacon Route mode 0*/
            sleep(1000*gromit.secondstodelay);   // delay for other team;

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



        int i;
        String directoryPath = "/sdcard/FIRST/";
        String filePath = directoryPath + fileName;
        new File(directoryPath).mkdir();        // Make sure that the directory exists


        if (!gamepad1.back) {

            /** if the back button is down skip reading the file!
             * read labels & values from file before we start the init loop
             * ============================================================
            // String filePath    = "/sdcard/FIRST/8045_Config.txt";   */

            i = 0;
            try {
                FileReader fr = new FileReader(filePath);
                BufferedReader br = new BufferedReader(fr);
                String s;
                while ((s = br.readLine()) != null && i < gromit.menuvalue.length) {                // read the label then the value they are on separate lines
                    gromit.menulabel[i] = s;                                   //Updates the label "string" of our array
                    s = br.readLine();
                    gromit.menuvalue[i] = Integer.parseInt(s);                //values is our integer array, converts string to integer
                    //System.out.println(s);
                    i += 1;                                         //Only to do with our array
                }
                fr.close();

            } catch (IOException ex) {
                System.err.println("Couldn't read this: " + filePath);//idk where this is printing
            }
        } else{
            telemetry.addLine("****** File NOT read from PHONE , defaults used ********");
            telemetry.update();
            sleep(2000);
        }


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
            gromit.TeamisBlue     = (gromit.menuvalue[0] != 0);
            gromit.programmode    = gromit.menuvalue[1];
            gromit.particles      = gromit.menuvalue[2];
            gromit.secondstodelay = gromit.menuvalue[3];
            gromit.heading0       = gromit.menuvalue[4];
            gromit.distance0      = gromit.menuvalue[5];
            gromit.heading1       = gromit.menuvalue[6];
            gromit.distance1      = gromit.menuvalue[7];
            gromit.heading2       = gromit.menuvalue[8];
            gromit.distance2      = gromit.menuvalue[9];
            gromit.heading3       = gromit.menuvalue[10];
            gromit.distance3      = gromit.menuvalue[11];
            gromit.heading4       = gromit.menuvalue[12];
            gromit.distance4      = gromit.menuvalue[13];
            gromit.heading5       = gromit.menuvalue[14];
            gromit.distance5      = gromit.menuvalue[15];
            gromit.rightaveragecolor = gromit.menuvalue[16];
            gromit.leftaveragecolor = gromit.menuvalue[17];
            gromit.supermodeturnblue      = gromit.menuvalue[18];
            gromit.supermodeturnred       = gromit.menuvalue[19];
            gromit.PickUpPartnerBall      = (gromit.menuvalue[21] != 0);

            //telemetry.addLine().addData(gromit.mode[gromit.programmode]);
            telemetry.addLine("===> Press Joystick to exit MENU mode <===" );
            telemetry.addLine("#######    " + gromit.teamname[gromit.menuvalue[0]] +"    " + gromit.modename[gromit.programmode] + "    #######");

            // output current settings to the screen  (1st two have labels... token is red/blue treat it separately.

//           if (gromit.menuvalue[0] == 1) {       //If Blue Highlight if index is 0
//               if (index == 0 && editmode ){telemetry.addLine("BLUE Alliance  <=======");} else {telemetry.addLine("BLUE Alliance");}
//            } else {                    //Otherwise must be Red Highlight if index is 0
//                if (index == 0 && editmode ){telemetry.addLine("RED Alliance  <=======");} else {telemetry.addLine("RED Alliance");}
//            }


            /**          loop to display the configured items  if the index matches add arrow.
            *          First two entries are treate separately with additional strings  */

            if (0 == index && editmode) {
                telemetry.addLine().addData(gromit.menulabel[0], gromit.menuvalue[0] + "  " + gromit.teamname[gromit.menuvalue[0]]+"  <=======");
            }else{
                telemetry.addLine().addData(gromit.menulabel[0], gromit.menuvalue[0] + "  " + gromit.teamname[gromit.menuvalue[0]]);
            }

            if (1 == index && editmode) {
                telemetry.addLine().addData(gromit.menulabel[1], gromit.menuvalue[1] + "  " + gromit.modename[gromit.menuvalue[1]]+"  <=======");
            }else{
                telemetry.addLine().addData(gromit.menulabel[1], gromit.menuvalue[1] + "  " + gromit.modename[gromit.menuvalue[1]]);
            }



            /**    loop to display the configured items  if the index matches add arrow.   */
            for ( i=2; i < gromit.menuvalue.length; i++){
                //telemetry.addData("*",values[i]);
                if (i == index && editmode) {
                    telemetry.addLine().addData(gromit.menulabel[i], gromit.menuvalue[i]+"  <=======");

                }else{
                    telemetry.addLine().addData(gromit.menulabel[i], gromit.menuvalue[i]);
                }

            }
            telemetry.addLine("");



//            // blink lights for red/blue  while in the menu method
//            boolean even = (((int) (runtime.time()) & 0x01) == 0);             // Determine if we are on an odd or even second
//            if ( gromit.TeamisBlue) {
//                gromit.dim.setLED(gromit.BLUE_LED, even); // blue for true
//                gromit.dim.setLED(gromit.RED_LED, false);
//                if(even) {
//                    gromit.lightsblue.setPower(0.5);
//                }
//                else{
//                    gromit.lightsblue.setPower(0.0);
//                }
//            } else {
//                gromit.dim.setLED(gromit.RED_LED,  even); // Red for false
//                gromit.dim.setLED(gromit.BLUE_LED, false);
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
                if (index >= gromit.menuvalue.length) index = 0;          // limit the bounds of index
                if (index < 0) index = gromit.menuvalue.length - 1 ;

                // increase or decrease the values
                if (gamepad1.b) {
                    if (bisreleased) {
                        bisreleased = false;
                        gromit.menuvalue[index] += 1;
                    }
                } else {
                    bisreleased = true;
                }
                if (gamepad1.x) {
                    if (xisreleased) {
                        xisreleased = false;
                        gromit.menuvalue[index] -= 1;
                    }
                } else {
                    xisreleased = true;
                }

                // limit the values of some buttons
                if (gromit.menuvalue[0] >  1) gromit.menuvalue[0] = 0;          //team color is boolean
                if (gromit.menuvalue[0] <  0) gromit.menuvalue[0] = 1;          //team color is boolean
                if (gromit.menuvalue[1] >= 7) gromit.menuvalue[1] = 0;          //modes 0,1,2,3,4,5,6
                if (gromit.menuvalue[1] <  0) gromit.menuvalue[1] = 6;          //
                if (gromit.menuvalue[2] >= 3) gromit.menuvalue[2] = 0;          //Particles, 0,1,2
                if (gromit.menuvalue[2] <  0) gromit.menuvalue[2] = 2;          //
                if (gromit.menuvalue[3] <  0) gromit.menuvalue[3] = 0;          // Delay can't be negative
                if (gromit.menuvalue[20] >  1) gromit.menuvalue[20] = 0;          // Cap Ball boolean
                if (gromit.menuvalue[20] <  0) gromit.menuvalue[20] = 1;          //
                if (gromit.menuvalue[21] >  1) gromit.menuvalue[21] = 0;          //Pick up Partner Ball boolean
                if (gromit.menuvalue[21] <  0) gromit.menuvalue[21] = 1;          //


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
        try {
              FileWriter fw = new FileWriter(filePath);
              for ( i=0; i<=gromit.menuvalue.length-1; i++){
                fw.write((gromit.menulabel[i]));
                fw.write(System.lineSeparator());
                fw.write(Integer.toString(gromit.menuvalue[i]));
                fw.write(System.lineSeparator());
              }
//            fw.write(values);
//            fw.write(System.lineSeparator());
            fw.close();

        } catch (IOException ex) {
            System.err.println("Couldn't write this file: "+filePath);
           }
        telemetry.addData("ConfigFile saved to", filePath);
        telemetry.update();
        sleep(500);



    }


}
