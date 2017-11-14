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
    private MenuFileHandler menuFile;


    @Override
    public void runOpMode() {
        gromit = new RobotRR();
        gromit.init(hardwareMap);

        menuFile = new MenuFileHandler(telemetry, gamepad1);

        if (!gamepad1.back) {
            menuFile.readDataFromTxtFile(hardwareMap.appContext);
        } else{
            menuFile.initializeValues();
        }
        //sleep(5000);
        //menuFile.writeDataToFile(hardwareMap.appContext);
        menuFile.writeDataToTxtFile(hardwareMap.appContext);

        int i=0;

        for ( i=0; i < menuFile.menulabel.length; i++){
            telemetry.addData(menuFile.menulabel[i], menuFile.menuvalue[i]);
        }
        telemetry.update();

        waitForStart();
        while (opModeIsActive()) {
            telemetry.update();
            menuFile.testGamepad();
        }

//        gromit.driveTrain.drive(0,0,0);


    }
}
    

