package org.firstinspires.ftc.teamcode.Demos;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.NullbotHardware;

/**
 * Created by guberti on 10/27/2017.
 */
@Disabled
public class DriveOffTable extends LinearOpMode {
    NullbotHardware robot = new NullbotHardware();
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("Please don't run this code");
        telemetry.addLine("You don't want to be the next Aman");
        telemetry.update();
        robot.init(hardwareMap, this, gamepad1, gamepad2);

        //IN HONOR OF HENRY MENG'S VALIANT HUMILIATION AND USAGE OF WHILE(TRUE)
        while(true) {

            // IN HONOR OF AMAN'S INFAMOUS AUTONOMOUS
            robot.sleep(15000);

            // IN HONOR OF AMAN'S FEARLESS DRIVING OF THE ROBOT OFF THE TABLE
            for (DcMotor m : robot.motorArr) {
                m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                m.setPower(1);
            }

            // IN HONOR OF YEAR ONE'S SLEEP FUNCTION, DURING WHICH YOU COULD NOT HALT
            // THE PROGRAM
            Thread.sleep(10000);

            // IN HONOR OF AMOUNT OF TALENT ON JV
            int t = 0;

        }
    }
}
