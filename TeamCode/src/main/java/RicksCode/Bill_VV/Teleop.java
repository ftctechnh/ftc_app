package RicksCode.Bill_VV;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import static RicksCode.Bill_VV.DriveTrain.SpeedSetting.FAST;
import static RicksCode.Bill_VV.DriveTrain.SpeedSetting.SLOW;

@TeleOp(name = "zMoo")  // @Autonomous(...) is the other common choice
@Disabled
public class Teleop extends OpMode {
    
    RobotRR gromit;
    double turnDirection;

    private ElapsedTime runtime = new ElapsedTime();
    double timeLeft;

    boolean loadedLastTime = false;

    double lastLoadTime;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        gromit = new RobotRR();
        gromit.init(hardwareMap);

        turnDirection=1;
        timeLeft=120;

//       lastLoadTime = -10000;
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        timeLeft = 120 - runtime.seconds();
        
        //set drive speed
        if (gamepad1.left_stick_button || gamepad1.right_stick_button)
            gromit.driveTrain.setSpeedMode(SLOW);
        else
            gromit.driveTrain.setSpeedMode(FAST);
        
        //Set drive train direction
//        if (gamepad1.a)
//            gromit.driveTrain.setBack(DriveTrain.Color.GREEN);
//        if (gamepad1.y)
//            gromit.driveTrain.setBack(DriveTrain.Color.YELLOW);
//        if (gamepad1.b)
//            gromit.driveTrain.setBack(DriveTrain.Color.RED);
//        if (gamepad1.x)
//            gromit.driveTrain.setBack(DriveTrain.Color.BLUE);

        gromit.driveTrain.drive(gamepad1.right_stick_x, -gamepad1.right_stick_y, turnDirection* gamepad1.left_stick_x);

        //Shooter on and off
//        if (gamepad1.dpad_up)
//            gromit.shooter.turnOn();
//        if (gamepad1.dpad_down)
//            gromit.shooter.turnOff();
//        if (gamepad2.dpad_up)
//            gromit.shooter.turnOn();
//        if (gamepad2.dpad_down)
//            gromit.shooter.turnOff();
//        if (gamepad2.dpad_left)
//            gromit.shooter.turnOnNoEncoder();

//        if (!gromit.shooter.usingEncoders) {
//
//            if (runtime.milliseconds() < lastLoadTime + 1000)
//                gromit.shooter.turnOnNoEncoderHigher();
//            else
//                gromit.shooter.turnOnNoEncoder();
//
//
//        }



//        //Sweeper on and off
//        if (gamepad2.right_stick_y<-.5)
//            gromit.sweeper.sweepOut();
//        else if (gamepad2.right_stick_y>0.5)
//            gromit.sweeper.sweepIn();
//        else
//            gromit.sweeper.stop();
//
//        //Loader raise and lower
//        if (gamepad2.right_bumper)
//            gromit.loader.raise();
//        else
//            gromit.loader.lower();
//
//        if (gamepad2.right_bumper && !loadedLastTime)
//            lastLoadTime = runtime.milliseconds();
//
//        loadedLastTime = gamepad2.right_bumper;

        //Beacon pusher in and out
        if (gamepad1.left_bumper) {
            gromit.jewelArm.leftOut();
            gromit.jewelArm.rightOut();
        }
        if (gamepad1.left_trigger>0.5){
            gromit.jewelArm.leftIn();
            gromit.jewelArm.rightIn();
        }
        if (gamepad2.y) {
            gromit.jewelArm.leftOut();
            gromit.jewelArm.rightOut();
        }
        if (gamepad2.a){
            gromit.jewelArm.leftIn();
            gromit.jewelArm.rightIn();
        }
        if (gamepad2.x)
            gromit.jewelArm.leftOut();
        if (gamepad2.b)
            gromit.jewelArm.rightOut();

        //telemetry.addData("Status", "Running: " + runtime.toString());
        telemetry.addLine("Time Left: " + timeLeft );
    }

    @Override
    public void stop() {
 //       gromit.shooter.turnOff();
    }

}
