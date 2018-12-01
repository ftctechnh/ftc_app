package org.firstinspires.ftc.teamcode.SubAssembly.Sensors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "IMU Test", group = "Test")
public class ImuTest extends LinearOpMode {
    private double runTimes = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("IMU Test OpMode");

        telemetry.update();

        /* initialize sub-assemblies
         */
        IMUcontrol imu = new IMUcontrol();

        imu.init(this);
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {

            if (runTimes < 1) {
                imu.setStartAngle();
                telemetry.addData("Start Position", imu.startAngle);
                telemetry.update();
                runTimes++;
            } else {
                imu.update();
                telemetry.addData("startAngle", imu.startAngle);
                telemetry.addData("currentAngle", imu.currentAngle);
                telemetry.addData("trueAngle", imu.trueAngle);
                telemetry.update();
            }
            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }
}
