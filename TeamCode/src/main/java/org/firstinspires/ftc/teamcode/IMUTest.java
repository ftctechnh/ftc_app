package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SubAssembly.Claimer.ClaimerControl;
import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Leds.LedControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Lift.LiftControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Vucam.VucamControl;
import org.firstinspires.ftc.teamcode.Utilities.AutoTransitioner;
import org.firstinspires.ftc.teamcode.Utilities.UserControl;

@Autonomous(name = "IMUTest", group = "Auto")
public class IMUTest extends LinearOpMode {

    /* Sub assemblies */
    DriveControl Drive = new DriveControl();

    //time based variables
    private ElapsedTime runtime = new ElapsedTime();
    private double lastReset = 0;
    private double now = 0;
    private int interval = 0;

    /* Constants */
    final double TURN_SPEED = 0.3;


    public void resetClock() {
        //resets the clock
        lastReset = runtime.seconds();
    }


    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.setAutoClear(false);
        telemetry.addLine("Autonomous");
        telemetry.update();

        /* initialize sub-assemblies
         */
        Drive.init(this);

        //waits for that giant PLAY button to be pressed on RC
        telemetry.addLine(">> Press PLAY to start");
        telemetry.update();
        telemetry.setAutoClear(true);
        waitForStart();

        telemetry.update();
        telemetry.setAutoClear(false);

        Drive.imu.setStartAngle();

        while (opModeIsActive()) {

            now = runtime.seconds() - lastReset;

            Drive.imu.update();

            Drive.turnRight(TURN_SPEED, 5);
            Drive.TimeDelay(5);

            sleep(40);
        }

        //telemetry.setAutoClear(true);
    }
}