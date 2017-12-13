package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by ftc6347 on 12/9/17.
 */
@Autonomous(name = "Autonomous", group = "autonomous")
public class RobotAutonomous extends LinearOpMode {

    private RelicRecoveryRobot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        this.robot = new RelicRecoveryRobot(this);

        robot.glyphLift.initializeGrippers();
        robot.intake.raiseIntake();

        waitForStart();

        robot.hDriveTrain.directionalDrive(90, 0.5, 24, false);


    }
}
