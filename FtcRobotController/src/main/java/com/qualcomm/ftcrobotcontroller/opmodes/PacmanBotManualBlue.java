package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by tdoylend on 2015-12-17.
 *
 * Change log:
 * 1.0.0 - First version.
 */

public class PacmanBotManualBlue extends PacmanBotHWB2 {

    VersionNumber version = new VersionNumber(1,0,0);
    final boolean side = true; //False is red, True is blue

    boolean mtnMode = false;
    boolean mtnBtn  = false;
    boolean hookReleaseFlag = false;

    boolean fingerFlag = false;
    boolean fingerBtn  = false;

    RunToPositionController tailController;

    @Override
    public void init() {
        telemetry.addData("OpMode","PacmanBotManual");
        telemetry.addData("Version",version.string());
        telemetry.addData("HWB Version", hwbVersion.string());

        setupHardware();
        setFinger(false, side);
        tailController=new RunToPositionController(tail,false);
    }

    @Override
    public void loop() {
        tailController.check();
        update();

        setCollector(threeWay(gamepad1.b, gamepad1.a));

        if (!mtnMode) drive(-gamepad1.left_stick_y,gamepad1.right_stick_x);
        else driveMtn(-gamepad1.left_stick_y,gamepad1.right_stick_x);

        telemetry.addData("Basket Position", basket.getCurrentPosition());
        telemetry.addData("Mountain Mode", mtnMode ? "ON" : "OFF");


        setBasket(threeWay(gamepad1.dpad_left, gamepad1.dpad_right));

        setDoor(gamepad1.a || gamepad1.y);

        //setFinger(mtnMode, side);

        setDumper(gamepad1.dpad_up);

        setWinch(threeWay(gamepad1.right_bumper, gamepad1.right_trigger > 0.5));

        //encoder based tail
        if(gamepad1.left_trigger>0.5) {
            tailController.goTo(0,0.2);//in
        }
        else if(gamepad1.left_bumper) {
            tailController.goTo(180,0.2);//out
        }
        //setTail(threeWay(gamepad1.left_trigger > .5, gamepad1.left_bumper));

        if (mtnMode) {
            if (gamepad1.x) hookReleaseFlag=true;
            setRelease(hookReleaseFlag);
        } else {
            setRelease(false);
        }

        if (!mtnBtn) {
            if (gamepad1.left_stick_button) {
                mtnMode = !mtnMode;
                mtnBtn=true;
            }
        } else if (!gamepad1.left_stick_button) mtnBtn=false;

        setFinger(mtnMode && fingerFlag,side);

        if (!fingerBtn) {
            if (gamepad1.dpad_down) {
                fingerBtn = true;
                fingerFlag = !fingerFlag;
            }
        } else {
            if (!gamepad1.dpad_down) fingerBtn=false;
        }

    }
}
