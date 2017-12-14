package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotplus.autonomous.VuforiaWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.ColorSensorWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.IMUWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.MecanumDrive;
import org.firstinspires.ftc.teamcode.robotplus.hardware.Robot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotplus.autonomous.VuforiaWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.ColorSensorWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.IMUWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.MecanumDrive;
import org.firstinspires.ftc.teamcode.robotplus.hardware.Robot;

/**
 * Created by BAbel on 11/2/2017.
 */

//@Disabled
@Autonomous(name = "Blake tryina code (servo)", group = "Competition OpModes")
public class ServoJewelAuto extends LinearOpMode {

    private Robot robot;
    private DcMotor raiser;
    private Servo grabber;
    private MecanumDrive drivetrain;
    private IMUWrapper imuWrapper;
    private VuforiaWrapper vuforiaWrapper;

    private Servo armExtender;
    private Servo armRotator;
    private ColorSensorWrapper colorSensorWrapper;

    private RelicRecoveryVuMark relicRecoveryVuMark;

    @Override
    public void runOpMode(){

        //Initialize hardware
        robot = new Robot(hardwareMap);
        drivetrain = (MecanumDrive)robot.getDrivetrain();
        raiser = hardwareMap.dcMotor.get("raiser");
        grabber = hardwareMap.servo.get("grabber");
        imuWrapper = new IMUWrapper(hardwareMap);
        vuforiaWrapper = new VuforiaWrapper(hardwareMap);

        //Assuming other hardware not yet on the robot
        armRotator = hardwareMap.servo.get("armRotator");
        armExtender = hardwareMap.servo.get("armExtender");

        armRotator.scaleRange(0.1,0.9);
        armExtender.scaleRange(0.16, 0.75);

        armExtender.setPosition(1.0);
        armRotator.setPosition(0.5);

        colorSensorWrapper = new ColorSensorWrapper(hardwareMap);

        vuforiaWrapper.getLoader().getTrackables().activate();

        waitForStart();

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

        //Checks that blue jewel is closer towards the cryptoboxes (assuming color sensor is facing forward
        if (colorSensorWrapper.getRGBValues()[2] > colorSensorWrapper.getRGBValues()[0]) {
            armRotator.setPosition(1);
        } else {
            armRotator.setPosition(0);
        }

        sleep(5000);

        armExtender.setPosition(1);
        armRotator.setPosition(0.5);

        sleep(10000);

        //STEP 3: Get to center tape thing
        while (//imuWrapper.getPosition().toUnit(DistanceUnit.INCH).y < 36 ||
                colorSensorWrapper.getRGBValues()[2] < 0.5) {
            drivetrain.complexDrive(MecanumDrive.Direction.UP.angle(), 0.5, 0);
        }

        //STEP 4: Rotate
        while (imuWrapper.getOrientation().toAngleUnit(AngleUnit.RADIANS).firstAngle > Math.PI/2){
            drivetrain.complexDrive(0, 0, -0.2);
        }

        //STEP 5: Move to the thingo

        //Move using the x position and an approximate angle for mecanum drive (can refine with tests or trig)
        while (imuWrapper.getPosition().toUnit(DistanceUnit.INCH).x > -9) {
            switch (relicRecoveryVuMark) {
                case LEFT: drivetrain.complexDrive(MecanumDrive.Direction.UPLEFT.angle(), 0.1, 0);
                    break;
                case CENTER: drivetrain.complexDrive(MecanumDrive.Direction.UP.angle(), 0.1, 0);
                    break;
                case RIGHT: drivetrain.complexDrive(MecanumDrive.Direction.UPRIGHT.angle(), 0.1, 0);
                    break;
                default: telemetry.addData("Glyph", "Not going towards the cryptobox lol prankt");
            }
        }
        //Move using color sensor
        //nah

    }
}
