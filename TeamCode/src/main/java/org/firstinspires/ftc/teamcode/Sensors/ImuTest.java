package org.firstinspires.ftc.teamcode.Sensors;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Auto3imu;
import org.firstinspires.ftc.teamcode.Sensors.IMUcontrol;
import org.firstinspires.ftc.teamcode.SubAssembly.Sample.SampleTemplate;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "IMU Test", group = "Test")
public class ImuTest extends LinearOpMode {
    IMUcontrol imu = new IMUcontrol();
    Auto3imu auto = new Auto3imu();

    private double runTimes = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        imu.init(hardwareMap);
        telemetry.addLine("IMU Test OpMode");

        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {

            if (runTimes < 1) {
                imu.IMUinit();
                telemetry.addData("Start Position", imu.startAngle);
                telemetry.update();
                runTimes++;
            }
            else {
                imu.IMUupdate();
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
