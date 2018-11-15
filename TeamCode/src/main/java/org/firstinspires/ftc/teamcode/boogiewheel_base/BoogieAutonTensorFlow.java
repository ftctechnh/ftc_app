package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.AbstractAuton;
import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.SamplePosition;
import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.TensorFlow;
import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.SlewDcMotor;

@Autonomous(name = "boogiewheel_auton_tensorflow", group = "New")
//@Disabled

public class BoogieAutonTensorFlow extends AbstractAuton {

    private TensorFlow tensorFlow;

    Robot robot;
    double speed = 1, error = 3;
    int period = 100;

    private SlewDcMotor intakeMotor;

    @Override
    public void Init() {
        robot = new Robot();
        intakeMotor = new SlewDcMotor(hardwareMap.dcMotor.get("intake"));
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        tensorFlow = new TensorFlow(TensorFlow.CameraOrientation.HORIZONTAL, false);

    }

    @Override
    public void Run() {
        tensorFlow.start();

        while (tensorFlow.getSamplePosition() == SamplePosition.UNKNOWN);

        switch (tensorFlow.getSamplePosition()) {
            case LEFT: {
                telemetry.addData("-----LEFT-----");
                telemetry.update();
                tensorFlow.stop();
                craterSideLeftMineral();
                break;
            }

            case CENTER: {
                telemetry.addData("-----CENTER-----");
                telemetry.update();
                tensorFlow.stop();
                craterSideCenterMineral();
                break;
            }

            case RIGHT: {
                telemetry.addData("-----RIGHT-----");
                telemetry.update();
                tensorFlow.stop();
                craterSideRightMineral();
                break;
            }
        }
    }

    @Override
    public void Stop() {
        robot.stop();
        tensorFlow.stop();
    }

    public void craterSideLeftMineral() {
        robot.driveTo(6, speed);

        robot.turnTo(25, speed, error, period);

        intakeMotor.setPower(1);

        robot.driveTo(24, speed);

        intakeMotor.setPower(0);

        robot.turnTo(45, speed, error, period);

        robot.driveTo(-8, speed);

        craterSideToCrater();
    }

    public void craterSideCenterMineral() {
        robot.driveTo(6, speed);

        intakeMotor.setPower(1);

        robot.driveTo(24, speed);

        intakeMotor.setPower(0);

        robot.driveTo(-6, speed);

        craterSideToCrater();
    }

    public void craterSideRightMineral() {
        robot.driveTo(6, speed);

        robot.turnTo(-25, speed, error, period);

        intakeMotor.setPower(1);

        robot.driveTo(24, speed);

        intakeMotor.setPower(0);

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
