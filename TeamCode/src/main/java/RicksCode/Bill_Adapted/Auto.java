package RicksCode.Bill_Adapted;

/**
 * Created by ftc8045 on 10/22/2017.
 */


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Tele;


@Autonomous(name="AUTOTEST",group="zRick")  // @Autonomous(...) is the other common choice
//@Disabled
public class Auto extends LinearOpMode {

    RobotRR gromit;

    private ElapsedTime runtime = new ElapsedTime();
    private ConfigFileHandler configFile;


    @Override
    public void runOpMode() {
        gromit = new RobotRR();
        gromit.init(hardwareMap);

        configFile = new ConfigFileHandler(telemetry);

        if (!gamepad1.back) {
            configFile.readDataFromTxtFile(hardwareMap.appContext);
        } else{
            configFile.initializeValues();
        }
        sleep(5000);
        //configFile.writeDataToFile(hardwareMap.appContext);
        configFile.writeDataToTxtFile(hardwareMap.appContext);

        int i=0;

        for ( i=0; i < configFile.menulabel.length; i++){
            telemetry.addData(configFile.menulabel[i], configFile.menuvalue[i]);
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
    

