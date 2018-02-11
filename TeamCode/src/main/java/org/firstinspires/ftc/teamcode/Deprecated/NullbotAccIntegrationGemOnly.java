package org.firstinspires.ftc.teamcode.Deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.NullbotHardware;

/**
 * Created by guberti on 10/17/2017.
 */
@Disabled
@Autonomous(name="FRONT BLUE acc integration test", group="D_Autonomous")
public class NullbotAccIntegrationGemOnly extends LinearOpMode {

    NullbotHardware robot = new NullbotHardware();

    final int DISTANCE_TO_DRIVE = 400;
    final Position DESIRED_POSITION = new Position(DistanceUnit.METER, 0.5, 0, 0, 0);
    final double accuracy = 0.005;

    double calibXSum = 0;
    double calibYSum = 0;
    double calibZSum = 0;

    PixyCam pixyCam;
    PixyCam.Block redBall;
    PixyCam.Block blueBall;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, gamepad1, gamepad2);

        int tick = 0;
        double ySum = 0;
        double xSum = 0;
        double zSum = 0;
        /*while (opModeIsActive()) {
            tick++;
            robot.updateReadings();
            Position currentPos = robot.imu.getPosition();
            Acceleration a = robot.imu.getAcceleration();


            double distance = getDistance(currentPos, DESIRED_POSITION);
            if (distance < accuracy) break;

            double degX = DESIRED_POSITION.x - currentPos.x;
            double degY = DESIRED_POSITION.y - currentPos.y;
            double angle = robot.normAngle(Math.atan2(degX, degY) + Math.PI / 2);
            double[] powers = robot.getDrivePowersFromAngle(angle);
            double drivePower = Math.max(distance, 0.2);
            double multiplier = drivePower/getMaxVal(powers);

            for (int i = 0; i < powers.length; i++) powers[i] *= multiplier;

            robot.setMotorSpeeds(powers);

            xSum +=  a.xAccel;
            ySum +=  a.yAccel;
            zSum +=  a.zAccel;

            telemetry.addData("xSum", xSum);
            telemetry.addData("ySum", ySum);
            telemetry.addData("zSum", zSum);

            telemetry.addData("xAcc", a.xAccel);
            telemetry.addData("yAcc", a.yAccel);
            telemetry.addData("zAcc", a.zAccel);

            telemetry.addData("Current tick", tick);
            telemetry.addData("dx", degX);
            telemetry.addData("dy", degX);
            telemetry.addData("angle", angle);
            telemetry.update();
        }*/
        while (opModeIsActive()) {
            averageValuesForTime(5000);
        }
    }

    public void updateBlocks() {
        redBall = pixyCam.GetBiggestBlock(1);
        blueBall = pixyCam.GetBiggestBlock(2);
    }

    public double getDistance(Position a, Position b) {
        double xd = a.x - b.x;
        double yd = a.y - b.y;

        return Math.sqrt(xd * xd + yd * yd);

    }

    public double getMaxVal(double[] arr) {
        double m = Double.MIN_VALUE;
        for (double a : arr) {
            m = Math.max(m, a);
        }
        return m;
    }

    public void averageValuesForTime(int ms) {
        ElapsedTime timer = new ElapsedTime();
        int readingNum = 0;
        calibXSum = 0;
        calibYSum = 0;
        calibZSum = 0;

        while (timer.milliseconds() < ms) {
            robot.updateReadings();
            Acceleration a = robot.imu.getAcceleration();

            calibXSum += a.xAccel;
            calibYSum += a.yAccel;
            calibZSum += a.zAccel;
            readingNum++;
        }

        telemetry.addData("Seconds averaged", timer.milliseconds());
        telemetry.addData("Readings taken", readingNum);
        telemetry.addData("", "----------");
        telemetry.addData("Total x accel", calibXSum);
        telemetry.addData("Total y accel", calibYSum);
        telemetry.addData("Total z accel", calibZSum);
        telemetry.addData("", "----------");
        telemetry.addData("Average x accel", calibXSum / (double) readingNum);
        telemetry.addData("Average y accel", calibYSum / (double) readingNum);
        telemetry.addData("Average z accel", calibZSum / (double) readingNum);
        telemetry.update();

    }
}
