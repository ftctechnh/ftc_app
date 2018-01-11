package org.firstinspires.ftc.teamcode.Qualifier;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Qualifier.DriveTrain.SpeedSetting.FAST;
import static org.firstinspires.ftc.teamcode.Qualifier.DriveTrain.SpeedSetting.SLOW;
import static java.lang.Math.atan;
import static java.lang.Math.toDegrees;


@TeleOp(name = "zMoo", group = "8045")  // @Autonomous(...) is the other common choice
//@Disabled
public class Teleop extends OpMode {

    RobotRR gromit;
    double turnDirection;

    private ElapsedTime runtime = new ElapsedTime();
    double timeLeft;

    public boolean backIsReleased = true;
    public boolean rightbumperIsReleased = true;
    public boolean righttriggerIsReleased = true;

    boolean loadedLastTime = false;

    double lastLoadTime;

    @Override
    public void init() {

        gromit = new RobotRR();
        gromit.init(hardwareMap);
        telemetry.addData("Status", "Initialized");

        turnDirection = 1;
        timeLeft = 120;

//       lastLoadTime = -10000;
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        timeLeft = 120 - runtime.seconds();
        //------------------------------------------------------------------------------
        //toggle  drive direction-when the button was released and it is now pressed.
        //------------------------------------------------------------------------------
        if(gamepad1.back) {
            if (backIsReleased) {
                backIsReleased = false;
                gromit.driveTrain.frontIsForward = !gromit.driveTrain.frontIsForward;
            }
        } else {
            backIsReleased = true;
        }
        //------------------------------------------------------------------------------
        //------------------------------------------------------------------------------
        //Lift/lower glyph
        //------------------------------------------------------------------------------
       if(gamepad1.y) {
            if (rightbumperIsReleased) {
                rightbumperIsReleased = false;

                gromit.glyphTrain.liftIndex = Math.min(gromit.glyphTrain.liftIndex + 1, 2);   //add one to index, max is 2
                gromit.glyphTrain.liftGlyphIndex(gromit.glyphTrain.liftIndex);  //lift
                //gromit.glyphTrain.liftGlyph(6);
            }
        } else {
            rightbumperIsReleased = true;
        }
       if(gamepad1.a) {
            if (righttriggerIsReleased) {
                righttriggerIsReleased = false;
                //gromit.glyphTrain.lowerGlyph(6);
                gromit.glyphTrain.liftIndex = Math.max(gromit.glyphTrain.liftIndex - 1, 0);  //subtract one from index, min is 0
                gromit.glyphTrain.liftGlyphIndex(gromit.glyphTrain.liftIndex);  //lower
            }
        } else {
            righttriggerIsReleased = true;
        }
        telemetry.addData("liftticks",gromit.glyphTrain.lift_motor.getCurrentPosition() );
        //------------------------------------------------------------------------------
        // glyph lift
        if (gamepad1.dpad_up) {
            if(gromit.glyphTrain.lift_motor.getCurrentPosition() < gromit.glyphTrain.upperLiftLimit){
                gromit.glyphTrain.lift_motor.setPower(1.0);
            }else {
                gromit.glyphTrain.lift_motor.setPower(0);
            }
        } else if (gamepad1.dpad_down) {
            if(gromit.glyphTrain.lift_motor.getCurrentPosition() > gromit.glyphTrain.lowerLiftLimit){
                gromit.glyphTrain.lift_motor.setPower(-0.8);
            }else {
                gromit.glyphTrain.lift_motor.setPower(0);
            }

        } else {
            gromit.glyphTrain.lift_motor.setPower(0);
        }

        //set drive speed
        if (gamepad1.left_bumper) {
            gromit.driveTrain.setSpeedMode(FAST);
        } else {
            gromit.driveTrain.setSpeedMode(SLOW);
        }
//on and off glyph intake
        if (gamepad1.b) {
            gromit.glyphTrain.startGlyphMotors(1.0);

        } else if (gamepad1.x) {
            gromit.glyphTrain.stopGlyphMotors();
        }
        // glyph clamp
        if (gamepad1.dpad_left) {
            gromit.glyphTrain.glyphclamp("open");
        } else if (gamepad1.dpad_right) {
            gromit.glyphTrain.glyphclamp("close");
        }


//        if (gamepad1.a){
//            gromit.relicArm.relicClawServo.setPosition(.4);
//        }
//        else if (gamepad1.y){
//            gromit.relicArm.relicClawServo.setPosition(.6);
//        }

        if (gamepad1.right_bumper){
            gromit.relicArm.relicClawServo.setPosition(.45);
        }
        else if (gamepad1.left_bumper){
            gromit.relicArm.relicClawServo.setPosition(.55);
        }
        //Set drive train direction
//        if (gamepad1.a)
//            gromit.driveTrain.setBack(DriveTrain.Color.GREEN);
//        if (gamepad1.y)
//            gromit.driveTrain.setBack(DriveTrain.Color.YELLOW);
//        if (gamepad1.b)
//            gromit.driveTrain.setBack(DriveTrain.Color.RED);
//        if (gamepad1.x)
//            gromit.driveTrain.setBack(DriveTrain.Color.BLUE);

        gromit.driveTrain.drive(gamepad1.right_stick_x, -gamepad1.right_stick_y, turnDirection * gamepad1.left_stick_x);
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
//        if (gamepad1.left_bumper) {
//            gromit.jewelArm.leftOut();
//            gromit.jewelArm.rightOut();
//        }
//        if (gamepad1.left_trigger>0.5){
//            gromit.jewelArm.leftIn();
//            gromit.jewelArm.rightIn();
//        }
        /*if (gamepad1.y) {
            gromit.jewelArm.jewelArmUp();
        }
        if (gamepad1.a){
            gromit.jewelArm.jewelArmDown();
        }*/
        double vector = toDegrees(atan(gamepad1.right_stick_y / gamepad1.right_stick_x)) + 90;
        telemetry.addData("vector: ",  vector);
        telemetry.addLine("Time Left: " + timeLeft);
        telemetry.addData("liftindex",gromit.glyphTrain.liftIndex);

    }

    @Override
    public void stop() {
        //       gromit.shooter.turnOff();
    }

}
