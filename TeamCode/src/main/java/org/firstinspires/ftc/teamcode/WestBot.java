
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;


@TeleOp

public class WestBot extends LinearOpMode {

    private DcMotor m1, m2;
    private double rightPower = 0;
    private double leftPower = 0;

    private double sens  = 1;


    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        m1 = hardwareMap.get(DcMotor.class, "Motor1");
        m2 = hardwareMap.get(DcMotor.class, "Motor2");



        waitForStart();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();

            rightPower = Range.clip(gamepad1.left_stick_y - gamepad1.left_stick_x,-sens,sens);
            leftPower = Range.clip(gamepad1.left_stick_y + gamepad1.left_stick_x,-sens,sens);


            m1.setPower(-rightPower);
            m2.setPower(leftPower);

            if (gamepad1.right_bumper && (sens/2 >= 0.125)){
                sens /= 2;
            }
            if (gamepad1.left_bumper && ((sens*2) <= 1)){
                sens *= 2;
            }


        }
    }
}
