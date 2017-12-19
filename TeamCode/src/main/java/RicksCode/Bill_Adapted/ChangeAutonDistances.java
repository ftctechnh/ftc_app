package RicksCode.Bill_Adapted;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import RicksCode.Bill_VV.ParameterFileHandler;


@TeleOp(name = "zChangeParameters", group = "zRick")
@Disabled
public class ChangeAutonDistances extends OpMode {

    ConfigFileHandler parameterFile;

    @Override
    public void init() {
        parameterFile = new ConfigFileHandler(telemetry, gamepad1);
        parameterFile.readDataFromTxtFile(hardwareMap.appContext);

    }

    @Override
    public void init_loop() {
        int i = 0;
        for ( i=0; i < parameterFile.menulabel.length; i++){
            telemetry.addData(parameterFile.menulabel[i], parameterFile.menuvalue[i]);
            //telemetry.addData(parameterFile.menulabel[i], parameterFile.menuvaluetoken[i][parameterFile.menuvalue[i]]);
        }

//        telemetry.addData("A Left Trig = less, Bumper = more, DELAY START BY:", parameterFile.menuvalue[0]);
//        telemetry.addData(" ",parameterFile.menulabel[0], parameterFile.menuvalue[0]);
//        telemetry.addData("X is less, B is more, SHOOTER WAIT:", parameterFile.menuvalue[1]);
//        telemetry.addData(" ",parameterFile.menulabel[1], parameterFile.menuvalue[1]);
//        telemetry.addData("LEFT_TRIGGER(2) is less, LEFT_BUMPER(2) is more, SHOOTER FORWARD AFTER SHOOT:", parameterFile.menuvalue[2]);
//        telemetry.addData(" ",parameterFile.menulabel[2], parameterFile.menuvalue[2]);
//        telemetry.addData("RIGHT_TRIGGER(2) is less, RIGHT_BUMPER(2) is more, SHOOTER FORWARD TIME:", parameterFile.menuvalue[3]);
//        telemetry.addData(" ",parameterFile.menulabel[3], parameterFile.menuvalue[3]);
//        telemetry.addData("A DPad_Up = more, DPad_down = less, DRIVE SPEED", parameterFile.menuvalue[4]);
//        telemetry.addData(" ",parameterFile.menulabel[4], parameterFile.menuvalue[4]);
//        telemetry.addData("Dpad_Left is more, Dpad_Right is less WHITE COLOR" , parameterFile.menuvalue[5]);
//        telemetry.addData(" ",parameterFile.menulabel[5], parameterFile.menuvalue[5]);
//        telemetry.addData("Y is more, A is less DRIVE BACK_DIST" , parameterFile.menuvalue[6]);
//        telemetry.addData(" ",parameterFile.menulabel[6], parameterFile.menuvalue[6]);




        if (gamepad2.left_bumper && gamepad2.right_bumper)
            parameterFile.initializeValues();

        if(gamepad1.y)
            parameterFile.menuvalue[6] += 1;
        if(gamepad1.a)
            parameterFile.menuvalue[6] -= 1;
//
//        //Wait Time at the start
//        if (gamepad1.left_bumper) {
//            parameterFile.waitTime += 4;
//        }
//
//        if (gamepad1.left_trigger > .75) {
//            parameterFile.waitTime -= 4;
//            if (parameterFile.waitTime < 0) {
//                parameterFile.waitTime = 0;
//            }
//        }
//
//        if (gamepad1.dpad_down) {
//            parameterFile.driveSpeed -= .001;
//        }
//
//        if (gamepad1.dpad_up) {
//            parameterFile.driveSpeed += .001;
//        }
//
//        if(gamepad1.dpad_left)
//        {
//            parameterFile.whiteColor += .05;
//        }
//        if(gamepad1.dpad_right)
//        {
//            parameterFile.whiteColor -= .05;
//        }
//
//        if(gamepad1.x)
//        {
//            parameterFile.shooterWait -= 2;
//            if(parameterFile.shooterWait <= 0)
//                parameterFile.shooterWait = 0;
//        }
//        if(gamepad1.b)
//        {
//            parameterFile.shooterWait += 2;
//        }
//
//        if(gamepad2.right_trigger > .75)
//        {
//            parameterFile.shooterForwardTime -= 2;
//            if(parameterFile.shooterForwardTime <=0)
//                parameterFile.shooterForwardTime = 0;
//        }
//        if(gamepad2.right_bumper)
//        {
//            parameterFile.shooterForwardTime += 2;
//        }
//        if(gamepad2.left_bumper)
//        {
//            parameterFile.shooterForwardAfterShoot += 2;
//            if(parameterFile.shooterForwardAfterShoot <= 0)
//                parameterFile.shooterForwardAfterShoot = 0;
//        }
//        if(gamepad2.left_trigger > .75)
//        {
//            parameterFile.shooterForwardAfterShoot -= 2;
//        }

    }

    @Override
    public void start() {
        parameterFile.writeDataToTxtFile(hardwareMap.appContext);
    }

    @Override
    public void loop() {

    }
}