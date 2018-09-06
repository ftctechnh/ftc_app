package RicksCode.Bill_VV;

/**
 * Created by ftc8045 on 10/22/2017.
 */


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

    @Autonomous(name="AUTOTESTB")  // @Autonomous(...) is the other common choice
    @Disabled
    public class AutonShoot extends LinearOpMode {

        RobotRR gromit;
        boolean allianceIsRed;
        double xDirection;
        private ElapsedTime runtime = new ElapsedTime();

        public AutonShoot(boolean isRed) {
            allianceIsRed = isRed;
            if (allianceIsRed)
                xDirection = 1;
            else
                xDirection = -1;
        }

        @Override
        public void runOpMode() {
            gromit = new RobotRR();
            gromit.init(hardwareMap);

            ParameterFileHandler parameterFile;
            parameterFile = new ParameterFileHandler();
            parameterFile.readDataFromFile(hardwareMap.appContext);

            waitForStart();

            sleep(parameterFile.waitTime);

 //           gromit.shooter.turnOn();
 //           gromit.sweeper.sweepIn();

            gromit.driveTrain.drive(0,-parameterFile.driveSpeed,0);
            //sleep(750);
            double tempDouble = parameterFile.shooterForwardTime;
            sleep((long)tempDouble);
            gromit.driveTrain.stop();
            sleep(2000);

//            gromit.loader.raise();
//            sleep(gromit.loader.timeToRaise);
//            gromit.loader.lower();
//            sleep(gromit.loader.timeToLower);
//            gromit.loader.raise();
//            sleep(gromit.loader.timeToRaise);
//            gromit.loader.lower();

            tempDouble = parameterFile.shooterWait;
            sleep((long)tempDouble);


            gromit.driveTrain.drive(0,-parameterFile.driveSpeed,0);
            tempDouble = parameterFile.shooterForwardAfterShoot;
            sleep((long)tempDouble);


//            gromit.shooter.turnOff();
//            gromit.sweeper.stop();
/*
        gromit.driveTrain.drive(0,0,xDirection* parameterFile.driveSpeed); //Turn the robot
        sleep(175);    //parameterFile.shooterTurnValue
        gromit.driveTrain.drive(xDirection* parameterFile.driveSpeed,0,0);
        sleep(1000);    //parameterFile.shooterStrafeValue
        gromit.driveTrain.drive(0,-parameterFile.driveSpeed,0);
        sleep(2000);
        gromit.driveTrain.stop();
        */
        }
    }
    

