package RicksCode.Bill_VV;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "zChangeParameters", group = "Test")
@Disabled
public class ChangeAutonDistances extends OpMode {

    ParameterFileHandler parameterFile;

    @Override
    public void init() {
        parameterFile = new ParameterFileHandler();
        parameterFile.readDataFromFile(hardwareMap.appContext);
    }

    @Override
    public void init_loop() {

        telemetry.addData("A Left Trig = less, Bumper = more, DELAY START BY:", parameterFile.waitTime);
        telemetry.addData("X is less, B is more, SHOOTER WAIT:", parameterFile.shooterWait);
        telemetry.addData("LEFT_TRIGGER(2) is less, LEFT_BUMPER(2) is more, SHOOTER FORWARD AFTER SHOOT:", parameterFile.shooterForwardAfterShoot);
        telemetry.addData("RIGHT_TRIGGER(2) is less, RIGHT_BUMPER(2) is more, SHOOTER FORWARD TIME:", parameterFile.shooterForwardTime);
        telemetry.addData("A DPad_Up = more, DPad_down = less, DRIVE SPEED", parameterFile.driveSpeed);
        telemetry.addData("Dpad_Left is more, Dpad_Right is less WHITE COLOR" , parameterFile.whiteColor);
        telemetry.addData("Y is more, A is less DRIVE BACK_DIST" , parameterFile.driveBackTime);




        if (gamepad2.left_bumper && gamepad2.right_bumper)
            parameterFile.initializeValues();

        if(gamepad1.y)
            parameterFile.driveBackTime += .5;
        if(gamepad1.a)
            parameterFile.driveBackTime -= .5;

        //Wait Time at the start
        if (gamepad1.left_bumper) {
            parameterFile.waitTime += 4;
        }

        if (gamepad1.left_trigger > .75) {
            parameterFile.waitTime -= 4;
            if (parameterFile.waitTime < 0) {
                parameterFile.waitTime = 0;
            }
        }

        if (gamepad1.dpad_down) {
            parameterFile.driveSpeed -= .001;
        }

        if (gamepad1.dpad_up) {
            parameterFile.driveSpeed += .001;
        }

        if(gamepad1.dpad_left)
        {
            parameterFile.whiteColor += .05;
        }
        if(gamepad1.dpad_right)
        {
            parameterFile.whiteColor -= .05;
        }

        if(gamepad2.x)
        {
            parameterFile.shooterWait -= 2.5;
            if(parameterFile.shooterWait <= 0)
                parameterFile.shooterWait = 0.0;
        }
        if(gamepad2.b)
        {
            parameterFile.shooterWait += 2.5;
        }





        if(gamepad2.right_trigger > .75)
        {
            parameterFile.shooterForwardTime -= 2.5;
            if(parameterFile.shooterForwardTime <=0)
                parameterFile.shooterForwardTime = 0.0;
        }
        if(gamepad2.right_bumper)
        {
            parameterFile.shooterForwardTime += 2.5;
        }



        if(gamepad2.left_bumper)
        {
            parameterFile.shooterForwardAfterShoot += 2.5;
            if(parameterFile.shooterForwardAfterShoot <= 0)
                parameterFile.shooterForwardAfterShoot = 0.0;
        }
        if(gamepad2.left_trigger > .75)
        {
            parameterFile.shooterForwardAfterShoot -= 2.5;
        }

    }

    @Override
    public void start() {
        parameterFile.writeDataToFile(hardwareMap.appContext);
    }

    @Override
    public void loop() {

    }
}