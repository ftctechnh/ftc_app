package org.firstinspires.ftc.teamcode.commands.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;

/**
 * Created by nycfirst on 2/2/18.
 */

@Autonomous(name = "parking left", group = "parking")
public class ParkingLeft extends LinearOpMode {
    private MecanumDriveSystem mecanumDriveSystem;
    private ElapsedTime runtime = new ElapsedTime();

    private void initialize() {
        this.mecanumDriveSystem = new MecanumDriveSystem(hardwareMap);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        int count = 0;
        while (opModeIsActive() && (count < 1)) {
            mecanumDriveSystem.drive(0.5, 0.5);
            sleep(2000);
            count++;
        }
        mecanumDriveSystem.stop();
    }
}
