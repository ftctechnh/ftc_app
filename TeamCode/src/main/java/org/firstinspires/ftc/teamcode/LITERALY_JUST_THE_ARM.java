package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by jxfio on 9/30/2017.
 */
@TeleOp(name="Literaly just the arm", group="Linear Opmode")
public class LITERALY_JUST_THE_ARM extends LinearOpMode {
    public DcMotor arm1 = null;
    @Override

    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        arm1 = hardwareMap.get(DcMotor.class, "arm");
        waitForStart();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            arm1.setPower(Range.clip(gamepad2.left_stick_y,-1,1));
        }
    }
}
