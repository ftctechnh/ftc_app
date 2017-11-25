package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utilities.ReadColor;
import org.firstinspires.ftc.teamcode.Utilities.SetRobot;
import org.firstinspires.ftc.teamcode.Utilities.UseVuforia;

/**
 * Created by Shane on 7/19/2017.
 */

@Autonomous(name = "Relic Recovery Autonomous", group = "Autonomous")
public class RelicRecoveryAutonomous extends RelicRecoveryAutoMeth {

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

    private boolean ifBlue;
    private int state = 0;
    private boolean ifDone = false;

    @Override
    public void init() {
        super.init();

        mLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        mLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        useVuforia = new UseVuforia(hardwareMap,telemetry);
        readColor = new ReadColor(sColor);
        setRobot = new SetRobot(telemetry);

        useVuforia.init();
    }

    public void start() {
        useVuforia.start();
    }

    enum States {
        READING_VALUES ,
        HIT_JEWEL,
        MOVE_OFF_PLATE,
        ROTATE,
        TO_BOX,
        STOP
    }

    private States _state = States.READING_VALUES;
    private ReadColor.Color jewelColor = ReadColor.Color.NEITHER;

    @Override
    public void loop() {
        super.loop();

        switch(_state)
        {
            case READING_VALUES:
                ballPusherPosition = BALL_PUSHER_DOWN;
                if(useVuforia.run() && readColor.readColor()) {
                    _state = States.HIT_JEWEL;
                }
                break;
            case HIT_JEWEL:
                jewelColor = readColor.getColorDetected();
                if (jewelColor == ReadColor.Color.RED) {
                    ballRotatorPosition = BALL_ROTATE_RIGHT;
                } else if (jewelColor == ReadColor.Color.BLUE) {
                    ballRotatorPosition = BALL_ROTATE_LEFT;
                } else {
                    telemetry.addData("Color", "neither, you messed up");
                }
                try {
                    Thread.sleep(100); // .1 second
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                _state = States.MOVE_OFF_PLATE;
                break;
            case MOVE_OFF_PLATE:
                ballPusherPosition = BALL_PUSHER_UP;
                ballRotatorPosition = BALL_ROTATE_CENTER;
                leftPower = 1;
                rightPower = 1;
                if (mRight.getCurrentPosition() > 20*COUNTS_PER_INCH) {
                    _state = States.STOP;
                }
                break;
            case ROTATE:
                leftPower = .3;
                rightPower = 1;
                if (mRight.getCurrentPosition() > (20+40)*COUNTS_PER_INCH) {
                    _state = States.TO_BOX;
                }
                break;
            case TO_BOX:
                leftPower = 1;
                rightPower = 1;
                if (mRight.getCurrentPosition() > (20+40+30)*COUNTS_PER_INCH) {
                    _state = States.STOP;
                }
                break;
            case STOP:
                leftPower = 0;
                rightPower = 0;
            default:
                telemetry.addData("Test", "Cry");
        }

        telemetry.addData("State",_state);
        telemetry.addData("blue",sColor.blue());
        telemetry.addData("red",sColor.red());
        telemetry.addData("right encoder", mRight.getCurrentPosition());
        telemetry.addData("left encoder", mLeft.getCurrentPosition());
        telemetry.addData("arm lift encoder", mArmLift.getCurrentPosition());
        telemetry.addData("lift encoder", mLift.getCurrentPosition());

        setMotorPower();
    }
    private void setMotorPower() {
        // -------------- DcMotors --------------
        setRobot.power(mRight,rightPower,"right motor");
        setRobot.power(mLeft,leftPower,"left motor");
        setRobot.power(mLift,liftPower,"lift motor");
        setRobot.power(mArm,armPower,"arm motor");
        setRobot.power(mArmLift,armLifterPwr,"arm lift motor");
        // ---------- Standard Servos -----------
        setRobot.position(ssBallPusher,ballPusherPosition,"ball pusher servo");
        setRobot.position(ssArm,armPosition,"arm servo");
        setRobot.position(ssRelicGrabber,oneHandPosition,"relic grabber servo");
        setRobot.position(ssPoop,poopPosition,"poop");
        setRobot.position(ssBallRotator, ballRotatorPosition,"ball rototor sero");
        // ----- Continuous Rotation Servos -----
        setRobot.position(crHand,crHandPosition,"hand crservo");
        setRobot.position(crRelicGrabber,relicPosition, "relic grabber crservo");
        setRobot.position(crArmLift,armLifterSPosition,"arm lift crservo");
        setRobot.position(crArm,armPower,"arm crservo");
    }

}
