/**
 * Created by spmce on 2/9/2016.
 */
package org.firstinspires.ftc.teamcode.ResQ;

public class RobotTeleOp extends RobotTelemetry {

    public RobotTeleOp() {
        setDriveTrain(0.0);
    }

    @Override
    public void loop() {
        super.loop();
        leftDrive     = gamepad1.left_trigger;
        rightDrive    = gamepad1.right_trigger;
        backLeftDrive =-gamepad1.left_stick_y;
        backRightDrive=-gamepad1.right_stick_y;
        if (gamepad1.left_bumper)
            leftDrive = -leftDrive;
        if (gamepad1.right_bumper)
            rightDrive = -rightDrive;

        setDriveTrain(leftDrive,rightDrive,backLeftDrive,backRightDrive);
    }
}
