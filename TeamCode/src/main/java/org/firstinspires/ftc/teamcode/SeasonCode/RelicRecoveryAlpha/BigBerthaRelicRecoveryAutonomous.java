package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecoveryAlpha;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Components.DriveTrain.TwoMotor;
import org.firstinspires.ftc.teamcode.Utilities.ReadColor;
import org.firstinspires.ftc.teamcode.Utilities.SetRobot;
import org.firstinspires.ftc.teamcode.Utilities.UseIMU;
import org.firstinspires.ftc.teamcode.Utilities.UseVuforia;

import static org.firstinspires.ftc.teamcode.Utilities.ServoPositions.*;

/**
 * Created by Shane on 7/19/2017.
 */
@Autonomous(name = "Relic Recovery Autonomous Alpha", group = "Autonomous")
public class BigBerthaRelicRecoveryAutonomous extends OpMode {

    private BigBerthaRelicRecoveryRobot robot;

    /**
     * Encoders:
     * 60:1 1680
     * 40:1 1120
     * 20:1 560
     */
    private final static double COUNTS_PER_REVOLUTION = 1120.0; // 40:1 motor
    private final static double GEAR_ONE = 3.0;
    private final static double GEAR_TWO = 2.0;
    private final static double DIAMETER_OF_WHEEL = 4.0;

    private final static double RATIO = GEAR_ONE/GEAR_TWO;
    private final static double CIRCUMFERENCE_OF_WHEEL = DIAMETER_OF_WHEEL*Math.PI;
    private final static double COUNTS_PER_INCH = COUNTS_PER_REVOLUTION/RATIO/CIRCUMFERENCE_OF_WHEEL;

    UseVuforia useVuforia;
    ReadColor readColor;
    SetRobot setRobot;
    UseIMU useIMU;

    private boolean ifBlue;
    private int state = 0;
    private boolean ifDone = false;

    @Override
    public void init() {
        robot = new BigBerthaRelicRecoveryRobot(hardwareMap,telemetry);

        robot.driveTrain.mLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.driveTrain.mRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.driveTrain.mLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.driveTrain.mRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        useVuforia = new UseVuforia(hardwareMap,telemetry);
        readColor = new ReadColor(robot.sColor);
        setRobot = new SetRobot(telemetry);
        useIMU = new UseIMU(hardwareMap,telemetry);

        useVuforia.init();
        useIMU.init();
    }

    public void start() {
        useVuforia.start();
        useIMU.start();
    }

    enum States {
        READING_VALUES ,
        HIT_JEWEL,
        MOVE_OFF_PLATE,
        ROTATE,
        WAIT,
        TO_BOX,
        STOP
    }

    private States _state = States.READING_VALUES;
    private ReadColor.Color jewelColor = ReadColor.Color.NEITHER;

    @Override
    public void loop() {
        useIMU.run();
        switch(_state) {
            case READING_VALUES:
                robot.ballPusherPosition = BALL_PUSHER_DOWN;
                if(useVuforia.run() && readColor.readColor()) {
                    _state = States.HIT_JEWEL;
                }
                break;
            case HIT_JEWEL:
                jewelColor = readColor.getColorDetected();
                if (jewelColor == ReadColor.Color.RED) {
                    robot.ballRotatorPosition = BALL_ROTATOR_RIGHT;
                } else if (jewelColor == ReadColor.Color.BLUE) {
                    robot.ballRotatorPosition = BALL_ROTATOR_LEFT;
                } else {
                    telemetry.addData("Color", "neither, you messed up");
                }
                try {
                    Thread.sleep(250); // .1 second
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                _state = States.MOVE_OFF_PLATE;
                break;
            case MOVE_OFF_PLATE:
                robot.ballPusherPosition = BALL_PUSHER_UP;
                robot.ballRotatorPosition = BALL_ROTATOR_CENTER;
                robot.driveTrain.leftPower = 1;
                robot.driveTrain.rightPower = 1;
                if (robot.driveTrain.mRight.getCurrentPosition() > 24*COUNTS_PER_INCH) {
                    _state = States.ROTATE;
                }
                break;
            case ROTATE:
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        robot.driveTrain.leftPower = -.3;
                        robot.driveTrain.rightPower = .3;
                        while (useIMU.getHeading() < 90) {
                            telemetry.clear();
                            telemetry.addData("------HEADING------", useIMU.getHeading());
                        }
                        robot.driveTrain.leftPower = 0;
                        robot.driveTrain.rightPower = 0;
                        setRobot.power(robot.driveTrain.mRight,0,"right motor");
                        setRobot.power(robot.driveTrain.mLeft,0,"left motor");
                        _state = States.STOP;
                    }
                });
                t.start();
                _state = States.WAIT;
                break;
            case WAIT:
                telemetry.addData("Im waiting", "waiting");
                break;
            case TO_BOX:
                robot.driveTrain.leftPower = .5;
                robot.driveTrain.rightPower = .5;
                if (robot.driveTrain.mRight.getCurrentPosition() > (20+40+30)*COUNTS_PER_INCH) {
                    _state = States.STOP;
                }
                break;
            case STOP:
                telemetry.addData("------HEADING------", useIMU.getHeading());
                robot.driveTrain.leftPower = 0;
                robot.driveTrain.rightPower = 0;
            default:
                telemetry.addData("Test", "Cry");
        }

        telemetry.addData("State",_state);
        telemetry.addData("blue",robot.sColor.blue());
        telemetry.addData("red",robot.sColor.red());
        telemetry.addData("right encoder",robot.driveTrain.mRight.getCurrentPosition());
        telemetry.addData("left encoder",robot.driveTrain.mLeft.getCurrentPosition());
        telemetry.addData("arm lift encoder",robot.mArmLift.getCurrentPosition());
        telemetry.addData("lift encoder",robot.mLift.getCurrentPosition());
    }

}
