package org.firstinspires.ftc.teamcode.compautonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotplus.autonomous.VuforiaWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.ColorSensorWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.GrabberPrimer;
import org.firstinspires.ftc.teamcode.robotplus.hardware.IMUWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.MecanumDrive;
import org.firstinspires.ftc.teamcode.robotplus.hardware.Robot;

/**
 * Red Right Scenario
 * @author Alex Migala, Blake Abel
 * @since 12/30/17
 */
@Autonomous(name="RedRight", group="compauto")
public class RedRight extends LinearOpMode implements Settings {

    private Robot robot;
    private DcMotor raiser;
    private Servo grabber;
    private MecanumDrive drivetrain;
    private IMUWrapper imuWrapper;
    private VuforiaWrapper vuforiaWrapper;
    private GrabberPrimer grabberPrimer;

    private Servo armExtender;
    private Servo armRotator;
    private ColorSensorWrapper colorSensorWrapper;

    private RelicRecoveryVuMark relicRecoveryVuMark;

    @Override
    public void runOpMode() throws InterruptedException {

        //Initialize hardware
        robot = new Robot(hardwareMap);
        drivetrain = (MecanumDrive) robot.getDrivetrain();
        raiser = hardwareMap.dcMotor.get("raiser");
        grabber = hardwareMap.servo.get("grabber");
        imuWrapper = new IMUWrapper(hardwareMap);
        vuforiaWrapper = new VuforiaWrapper(hardwareMap);
        grabberPrimer = new GrabberPrimer(this.grabber);

        //Assuming other hardware not yet on the robot
        armRotator = hardwareMap.servo.get("armRotator");
        armExtender = hardwareMap.servo.get("armExtender");

        armRotator.scaleRange(0.1, 0.9);
        armExtender.scaleRange(0.16, 0.75);

        armExtender.setPosition(1.0);
        armRotator.setPosition(0.5);

        this.grabberPrimer.initSystem();

        colorSensorWrapper = new ColorSensorWrapper(hardwareMap);

        vuforiaWrapper.getLoader().getTrackables().activate();

        waitForStart();

        this.grabberPrimer.grab();

        //STEP 1: Scan vuforia pattern
        relicRecoveryVuMark = RelicRecoveryVuMark.from(vuforiaWrapper.getLoader().getRelicTemplate());

        if (relicRecoveryVuMark != RelicRecoveryVuMark.UNKNOWN) {
            telemetry.addData("VuMark Column", relicRecoveryVuMark.name());
        } else {
            telemetry.addData("VuMark Column", "borked");
        }
        telemetry.update();

        //STEP 2: Hitting the jewel
        armExtender.setPosition(0); //servo in 'out' position

        sleep(2000);

        telemetry.addData("Color Sensor", "R: %f \nB: %f ", colorSensorWrapper.getRGBValues()[0], colorSensorWrapper.getRGBValues()[2]);
        //Checks that blue jewel is closer towards the cryptoboxes (assuming color sensor is facing forward
        if (Math.abs(colorSensorWrapper.getRGBValues()[2] - colorSensorWrapper.getRGBValues()[0]) < 30) {
            telemetry.addData("Jewels", "Too close.");
        } else if (colorSensorWrapper.getRGBValues()[2] > colorSensorWrapper.getRGBValues()[0]) {
            armRotator.setPosition(1);
            telemetry.addData("Jewels", "Blue Team!");
        } else {
            armRotator.setPosition(0);
            telemetry.addData("Jewels", "Red Team!");
        }
        telemetry.update();

        sleep(1000);

        armExtender.setPosition(1);
        armRotator.setPosition(0.5);

        sleep(1000);

        imuWrapper.getIMU().initialize(imuWrapper.getIMU().getParameters());

        // move backwards and slam into the wall
        this.drivetrain.complexDrive(MecanumDrive.Direction.DOWN.angle(), 1, 0);
        sleep(forwardShort);
        this.drivetrain.complexDrive(MecanumDrive.Direction.RIGHT.angle(), 1, 0);
        sleep(sideShort);
        // turn clockwise
        this.drivetrain.complexDrive(0, 0, 0.5);
        sleep(2*rotate90);
        this.drivetrain.stopMoving();
        switch (relicRecoveryVuMark) {
            case LEFT: this.drivetrain.complexDrive(MecanumDrive.Direction.UPLEFT.angle(), 1, 0); break;
            case CENTER: this.drivetrain.complexDrive(MecanumDrive.Direction.UP.angle(), 1, 0); break;
            case RIGHT: this.drivetrain.complexDrive(MecanumDrive.Direction.UPRIGHT.angle(), 1, 0); break;
            default: this.drivetrain.complexDrive(MecanumDrive.Direction.UP.angle(), 1, 0 ); break;
        }

        this.grabberPrimer.open();
    }
}