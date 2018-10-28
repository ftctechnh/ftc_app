package org.firstinspires.ftc.teamcode.Utilities.RoadRunner;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.drive.Drive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.RoadRunnerMecanumInterface;

@Autonomous
@Config
public class SampleFeedforwardTuningOpMode extends FeedforwardTuningOpMode {
    public static double DISTANCE = 30;
    public static double WHEEL_DIAMETER = 3.93;
    public SampleFeedforwardTuningOpMode() {
        // TODO: change the following to match your drive
        super(DISTANCE, RoadRunnerMecanumInterface.MOTOR_CONFIG.getMaxRPM(), WHEEL_DIAMETER);
    }

    @Override
    protected Drive initDrive() {
        return new RoadRunnerMecanumInterface(hardwareMap);
    }
}
