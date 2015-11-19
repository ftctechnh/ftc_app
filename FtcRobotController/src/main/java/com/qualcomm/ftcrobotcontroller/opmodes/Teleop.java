package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Eric on 10/19/2015.
 * Created for 2015-2016 FTC Season
 */
public class Teleop extends FTCCompetitionBase {

    // TODO: Get some commands to control robot. Variable gamepad1 and gamepad2 are your friends.
    @Override
    public void loop() {
        this.ArcadeDrive(gamepad1.left_stick_y, -gamepad1.right_stick_x);
        this.BlockPickup(gamepad1.left_bumper);
        this.PullupHook(gamepad1.y, gamepad1.a);
        this.setArmTilt(gamepad2.left_stick_y);
        this.setArmLift(gamepad2.right_stick_y);
        this.DumpTrash(gamepad2.x, gamepad2.b);
        this.BlockPickup(gamepad1.right_bumper);
        this.TabDropper(gamepad2.left_bumper, gamepad2.right_bumper);

        // Macros
        this.pullUpMountain(gamepad1.b);
    }
}
