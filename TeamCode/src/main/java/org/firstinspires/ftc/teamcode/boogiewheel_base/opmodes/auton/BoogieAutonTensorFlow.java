package org.firstinspires.ftc.teamcode.boogiewheel_base.opmodes.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.RobotState;
import org.firstinspires.ftc.teamcode.framework.opModes.AbstractAuton;
import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.SamplePosition;
import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.TensorFlow;

@Autonomous(name = "BoogieWheel Auton Tensorflow", group = "New")
//@Disabled

public class BoogieAutonTensorFlow extends AbstractAuton {

    private TensorFlow tensorFlow;
    ElapsedTime Mineraltimer;


    Robot robot;
    double speed = 1, error = 3;
    int period = 100;

    @Override
    public void Init() {
        //IF YOU HIT INIT AND WAIT A LITTLE BIT, THE CAMERA WILL FIND THE OBJECT MUCH FASTER
        robot = new Robot();

        tensorFlow = new TensorFlow(TensorFlow.CameraOrientation.HORIZONTAL, false);
        Mineraltimer = new ElapsedTime();
    }

    @Override
    public void Run() {
        SamplePosition TensorPosition;
        double MineraldetectionTime;


        tensorFlow.start();
        Mineraltimer.reset();  //timer = 0


        //If the object is not found then it will wait until it finds the object
        while (isOpModeActive()
                && (tensorFlow.getSamplePosition() == SamplePosition.UNKNOWN)
                && (Mineraltimer.milliseconds() < 10000)
                ) {
            // wait indefinitly until position of object is returned
            //If position == unknown then it waits until it finds it ....

        }
        MineraldetectionTime = Mineraltimer.milliseconds();
        telemetry.addData("Mineral Time = " + MineraldetectionTime);
        //after it exits the while loop, find out where mineral is
        TensorPosition = (tensorFlow.getSamplePosition());

        if (TensorPosition == SamplePosition.UNKNOWN) {
            TensorPosition = SamplePosition.LEFT;
        }


        switch (TensorPosition) {
            case LEFT: {

                telemetry.addData("-----LEFT-----");
                telemetry.update();
                tensorFlow.stop();
                //  craterSideLeftMineral();
                break;
            }

            case CENTER: {
                telemetry.addData("-----CENTER-----");
                telemetry.update();
                tensorFlow.stop();
                //craterSideCenterMineral();
                break;
            }

            case RIGHT: {
                telemetry.addData("-----RIGHT-----");
                telemetry.update();
                tensorFlow.stop();
                //craterSideRightMineral();
                break;

            }

        } //Switch statement

        delay(5000);
    }

    @Override
    public void Stop() {
        robot.stop();
        tensorFlow.stop();
    }

    public void craterSideLeftMineral() {
        robot.driveTo(6, speed);

        robot.turnTo(25, speed, error, period);

        robot.driveTo(24, speed);

        robot.turnTo(45, speed, error, period);

        robot.driveTo(-8, speed);

        craterSideToCrater();
    }

    public void craterSideCenterMineral() {
        robot.driveTo(6, speed);

        robot.driveTo(24, speed);

        robot.driveTo(-6, speed);

        craterSideToCrater();
    }

    public void craterSideRightMineral() {
        robot.driveTo(6, speed);

        robot.turnTo(-25, speed, error, period);

        robot.driveTo(24, speed);

        robot.turnTo(-45, speed, error, period);

        robot.driveTo(-10, speed);

        craterSideToCrater();
    }

    public void craterSideToCrater() {
        robot.turnTo(90, speed, error, period);

        robot.driveTo(38, speed);

        robot.turnTo(135, speed, error, period);

        robot.driveTo(40, speed);

        robot.driveTo(-65, speed);

        robot.driveTo(-10, 0.7);
    }

    public void depotSideRightMineral() {
        robot.turnTo(-25, speed, error, period);

        robot.driveTo(32, speed);

        robot.turnTo(25, speed, error, period);

        robot.driveTo(34, speed);

        robot.turnTo(45, speed, error, period);

        robot.driveTo(-68, speed);

        robot.driveTo(-10, 0.5);
    }

    public void aroundField() {
        robot.driveTo(65, speed);

        robot.turnTo(-45, speed, error, period);

        robot.driveTo(78, speed);

        robot.turnTo(-90, speed, error, period);

        robot.driveTo(62, speed);
    }
}
