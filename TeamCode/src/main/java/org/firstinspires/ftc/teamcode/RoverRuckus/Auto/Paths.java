package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.drive.Drive;
import com.acmerobotics.roadrunner.path.heading.ConstantInterpolator;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.DriveConstants;

@Config
public class Paths {

    static Trajectory STRAFE_RIGHT = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(0, 15), new ConstantInterpolator(0))
            .build();

    static Trajectory STRAFE_LEFT = new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(0, -15), new ConstantInterpolator(0))
            .build();

    static Trajectory DEPOT_TO_SAME_CRATER = new TrajectoryBuilder(new Pose2d(0, 0, Math.PI), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(-75, -8), new ConstantInterpolator(Math.PI))
            .build();

    static Trajectory DEPOT_TO_OTHER_CRATER = new TrajectoryBuilder(new Pose2d(0, 0, Math.PI), DriveConstants.BASE_CONSTRAINTS)
            .lineTo(new Vector2d(-75, 8), new ConstantInterpolator(Math.PI))
            .build();

    static Trajectory DOUBLE_RETURN_TO_DEPO = new TrajectoryBuilder(new Pose2d(0, 0, Math.PI), DriveConstants.RAPID_CONSTRAINTS)
            .lineTo(new Vector2d(24, 0), new ConstantInterpolator(Math.PI))
            .build();

    static Trajectory DEPOT_TO_SAME_CRATER_SHORT = new TrajectoryBuilder(new Pose2d(0, 0, Math.PI), DriveConstants.RAPID_CONSTRAINTS)
            .lineTo(new Vector2d(-65, -6), new ConstantInterpolator(Math.PI))
            .build();
}
