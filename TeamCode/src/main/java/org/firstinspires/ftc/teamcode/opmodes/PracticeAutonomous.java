package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware9330;
import org.firstinspires.ftc.teamcode.subsystems.Drive9330;
import org.firstinspires.ftc.teamcode.subsystems.Ultrasonic9330;

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
        Drive9330 BillyMays = new Drive9330(hwMap);
        Ultrasonic9330 ultrasonic = new Ultrasonic9330(hwMap);

        // wait for the start button to be pressed.
        telemetry.addData(">", "Press start!");
        telemetry.update();
        waitForStart();

        /*
        Checks the state of the touch sensor - returns true by default, but will return false when pressed.
        We need to change it to stop when the button is pressed. HAVE FUN!!!
        */

        while (opModeIsActive()) {
            //hwMap.rightMotor.setPower(50);
            while (hwMap.touch.getState() && !isStopRequested()) {
                //telemetry.addData("Ultrasonic ", ultrasonic.getDistance());
               // telemetry.update();
                BillyMays.gyroTurn(90, 69);
                telemetry.addData("Program","We is turnin ninety degrees yo!!!!!!");
                telemetry.update();
                BillyMays.driveForward(69);
                sleep(1000);
                telemetry.addData("Program","We is drivin upward yo B))))))))) Help me conquer obesity!!!!!!");
                telemetry.update();
            }
            //hwMap.rightMotor.setPower(0);
            stop();
        }
    }
}
