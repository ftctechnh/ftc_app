
package RicksCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name="Method Test", group="Pushbot")
@Disabled

public class MethodTest extends LinearOpMode {

     /* Declare OpMode members. */
     // Hardware_8045Worlds gromit    = new Hardware_8045Worlds();   // Use a 8045 hardware
     public static int[] menuvalues;
     public static String[] menulabels;
     //public static int[] menuvalues = {10, 20, 30, 40};
     //public static String[] menulabels = {"Red", "yellow", "blue", "green",};

     @Override
     public void runOpMode() {
         /**********************************************************************************************\
          |--------------------------------- Pre Init Loop ----------------------------------------------|
          \**********************************************************************************************/
         // gromit.init(hardwareMap);

         // Send telemetry message to signify robot waiting;
         telemetry.addLine(" The Pre - init Loop");
//         for (int i = 0; i < menuvalues.length; i++) {
//             telemetry.addLine().addData(menulabels[i], menuvalues[i]);
//         }
         telemetry.update();
         sleep(3000);
         /**********************************************************************************************\
          |--------------------------------------Init Loop-----------------------------------------------|
          \**********************************************************************************************/
         Method1("NameofFile");

         while (!isStarted()) {
             telemetry.addLine("Now in the INIT Loop");    //
             for (int i = 0; i < menuvalues.length; i++) {
                 telemetry.addLine().addData(menulabels[i], menuvalues[i]);
             }
             telemetry.update();

         }

            telemetry.clear();
          /**********************************************************************************************\
          |-------------------------------------START OF GAME--------------------------------------------|
          \**********************************************************************************************/
         resetStartTime();      // start timer

         while (opModeIsActive()) {
             telemetry.addLine("Now in the Main Loop");    //
             for (int i = 0; i < menuvalues.length; i++) {
                 telemetry.addLine().addData(menulabels[i], menuvalues[i]);
             }
             telemetry.update();

             idle();
         }


     }


    public void Method1(String fileName) {
        telemetry.addLine("Now in the Method1");    //
        int[] menuvalues = {10, 20, 30, 40};
        String[] menulabels = {"Red", "yellow", "blue", "green",};

        menuvalues[3] = 90;
        for (int i = 0; i < menuvalues.length; i++) {
            telemetry.addLine().addData(menulabels[i], menuvalues[i]);
        }
        telemetry.update();
        sleep(4000);

    }
 }
