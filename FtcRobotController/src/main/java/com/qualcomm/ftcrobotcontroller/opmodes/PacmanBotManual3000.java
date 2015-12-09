package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by tdoylend on 2015-11-28.
 *
 * Change log:
 * 1.1.0 - Added tire toggle.
 * 1.0.1 - Fixed toggle bug.
 * 1.0.0 - First version.
 */
public class PacmanBotManual3000 extends PacmanBotHardwareBase {

    VersionNumber version = new VersionNumber(1,1,0);
    ElapsedTime timer = new ElapsedTime();

    boolean highSpeed = false;
    boolean hsBtn = false;

    boolean flipperToggle = false;
    boolean fBtn = false;

    @Override
    public void init() {
        telemetry.addData("Program", "Manual Drive 3000");
        telemetry.addData("Version", version.string());
        telemetry.addData("HWB Version", hwbVersion.string());

        setupHardware();
        timer.reset();
    }

    @Override
    public void loop() {
        checkUsers();

        if (!hsBtn) {
            if (gamepad.left_stick_button){
                hsBtn=true;
                highSpeed=!highSpeed;
            } else {
                hsBtn = false;
            }
        } else {
            if (!gamepad.left_stick_button) hsBtn=false;
        }

        if (!fBtn) {
            if (gamepad.y) {
                fBtn=true;
                flipperToggle=!flipperToggle;
            }
        } else if (!gamepad.y) fBtn=false;
        
        setFlipper(flipperToggle);
        setFinalRateMultiplier(highSpeed ? 1.0 : 0.25);

        double drive_rate = -gamepad.left_stick_y;
        double turn_rate  = gamepad.right_stick_x;
        if (highSpeed) limit(turn_rate,-drive_rate,drive_rate);

        drive(drive_rate, turn_rate);

        setTire(threeWay(gamepad.right_bumper, gamepad.right_trigger > .5) / 3);

        setWinch(threeWay(gamepad.left_bumper, gamepad.left_trigger>.5));

        setBrush(threeWay(gamepad.a, gamepad.b));


        setBelt(threeWay(gamepad.dpad_left, gamepad.dpad_right)*.35);

        setThrower(gamepad.dpad_up);
        setArm(gamepad.dpad_up ? .5 : 0);

        //setHookRelease(gamepad.x);

        //setArm(Math.sin(timer.time()*4)); //Uncomment to annoy people.


    }
}
