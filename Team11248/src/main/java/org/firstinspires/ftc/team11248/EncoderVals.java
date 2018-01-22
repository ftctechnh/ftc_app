package org.firstinspires.ftc.team11248;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Tony_Air on 12/3/17.
 */
@TeleOp(name = "EncoderVal")
public class EncoderVals extends OpMode {

    Robot11248 robot;

    @Override
    public void init() {

        robot = new Robot11248(hardwareMap,telemetry);
        robot.init();
        robot.resetDriveEncoders();
        robot.backLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.backLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        robot.setDriftMode(true);
        robot.setDriveMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        robot.printDriveRotations();
        telemetry.addData("HOLONOMIC", "FrontLift: " + robot.frontLift.getCurrentPosition());
        telemetry.addData("HOLONOMIC", "BackLift: " + robot.backLift.getCurrentPosition());

    }
}
