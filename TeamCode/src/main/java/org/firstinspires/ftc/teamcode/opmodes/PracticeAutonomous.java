package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 10/20/2017.
 */
@Autonomous(name="Practice Autonomous", group="Opmode")
public class PracticeAutonomous extends LinearOpMode
{
    Hardware9330 hwMap = new Hardware9330();

    @Override
    public void runOpMode() throws InterruptedException {
        hwMap.init(hardwareMap);

        // wait for the start button to be pressed.
        telemetry.addData(">", "Press start!");
        telemetry.update();
        waitForStart();

        /*
        Checks the state of the touch sensor - returns true by default, but will return false when pressed.
        We need to change it to stop when the button is pressed. HAVE FUN!!!
        */

        while (opModeIsActive()) {
            hwMap.rightMotor.setPower(50);
            while (hwMap.touch.getState() && !isStopRequested()) {
                telemetry.addData("Touch ", hwMap.touch.getState());
                telemetry.update();
            }
            hwMap.rightMotor.setPower(0);
            stop();
        }
    }
}
