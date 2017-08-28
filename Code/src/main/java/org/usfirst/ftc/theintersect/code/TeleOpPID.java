/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Timer;
import java.util.TimerTask;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Teleop Flywheel PID")
public class TeleOpPID extends LinearOpMode {
    private DcMotor rFmotor, rBmotor, lFmotor, lBmotor;
    DcMotor flywheel1, flywheel2;
    DcMotor sweeperLow;
    DcMotor liftMotor;
    Servo lLift, rLift;
    Servo sideWall;
    CRServo buttonPusher;

    private int prevPos1, prevPos2;
    private double prevTime;
    private double  voltage;
    private double variableSpeed;
    private double speed1, speed2; // current speed of flywheels
    private double loopTime; // loop time in sec


    private final double drivePidKp = 1; // Tuning variable for PID.
    private final double drivePidTi = 1.0; // Eliminate integral error in 1 sec.
    private final double drivePidTd = 0.1; // Account for error in 0.1 sec.
    // Protect against integral windup by limiting integral term.
    private final double drivePidIntMax = 1.0; // Limit to max speed.

    private final double sweeperPickupPower = 1.0; //  power for sweeper when running
    private final double sweeperReversePower = -0.5; // power for sweeper when running at reverse for unjam
    private final double ticksPerRevolutionFlywheel = 103; // Todo: Find our real value from datasheet


    private double flywheelTargetSpeed; // Target flyWheel speed in RPM. Todo: need to set to right speed
    private double flywheelCurrentSpeed;
    private boolean flywheelReady;
    private boolean sweeperOnPending; // if sweeper waiting for flywheel to be ready
    private Timer flywheelDelayTimer = null;

    private PID flywheelDrive1 = null;
    private PID flywheelDrive2 = null;

    private enum ShootState {
        IDLE,       // stop sweeper and flywheel
        STANDBY,    // reverse sweeper for little bit, run flywheel to speed
        PICKUP,     // pick up ball, reverse flywheel @ low speed and turn on sweeper
        SHOOT,      // then turn on sweeper to shoot after flywheel reach target speed
        UNLOAD      // unload ball - reverse sweeper, leave flywheel at current speed
    }
    private ShootState robotIdelState;      // IDLE/STANDBY : state of robot when no button pushed
    private ShootState robotMissionCurrent; // currnet Robot state
    private ShootState robotMissionNext;    // next Robot state

    private enum SweeperMode { // what is the mode sweeper should ruun
        STOP, // turn off sweeper
        REVERSE, // reverse sweeper
        REVERSE_AND_RUN_FLYWHEEL, // reverse sweeper for 150msec and then run flywheel
        FORWARD, // run forward
        FORWARD_WHEN_FLYWHEEL_READY //run forward when flywheel reach target speed
    }

    // when turn on flywheel, run sweeper reverse for X msec before turning on flywheel to avoid misfire
    private final int msecToFlyWheelOnDelay = 500;

    // RampFlywheel rampFlywheel = new RampFlywheel();
    // RampDownFlywheel rampDownFlywheel = new RampDownFlywheel();
    @Override
    public void runOpMode() throws InterruptedException {
        double fwdPower, strafePower, rotationPower;

        boolean toggleWall;
        boolean wallUp = true;
        boolean wallControl = false;

        boolean slideUp, slideDown, liftUp, liftDown, gateClose, extendPusher, retractPusher;
        boolean pushBeacon;


        initDevices();
        robotMissionCurrent = ShootState.IDLE;
        robotMissionNext = ShootState.IDLE;
        robotIdelState = ShootState.IDLE;
        flywheelCurrentSpeed = 0;


        waitForStart();
        prevTime = 0;

        flywheelDrive1 = new PID(drivePidKp, drivePidTi, drivePidTd,
                    -drivePidIntMax, drivePidIntMax);

        flywheelDrive2 = new PID(drivePidKp, drivePidTi, drivePidTd,
                    -drivePidIntMax, drivePidIntMax);



        while (opModeIsActive()) {

            // drive control
            fwdPower = gamepad1.left_stick_y;
            strafePower = gamepad1.left_stick_x;
            rotationPower = gamepad1.right_stick_x;

            if (gamepad1.start) {
                lFmotor.setPower((fwdPower + strafePower + rotationPower) / 2);
                lBmotor.setPower((fwdPower - strafePower + rotationPower) / 2);
                rFmotor.setPower((fwdPower - strafePower - rotationPower) / 2);
                rBmotor.setPower((fwdPower + strafePower - rotationPower) / 2);
            } else {
                lFmotor.setPower(fwdPower + strafePower + rotationPower);
                lBmotor.setPower(fwdPower - strafePower + rotationPower);
                rFmotor.setPower(fwdPower - strafePower - rotationPower);
                rBmotor.setPower(fwdPower + strafePower - rotationPower);
            }

            // lift control
            liftDown = gamepad1.dpad_down || gamepad2.dpad_down;
            liftUp = gamepad1.dpad_up || gamepad2.dpad_up;
            if (liftDown) {
                rLift.setPosition(Functions.liftDownPos);
                lLift.setPosition(Functions.liftDownPos);
            }
            if (liftUp) {
                rLift.setPosition(Functions.liftUpPos);
                lLift.setPosition(Functions.liftUpPos);
            }


            // wall guard control
            toggleWall = gamepad2.start || gamepad1.back;
            if (toggleWall && !wallUp && !wallControl) {
                sideWall.setPosition(Functions.sideWallUpPos);
                wallUp = true;
                wallControl = true;
            }
            if (toggleWall && wallUp && !wallControl) {
                sideWall.setPosition(Functions.sideWallDownPos);
                wallUp = false;
                wallControl = true;
            }
            if (!toggleWall) {
                wallControl = false;
            }


/*****************************
            // pusher control Todo: Assign new button
            extendPusher = gamepad1.y || gamepad2.y;
            retractPusher = gamepad1.x || gamepad2.x;
            if (extendPusher) {
                buttonPusher.setPower(1);
            } else if (retractPusher) {
                buttonPusher.setPower(-1);
            } else {
                buttonPusher.setPower(0);
            }
*******************/


            // Shooting mission control
            // Press A -> Pickup
            // Press B -> unload
            // Press X -> Shoot
            // Nothing pressed -> IDLE or STANDBY mode
            //   Press right bumper - set default to STANDBY
            //   Press left bumper  - set default to IDLE
            //
            telemetry.clear();

            if (gamepad1.left_bumper||gamepad2.left_bumper) {
                robotIdelState = ShootState.IDLE;
            } else if ( gamepad1.right_bumper || gamepad2.right_bumper ) {
                robotIdelState = ShootState.STANDBY;
            }

            if ( gamepad1.a || gamepad2.a) {
                robotMissionNext = ShootState.PICKUP;
            } else if ( gamepad1.x || gamepad2.x ) {
                robotMissionNext = ShootState.SHOOT;
            } else if ( gamepad1.b || gamepad2.b ) {
                robotMissionNext = ShootState.UNLOAD;
            } else {
                robotMissionNext = robotIdelState;
            }

            loopTime = calculateFlywheelSpeed();  // Calculate speed. Looptime is sec

            if ( robotMissionNext == robotMissionCurrent ) {
                telemetry.addData("SpeedL:", speed1); // in RPM
                telemetry.addData("SpeedR:", speed2); // in RPM
                telemetry.addData("prevPos1:", prevPos1);
                telemetry.addData("prevPos2:", prevPos2);
                telemetry.addData("Loop:", loopTime * 1000); // convert to msec
                telemetry.addData("time:", System.nanoTime());
                telemetry.update();
                continue;
            } // nothing changed for robot mission

            switch (robotMissionNext) {
                case IDLE:
                    SetSweeper(SweeperMode.STOP); // stop sweeper
                    SetFlywheel(0);
                    telemetry.addLine("STATE:IDLE");
                    break;
                case STANDBY:
                    SetSweeper(SweeperMode.STOP); // stop sweeper
                    SetFlywheel(1);
                    telemetry.addLine("STATE:STANDBY");
                    break;
                case UNLOAD:
                    SetSweeper(SweeperMode.REVERSE);
                    telemetry.addLine("STATE:UNLOAD");
                    break;
                case PICKUP:
                    SetFlywheel(-0.2);
                    SetSweeper(SweeperMode.FORWARD);
                    telemetry.addLine("STATE:PICKUP");
                    break;
                case SHOOT:
                    SetFlywheel(1);
                    SetSweeper(SweeperMode.FORWARD_WHEN_FLYWHEEL_READY);
                    telemetry.addLine("STATE:SHOOT");
                    break;
            }
            robotMissionCurrent = robotMissionNext;

            telemetry.addData("SpeedL:", speed1); // in RPM
            telemetry.addData("SpeedR:", speed2); // in RPM
            telemetry.addData("prevPos1:", prevPos1);
            telemetry.addData("prevPos2:", prevPos2);
            telemetry.addData("Loop:", loopTime * 1000); // convert to msec


            flyWheelControl();
            telemetry.addData("PowerL:", flywheel1.getPower());
            telemetry.addData("PowerR:", flywheel2.getPower());
            telemetry.update();


            if ( sweeperOnPending && flywheelReady ) {
                SetSweeper(SweeperMode.FORWARD);
            }

            telemetry.update();


            // Lift control
            if (gamepad1.left_trigger > 0.1)
                liftMotor.setPower(-gamepad1.left_trigger);
            else if (gamepad2.left_trigger > 0.1)
                liftMotor.setPower(-gamepad2.left_trigger);
            else if (gamepad1.right_trigger > 0.1)
                liftMotor.setPower(gamepad1.right_trigger);
            else if (gamepad2.right_trigger > 0.1)
                liftMotor.setPower(gamepad2.right_trigger);
            else {
                liftMotor.setPower(0);
            }



        }
    }

    private void initDevices() {
        rFmotor = hardwareMap.dcMotor.get("rF");
        rBmotor = hardwareMap.dcMotor.get("rB");
        lFmotor = hardwareMap.dcMotor.get("lF");
        lBmotor = hardwareMap.dcMotor.get("lB");

        flywheel1 = hardwareMap.dcMotor.get("flywheel1");
        flywheel2 = hardwareMap.dcMotor.get("flywheel2");

        sweeperLow = hardwareMap.dcMotor.get("sweeperLow");

        liftMotor = hardwareMap.dcMotor.get("liftMotor");

        sideWall = hardwareMap.servo.get("sideWall");

        lLift = hardwareMap.servo.get("lLift");
        rLift = hardwareMap.servo.get("rLift");

        buttonPusher = hardwareMap.crservo.get("buttonPusher");

        rLift.setDirection(Servo.Direction.REVERSE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lBmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rBmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lFmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rFmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lBmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rBmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        lBmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        lFmotor.setDirection(DcMotorSimple.Direction.FORWARD);


        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        flywheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // turn off internal PID
        flywheel1.setDirection(DcMotorSimple.Direction.REVERSE);

        flywheel2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // turn off internal PID

        sweeperLow.setDirection(DcMotorSimple.Direction.REVERSE);
        sideWall.setPosition(Functions.sideWallUpPos);

    }


    // calculate speed and set ready flag
    private double calculateFlywheelSpeed(){

        double deltaTime; // Elapse time between measurement
        int pos1 = flywheel1.getCurrentPosition();
        int pos2 = flywheel2.getCurrentPosition();

        long currentTime = System.nanoTime(); // get current time in nanosecond

        if (prevTime == 0) { // init value
            prevPos1 = pos1;
            prevPos2 = pos2;
            prevTime = currentTime;
            speed1 = 0;
            speed2 = 0;
            return 0.0;
        }

        deltaTime = (currentTime - prevTime) / 1000000000; // looptime in msecond
        speed1 = (pos1 - prevPos1) * 1000000000 * 60 / ticksPerRevolutionFlywheel / (currentTime-prevTime);
        speed2 = (pos2 - prevPos2) / deltaTime * 60 / ticksPerRevolutionFlywheel;

        // store current measurement
        prevPos1 = pos1;
        prevPos2 = pos2;
        prevTime = currentTime;

        // determine if the flywheel is ready
        flywheelReady =
                (Math.abs(1 - (speed1 / flywheelTargetSpeed)) < 0.05) &&
                (Math.abs(1 - (speed2 / flywheelTargetSpeed)) < 0.05) ;

        return deltaTime;
    }

    private void flyWheelControl(){
        if ( flywheelTargetSpeed == 0) {
            return;
        }
// Standard
        flywheel1.setPower(CalcFlywheelPower(flywheel1.getPower(), speed1, flywheelTargetSpeed));
        flywheel2.setPower(CalcFlywheelPower(flywheel2.getPower(), speed2, flywheelTargetSpeed));
// Use PID
        flywheelPIDControl(loopTime);

    }

    private double CalcFlywheelPower( double p, double speed, double targetSpeed ){

        if ( speed > targetSpeed ) {
            p += 0.1;
            if ( p > 1.0) {
                p = 1.0;
            }
        } else if ( speed1 < targetSpeed ) {
            p -= 0.1;
            if ( p < 0.0 ) {
                p = 0.0;
            }
        }
        return p;
    }

    private void flywheelPIDControl(double deltaTime) {


        double flywheelPower1 = flywheelDrive1.update(flywheelTargetSpeed, speed1, deltaTime);
        double flywheelPower2 = flywheelDrive2.update(flywheelTargetSpeed, speed2, deltaTime);

        flywheel1.setPower(Math.min(flywheelPower1, 1.0));
        flywheel2.setPower(Math.min(flywheelPower2, 1.0));

    }


    private void SetFlywheel(double power){

        if ( power <= 0 ) {
            flywheel1.setPower(power);
            flywheel2.setPower(power);
            flywheelTargetSpeed = 0;
            flywheelReady = false;
            return;
        }


        flywheel1.setPower(1.0);
        flywheel2.setPower(1.0);
        flywheelTargetSpeed = 1000; //

    }

    private void SetSweeper(SweeperMode mode) {
        if (mode!= SweeperMode.REVERSE_AND_RUN_FLYWHEEL) {
//            CancelSweeperTimer();
            if( flywheelDelayTimer != null ) {
                flywheelDelayTimer.cancel();
            }
        }
        sweeperOnPending = false;
        switch (mode) {
            case STOP:
                sweeperLow.setPower(0);
                break;
            case FORWARD:
                sweeperLow.setPower(1.0);
                break;
            case REVERSE:
                sweeperLow.setPower(-1.0);
                break;
            case FORWARD_WHEN_FLYWHEEL_READY:
                if ( flywheelReady ) {
                    sweeperLow.setPower(1);
                } else {
                    sweeperOnPending = true;
                }
                break;
            case REVERSE_AND_RUN_FLYWHEEL:
                flywheelDelayTimer = new Timer();
                flywheelDelayTimer.schedule( new TurnOnFlywheelTask(), msecToFlyWheelOnDelay);
                sweeperLow.setPower(-1);


//                if ( StartReverseTime == 0) {
//                    sweeperLow.setPower(-1);
//                    StartReverseTime = timer.time();
//                }

        }
    }

    private class TurnOnFlywheelTask extends TimerTask {
        public void run() {
            flywheelDelayTimer.cancel();
            flywheelDelayTimer = null;
            sweeperLow.setPower(0); // turn off sweeper
            SetFlywheel(1.0); // turn on fly wheel

        }

    }




 /*
    void CancelSweeperTimer() {
        StartReverseTimer = 0;
    }
    void CheckSweeperTimer() {
        if (StartReverseTimer > 0 ) {
            if ((timer.time() - StartReverseTimer) > sweeperDelay ) {
                sweeperLow.setPower(0);
                StartFlywheel();

            }
        }
    }
 */

}


