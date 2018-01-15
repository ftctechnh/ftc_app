package org.firstinspires.ftc.teamcode.Qualifier;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static org.firstinspires.ftc.teamcode.Qualifier.DriveTrain.SpeedSetting.FAST;
import static org.firstinspires.ftc.teamcode.Qualifier.DriveTrain.SpeedSetting.MID;
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
    public boolean tristanmode = false;
    public boolean rightbtnIsReleased = true;
    public boolean xIsReleased = true;
    //public boolean aIsReleased = true;
    public boolean rightbumperIsReleased = true;
    public boolean righttriggerIsReleased = true;
    public boolean glyphLiftismoving = false;
    public boolean padupIsReleased = true;
    public boolean paddownIsReleased = true;
    public boolean aIsReleased = true;

    public int liftTarget = 0;
    public boolean trainon = false;

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

//        double maxVoltage = gromit.driveTrain.maxbotixSensor.getVoltage();
//        telemetry.addData("maxbotix ",maxVoltage);


        //------------------------------------------------------------------------------
        //toggle  drive direction-when the button was released and it is now pressed.
        //------------------------------------------------------------------------------
        if (gamepad1.right_stick_button) {
            if (rightbtnIsReleased) {
                rightbtnIsReleased = false;
                gromit.driveTrain.frontIsForward = !gromit.driveTrain.frontIsForward;
            }
        } else {
            rightbtnIsReleased = true;
        }
        //------------------------------------------------------------------------------
        //------------------------------------------------------------------------------
        //Lift/lower glyph  x/a by increments,   y/b manually
        //------------------------------------------------------------------------------
        if (gamepad1.x || gamepad2.x) {
            if (xIsReleased) {
                gromit.glyphTrain.glyphclamp("close");
                xIsReleased = false;
                gromit.glyphTrain.liftIndex = Math.min(gromit.glyphTrain.liftIndex + 1, 2);   //add one to index, max is 2
//  check to see if you're lower than the next lower position by 400 ticks, stop there first.
                if (gromit.glyphTrain.lift_motor.getCurrentPosition() + 200 < gromit.glyphTrain.liftPosition[gromit.glyphTrain.liftIndex - 1 ] ){
                    gromit.glyphTrain.liftIndex -= 1;
                }
                liftTarget = gromit.glyphTrain.liftPosition[gromit.glyphTrain.liftIndex];  // set the new Target
                glyphLiftismoving = true;
                gromit.glyphTrain.lift_motor.setPower(1.0);   // start the motor going up
            }
        } else {
            xIsReleased = true;
        }
        if (gamepad1.a || gamepad2.a) {
            if (aIsReleased) {
                aIsReleased = false;
                //gromit.glyphTrain.lowerGlyph(6);
                glyphLiftismoving = true;
                gromit.glyphTrain.liftIndex = 0;  //down always goes to zero
                liftTarget = 0;
                gromit.glyphTrain.glyphclamp("open");   // might as well open when lowering
                gromit.glyphTrain.lift_motor.setPower(-0.9);   // start the motor going down
//                gromit.glyphTrain.liftIndex = Math.max(gromit.glyphTrain.liftIndex - 1, 0);  //subtract one from index, min is
//                gromit.glyphTrain.liftGlyphIndex(gromit.glyphTrain.liftIndex);  //lower
            }
        } else {
            aIsReleased = true;
        }
        // check if target is reached yet
        if (glyphLiftismoving) {
            if (liftTarget == 0) {  // going down
                if (gromit.glyphTrain.lift_motor.getCurrentPosition() <= liftTarget) {
                    gromit.glyphTrain.lift_motor.setPower(0.0);
                    glyphLiftismoving = false;
                }
            } else {
                if (gromit.glyphTrain.lift_motor.getCurrentPosition() >= liftTarget) {
                    //gromit.glyphTrain.lift_motor.setPower(0.0);
                    glyphLiftismoving = false;
                }
           }
        }


        //------------------------------------------------------------------------------
        // glyph lift
        if (gamepad1.y || gamepad2.y) {
            glyphLiftismoving = false;
            if (gromit.glyphTrain.lift_motor.getCurrentPosition() < gromit.glyphTrain.upperLiftLimit) {
                gromit.glyphTrain.lift_motor.setPower(1.0);
            } else {
                gromit.glyphTrain.lift_motor.setPower(0);
            }
        } else if (gamepad1.b || gamepad2.b) {
            glyphLiftismoving = false;
            if (gromit.glyphTrain.lift_motor.getCurrentPosition() > gromit.glyphTrain.lowerLiftLimit) {
                gromit.glyphTrain.lift_motor.setPower(-0.8);
            } else {
                gromit.glyphTrain.lift_motor.setPower(0);
            }

        } else if (!glyphLiftismoving)  {
            gromit.glyphTrain.lift_motor.setPower(0);
        }

        //set drive speed
        if (gamepad1.left_bumper) {
            gromit.driveTrain.setSpeedMode(FAST);
        } else if (gamepad1.left_trigger > 0.1) {
            gromit.driveTrain.setSpeedMode(SLOW);
        } else {
            gromit.driveTrain.setSpeedMode(MID);
        }
//on and off glyph intake
        if (gamepad1.dpad_down || gamepad2.dpad_down) {
            if(paddownIsReleased) {
                paddownIsReleased = false;
                if (trainon) {
                    trainon = false;
                    gromit.glyphTrain.stopGlyphMotors();
                } else {
                    trainon = true;
                    gromit.glyphTrain.startGlyphMotors(1.0);
                }
            }
        }
        else{
            paddownIsReleased = true;
        }
        //BACKWARDS
        if (gamepad1.dpad_up || gamepad2.dpad_up) {
            if(padupIsReleased) {
                padupIsReleased = false;
                if (trainon) {
                    trainon = false;
                    gromit.glyphTrain.stopGlyphMotors();
                } else {
                    trainon = true;
                    gromit.glyphTrain.startGlyphMotors(-0.2);
                }
            }
        }
        else{
            padupIsReleased =true;
        }
        // glyph clamp
        if (gamepad1.right_trigger > 0.1 || gamepad2.right_trigger > 0.1) {
            gromit.glyphTrain.glyphclamp("open");
        } else if (gamepad1.right_bumper || gamepad2.right_bumper) {
            gromit.glyphTrain.glyphclamp("close");
        }


//        if (gamepad1.a){
//            gromit.relicArm.relicClawServo.setPosition(.4);
//        }
//        else if (gamepad1.y){
//            gromit.relicArm.relicClawServo.setPosition(.6);
//        }

//        if (gamepad1.right_bumper){
//            gromit.relicArm.relicClawServo.setPosition(.45);
//        }
//        else if (gamepad1.left_bumper){
//            gromit.relicArm.relicClawServo.setPosition(.55);
//        }
        //Set drive train direction
//        if (gamepad1.a)
//            gromit.driveTrain.setBack(DriveTrain.Color.GREEN);
//        if (gamepad1.y)
//            gromit.driveTrain.setBack(DriveTrain.Color.YELLOW);
//        if (gamepad1.b)
//            gromit.driveTrain.setBack(DriveTrain.Color.RED);
//        if (gamepad1.x)
//            gromit.driveTrain.setBack(DriveTrain.Color.BLUE);
        if (gamepad1.back) {
            if (backIsReleased) {
                backIsReleased = false;
                if(tristanmode) {
                    tristanmode = false;
                }
                else{tristanmode = true;}

            }
        }
        else {
            backIsReleased = true;
        }

        if(tristanmode) {
            gromit.driveTrain.drivevector(gamepad1.right_stick_x, -gamepad1.right_stick_y, turnDirection * gamepad1.left_stick_x);
        }
        else{
            gromit.driveTrain.drivesmart(-gamepad1.right_stick_x, -gamepad1.right_stick_y, turnDirection * gamepad1.left_stick_x);
        }
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
        double vector = toDegrees(PI/4+atan(gamepad1.right_stick_y / gamepad1.right_stick_x));
        telemetry.addData("vector: ", vector);
        /*double drive_direction = atan(y/x);
        lfpower = signum(y)*Math.cos(drive_direction-PI/4);
        lrpower = signum(y)*Math.sin(drive_direction-PI/4);
        rfpower = signum(y)*Math.sin(drive_direction-PI/4);
        rrpower = signum(y)*Math.cos(drive_direction-PI/4);
*/

        telemetry.addLine("Time Left: " + timeLeft);
        telemetry.addData("liftindex", gromit.glyphTrain.liftIndex);

    }

    @Override
    public void stop() {
        gromit.driveTrain.stopMotors();
        gromit.glyphTrain.stopGlyphMotors();
        gromit.glyphTrain.lift_motor.setPower(0.0);
        //               gromit.relicArm.stoprelic...
    }
    public void drivevector(double x, double y, double turn) {
        //speed change code
        //   double drive_direction = atan(y/x);
        //double speedMultiplier = 0.4;

        double lfpower;
        double lrpower;
        double rfpower;
        double rrpower;

        double rotation = turn*0.75;

        double drive_direction = atan(y/x);

        lfpower = Math.cos(drive_direction-PI/4);
        lrpower = Math.sin(drive_direction-PI/4);
        rfpower = Math.sin(drive_direction-PI/4);
        rrpower = Math.cos(drive_direction-PI/4);

//        lfbase = signum(distance)*Math.cos(Math.toRadians(drive_direction + 45));
//        lrbase = signum(distance)*Math.sin(Math.toRadians(drive_direction + 45));
//        rfbase = signum(distance)*Math.sin(Math.toRadians(drive_direction + 45));
//        rrbase = signum(distance)*Math.cos(Math.toRadians(drive_direction + 45));


        //Determine largest power being applied in either direction
        double max = abs(lfpower);
        if (abs(lrpower) > max) max = abs(lrpower);
        if (abs(rfpower) > max) max = abs(rfpower);
        if (abs(rrpower) > max) max = abs(rrpower);

//            double multiplier = speedMultiplier / max; //multiplier to adjust speeds of each wheel so you can have a max power of 1 on atleast 1 wheel
        double multiplier = 0;//speedMultiplier;

        lfpower *= multiplier;
        lrpower *= multiplier;
        rfpower *= multiplier;
        rrpower *= multiplier;

        gromit.driveTrain.left_front.setPower(lfpower);
        gromit.driveTrain.left_rear.setPower(lrpower);
        gromit.driveTrain.right_front.setPower(rfpower);
        gromit.driveTrain.right_rear.setPower(rrpower);
    }
}
