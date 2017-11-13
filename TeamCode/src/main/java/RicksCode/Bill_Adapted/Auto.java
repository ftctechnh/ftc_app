package RicksCode.Bill_Adapted;

/**
 * Created by ftc8045 on 10/22/2017.
 */


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="AUTOTEST",group="zRick")  // @Autonomous(...) is the other common choice
//@Disabled
public class Auto extends LinearOpMode {

    RobotRR gromit;

    private ElapsedTime runtime = new ElapsedTime();



    @Override
    public void runOpMode() {
        gromit = new RobotRR();
        gromit.init(hardwareMap);

        ConfigFileHandler configFile;
        configFile = new ConfigFileHandler();
        if (!gamepad1.back) {
            configFile.readDataFromTxtFile(hardwareMap.appContext);
        } else{
            configFile.initializeValues();
        }

        //configFile.writeDataToFile(hardwareMap.appContext);
        configFile.writeDataToTxtFile(hardwareMap.appContext);

        int i=0;

        for ( i=0; i < configFile.menulabel.length; i++){
            telemetry.addData(configFile.menulabel[0], configFile.menuvalue[0]);
        }
//        telemetry.addData("SHOOTER WAIT:", configFile.menuvalue[1]);
//        telemetry.addData("SHOOTER FORWARD AFTER SHOOT:", configFile.menuvalue[2]);
//        telemetry.addData("SHOOTER FORWARD TIME:", configFile.menuvalue[3]);
//        telemetry.addData("DRIVE SPEED", configFile.menuvalue[4]);
//        telemetry.addData("WHITE COLOR" , configFile.menuvalue[5]);
//        telemetry.addData("DRIVE BACK_DIST" , configFile.menuvalue[6]);
        telemetry.update();

        waitForStart();

//           gromit.shooter.turnOn();
//           gromit.sweeper.sweepIn();

//        gromit.driveTrain.drive(0,0,0);


    }
}
    

