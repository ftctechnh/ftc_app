package org.firstinspires.ftc.teamcode.Qualifier;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static org.firstinspires.ftc.teamcode.Qualifier.DriveTrain.SpeedSetting.FAST;
import static org.firstinspires.ftc.teamcode.Qualifier.DriveTrain.SpeedSetting.MID;
import static org.firstinspires.ftc.teamcode.Qualifier.DriveTrain.SpeedSetting.SLOW;
import static java.lang.Math.atan;


@TeleOp(name = "zMoo", group = "8045")  // @Autonomous(...) is the other common choice
//@Disabled
public class Teleop extends OpMode {

    RobotRR gromit;
    double turnDirection;

    private ElapsedTime runtime = new ElapsedTime();
    double timeLeft;
    double startclosetime = 0.0;
    double startclamptime = 0.0;

    //Booleans
    public boolean backIsReleased = true;
    public boolean back2IsReleased = true;
    public boolean rightbtnIsReleased = true;
    public boolean rightbtn2IsReleased = true;
    public boolean xIsReleased = true;
    //public boolean aIsReleased = true;
    public boolean start2IsReleased = true;
    public boolean righttriggerIsReleased = true;
    public boolean tristanmode = false;

    //SLOW SERVO
    public boolean elbowmoving = false;
    public double elbowtotalmove = 0;
    double elbowstarttime = 0.0;
    double elbowstartpos = 0.0;
    double movetime = 0;
    double elbowtarget = 0.0;


    public boolean leftbumperIsReleased = true;
    public boolean leftttriggerIsReleased = true;
    public boolean glyphLiftismoving = false;
    public boolean delayLift = false;
    public boolean delayClamp = false;
    public boolean glyphSensed = false;
    public int manualLiftDelay = 200;
    public int glyphSensedDelay = 0;


    public boolean padupIsReleased = true;
    public boolean paddownIsReleased = true;
    public boolean aIsReleased = true;

    public int liftTarget = 0;
    public boolean trainon = false;
    boolean relicclamped = false;

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

        double sharpIRVoltage = gromit.driveTrain.sharpIRSensor.getVoltage();
        double IRdistance = 18.7754 * Math.pow(sharpIRVoltage, -1.51);
        telemetry.addData("Sharp IR V ", sharpIRVoltage);
        telemetry.addData("Sharp IR ", "cm %4.1f ", IRdistance);
        RobotLog.ii("[Gromit] IR", Double.toString(IRdistance), " Ticks " + Integer.toString(gromit.driveTrain.left_front.getCurrentPosition()));

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
        if (gamepad1.x || gamepad2.x) {  // raise lift
            if (xIsReleased) {
                gromit.glyphTrain.glyphclamp("close");
                startclosetime = runtime.milliseconds();    // start timer  set boolean
                delayLift = true;
                xIsReleased = false;                                              // button toggle
                gromit.glyphTrain.liftIndex = Math.min(gromit.glyphTrain.liftIndex + 1, 2);   //add one to index, max is 2
//  check to see if you're lower than the next lower position by 400 ticks, stop there first.
                if (gromit.glyphTrain.lift_motor.getCurrentPosition() + 200 < gromit.glyphTrain.liftPosition[gromit.glyphTrain.liftIndex - 1]) {
                    gromit.glyphTrain.liftIndex -= 1;
                }
                liftTarget = gromit.glyphTrain.liftPosition[gromit.glyphTrain.liftIndex];  // set the new Target
                glyphLiftismoving = true;
//                gromit.glyphTrain.lift_motor.setPower(1.0);   // start the motor going up  wait for a few loops to start lift to let clamp close
            }
        } else {
            xIsReleased = true;
        }
// check to see if enough time has elapsed to start lift   (automatic lift after clamp)
        if (delayClamp && runtime.milliseconds() - startclosetime > glyphSensedDelay) {     // this is the delay time in milliseconds
            delayClamp = false;
            glyphSensedDelay = 0;
            startclamptime = runtime.milliseconds();            // start timer to make sure lift waits the right amount of time set boolean
            delayLift = true;
            gromit.glyphTrain.glyphclamp("close");      //Clamp servo
        }
        if (delayLift && runtime.milliseconds() - startclamptime > manualLiftDelay) {     // this is the delay time in milliseconds
            delayLift = false;
            glyphSensedDelay = 0;
            gromit.glyphTrain.lift_motor.setPower(1.0);   // start the motor going up
        }

        if (gamepad1.a || gamepad2.a) {         //lower
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
                if (gromit.glyphTrain.lift_motor.getCurrentPosition() <= liftTarget + 200) {  // slow down before overshooting zero
                    gromit.glyphTrain.lift_motor.setPower(-0.4);

                }
                if (gromit.glyphTrain.lift_motor.getCurrentPosition() <= liftTarget) {
                    gromit.glyphTrain.lift_motor.setPower(0.0);
                    glyphLiftismoving = false;
                }
            } else {
                if (gromit.glyphTrain.lift_motor.getCurrentPosition() >= liftTarget) {
                    gromit.glyphTrain.lift_motor.setPower(0.0);
                    glyphLiftismoving = false;
                }
            }
        }

        //------------------------------------------------------------------------------
        // manual glyph lift
        if (gamepad1.y || gamepad2.y) {
            glyphLiftismoving = false;
            if (gromit.glyphTrain.lift_motor.getCurrentPosition() < gromit.glyphTrain.upperLiftLimit) {
                gromit.glyphTrain.lift_motor.setPower(1.0);
            } else {
                gromit.glyphTrain.lift_motor.setPower(0.0);
            }
        } else if (gamepad1.b || gamepad2.b) {
            glyphLiftismoving = false;
            if (gromit.glyphTrain.lift_motor.getCurrentPosition() > gromit.glyphTrain.lowerLiftLimit) {
                gromit.glyphTrain.lift_motor.setPower(-0.8);
            } else {
                gromit.glyphTrain.lift_motor.setPower(0.0);
            }

        } else if (!glyphLiftismoving) {
            gromit.glyphTrain.lift_motor.setPower(0.0);
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
            if (paddownIsReleased) {
                paddownIsReleased = false;
                if (trainon) {
                    trainon = false;
                    gromit.glyphTrain.stopGlyphMotors();
                } else {
                    trainon = true;
                    gromit.glyphTrain.startGlyphMotors(1.0);
                }
            }
        } else {
            paddownIsReleased = true;
        }
        //BACKWARDS
        if (gamepad1.dpad_up || gamepad2.dpad_up) {
            if (padupIsReleased) {
                padupIsReleased = false;
                if (trainon) {
                    trainon = false;
                    gromit.glyphTrain.stopGlyphMotors();
                } else {
                    trainon = true;
                    gromit.glyphTrain.startGlyphMotors(-0.2);
                }
            }
        } else {
            padupIsReleased = true;
        }

        //  check for incoming block  here
        if (trainon) {
            if (gromit.glyphTrain.sensorDistance.getDistance(DistanceUnit.CM) < 12 && !glyphSensed) {     // if block is sensed set boolean
                glyphSensed = true;
            } else if (glyphSensed && gromit.glyphTrain.sensorDistance.getDistance(DistanceUnit.CM) > 12) {     // if block was already sensed (sense the back end)
                glyphSensed = false;
                startclosetime = runtime.milliseconds();    // start timer  set boolean
                //delayLift = true;                            // check for time to lift
                delayClamp = true;
                glyphSensedDelay = 300;
                // set target  as 1 (assume we're at zero for now
                liftTarget = gromit.glyphTrain.liftPosition[1];  // set the new Target
                glyphLiftismoving = true;     // turn on manual override

            }
        }


        // glyph clamp
        if (gamepad1.right_trigger > 0.1 || gamepad2.right_trigger > 0.1) {
            gromit.glyphTrain.glyphclamp("open");
        } else if (gamepad1.right_bumper || gamepad2.right_bumper) {
            gromit.glyphTrain.glyphclamp("close");
        }


        if (gamepad1.back) {
            if (backIsReleased) {
                backIsReleased = false;
                if (tristanmode) {
                    tristanmode = false;
                } else {
                    tristanmode = true;
                }
            }
        } else {
            backIsReleased = true;
        }

        if (tristanmode) {
            gromit.driveTrain.drivevector(gamepad1.right_stick_x, -gamepad1.right_stick_y, turnDirection * gamepad1.left_stick_x);
        } else {
            gromit.driveTrain.drivesmart(-gamepad1.right_stick_x, -gamepad1.right_stick_y, turnDirection * gamepad1.left_stick_x);
        }
        /**
         * RELIC CONTROLS
         */
        //SLOW RELIC ELBOW
        //Zero position
        if (gamepad2.start) {
            if (start2IsReleased) {//IF CHANGE IN STATE
                start2IsReleased = false;
                movetime = 500;
                elbowmoving = true;
                elbowstartpos = gromit.relicArm.relicElbowServo.getPosition();
                elbowstarttime = runtime.milliseconds();//Start time
                elbowtarget = gromit.relicArm.elbowdown;
                elbowtotalmove = elbowtarget - elbowstartpos;
            }
        } else {
            start2IsReleased = true;
        }
        //Middle Position
        if (gamepad2.left_trigger > 0.1) {
            if (leftttriggerIsReleased) {//IF CHANGE IN STATE
                leftttriggerIsReleased = false;
                movetime = 500;
                elbowmoving = true;
                elbowstartpos = gromit.relicArm.relicElbowServo.getPosition();
                elbowstarttime = runtime.milliseconds();//Start time
                elbowtarget = gromit.relicArm.elbowup;
                elbowtotalmove = elbowtarget - elbowstartpos;
            }
        } else {
            leftttriggerIsReleased = true;
        }
        //TOP POSITION
        if (gamepad2.left_bumper) {
            if (leftbumperIsReleased) {//IF CHANGE IN STATE
                leftbumperIsReleased = false;
                movetime = 700;
                elbowmoving = true;
                elbowstartpos = gromit.relicArm.relicElbowServo.getPosition();
                elbowstarttime = runtime.milliseconds();//Start time
                elbowtarget = gromit.relicArm.elbowtop;
                elbowtotalmove = elbowtarget - elbowstartpos;
            }
        } else {
            leftbumperIsReleased = true;
        }
        if (elbowmoving) {
            gromit.relicArm.relicElbowServo.setPosition(((runtime.milliseconds() - elbowstarttime) / movetime) * elbowtotalmove + elbowstartpos);
            if (runtime.milliseconds() > elbowstarttime + movetime) {
                elbowmoving = false;
            }
        }
        //Clamp Full speed
        if (gamepad2.right_stick_button) {
            if (rightbtn2IsReleased) {
                rightbtn2IsReleased = false;
                if (relicclamped) {
                    relicclamped = false;
                    gromit.relicArm.clawOpen();
                } else {
                    relicclamped = true;
                    gromit.relicArm.clawClose();
                }
            }
        } else {
            rightbtn2IsReleased = true;
        }

        //Clamp slowly
        if(gamepad2.right_stick_y > 0.1){
            gromit.relicArm.relicClawServo.setPosition(gromit.relicArm.relicClawServo.getPosition()+.005);
        }
        else if(gamepad2.right_stick_y < -0.1){
            gromit.relicArm.relicClawServo.setPosition(gromit.relicArm.relicClawServo.getPosition()-.005);
        }

        //Elbow Slowly
        if (gamepad2.left_stick_y < -0.1) {
            gromit.relicArm.relicElbowServo.setPosition(gromit.relicArm.relicElbowServo.getPosition()+.004);
        } else if (gamepad2.left_stick_y > 0.1) {
            gromit.relicArm.relicElbowServo.setPosition(gromit.relicArm.relicElbowServo.getPosition()-.004);
        }


        // RELIC ARM IN/OUT
        if (gamepad1.dpad_right && gromit.relicArm.relicArmMotor.getCurrentPosition() < gromit.relicArm.relicArmMotorMax) {
            gromit.relicArm.relicArmMotor.setPower(0.8);
        }
       else if(gamepad1.dpad_left && gromit.relicArm.relicArmMotor.getCurrentPosition() > gromit.relicArm.relicArmMotorMin){
            gromit.relicArm.relicArmMotor.setPower(-0.7);
            }
        else {
            gromit.relicArm.relicArmMotor.setPower(0.0);
        }


        //Telemetry
        //telemetry.addLine("Time Left: " + timeLeft);
        //telemetry.addData("liftindex", gromit.glyphTrain.liftIndex);
        //telemetry.addData("liftPosition", gromit.glyphTrain.lift_motor.getCurrentPosition());
        telemetry.addData("glyph sensor", gromit.glyphTrain.sensorDistance.getDistance(DistanceUnit.CM));
        telemetry.addData("relicArmTicks",gromit.relicArm.relicArmMotor.getCurrentPosition());


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

        double rotation = turn * 0.75;

        double drive_direction = atan(y / x);

        lfpower = Math.cos(drive_direction - PI / 4);
        lrpower = Math.sin(drive_direction - PI / 4);
        rfpower = Math.sin(drive_direction - PI / 4);
        rrpower = Math.cos(drive_direction - PI / 4);

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
