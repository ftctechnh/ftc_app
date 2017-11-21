package Tristan;

/**
 *
 */


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import RicksCode.Bill_Adapted.MenuFileHandler;
import RicksCode.Bill_Adapted.RobotRR;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AUTOTristan",group="Tristan")  // @Autonomous(...) is the other common choice
//@Disabled
public class Autonomous extends LinearOpMode {

    Tristan.RobotRR gromit;

    private ElapsedTime runtime = new ElapsedTime();
    private MenuFileHandler menuFile;


    @Override
    public void runOpMode() {
        gromit = new Tristan.RobotRR();
        gromit.init(hardwareMap);

        menuFile = new MenuFileHandler(telemetry, gamepad1);

        /**********************************************************************************************\
         |--------------------------------- Pre Init Loop ----------------------------------------------|
         \**********************************************************************************************/
        telemetry.addLine("Back Button reverts to code values");
        telemetry.update();
        sleep(1000);
        menuFile.initializeValues();       // initialize variables & values

        // update them from the file if the back button is not pressed

        if (!gamepad1.back) {
            menuFile.readDataFromTxtFile(hardwareMap.appContext);
            telemetry.addLine("Reading Data from File" );
            telemetry.update();
            sleep(1000);
        }else {
            telemetry.addLine("Reverting to Initial Values");
            telemetry.update();
            sleep(1000);
        }

        sleep(3000);
        menuFile.editParameters();                            // edit parameters
        menuFile.writeDataToTxtFile(hardwareMap.appContext);  // write the current parameters to the file for next time
        menuFile.updateVariables();   // transfer the array to variables with useable names






        /**********************************************************************************************\
         |--------------------------------------Init Loop-----------------------------------------------|
         \**********************************************************************************************/
        //while (!isStarted()) {  // could add a loop here if you might want to go back to edit mode...

            telemetry.addLine("************** Ready to RUN **************" );
            telemetry.addLine("####    " + menuFile.menuvaluetoken[0][menuFile.menuvalue[0]]+" " +menuFile.menuvaluetoken[1][menuFile.menuvalue[1]]+" " +menuFile.menuvaluetoken[2][menuFile.menuvalue[2]] +"   ####");
            for ( int i=0; i < menuFile.menulabel.length; i++){
                if (menuFile.menuupperlimit[i] < 5) {                           // menu items that need tokens should be less than 5
                    telemetry.addLine().addData(menuFile.menulabel[i], menuFile.menuvalue[i] + "  " + menuFile.menuvaluetoken[i][menuFile.menuvalue[i]] );
                }else{
                    telemetry.addData(menuFile.menulabel[i], menuFile.menuvalue[i]);
                }
            }
            telemetry.addLine("*************** Ready to RUN **************" );
            telemetry.update();

            idle();
        //}

        /**********************************************************************************************\
         |----------------------------------- Run the Autonomous --------------------------------------|
         \*********************************************************************************************/


        waitForStart();
        while (opModeIsActive()) {
            telemetry.update();
            //menuFile.testGamepad();
        }

//        gromit.driveTrain.drive(0,0,0);


    }
}
    

