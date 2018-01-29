package org.firstinspires.ftc.teamcode.GMR.Robot;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.AllianceColor;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.BlockLift;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.ColumnDetection;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.DriveTrain;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.RelicGrab;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by pston on 11/12/2017
 */

public class Robot {

    public DriveTrain driveTrain;
    public BlockLift blockLift;
    public RelicGrab relicGrab;
    public ColumnDetection columnDetection;

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;
    private NavxMicroNavigationSensor gyroReference;
    private IntegratingGyroscope gyro;

    private DcMotor liftMotor;
    private DcMotor leftGrab;
    private DcMotor rightGrab;

    private DcMotor relicLift;
    private Servo slideLift;
    private Servo relicTilt;
    private Servo relicClamp;

    private Servo rightColor;
    private Servo leftColor;

    private ModernRoboticsI2cRangeSensor rangeSensor;

    private int ultraRange = 0;
    private int distanceThreshold = 4;
    private int columnPassed = 0;
    private boolean distanceChange = false;
    private RelicRecoveryVuMark currentColumn;
    private double strafePower = .25;
    private int strafetimes = 0;

    private OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;
    private VuforiaLocalizer.Parameters parameters;


    public Robot (HardwareMap hardwareMap, Telemetry telemetry) {

        gyro = hardwareMap.get(NavxMicroNavigationSensor.class, "navx");
        leftFront = hardwareMap.dcMotor.get("leftfront");
        rightFront = hardwareMap.dcMotor.get("rightfront");
        leftRear = hardwareMap.dcMotor.get("leftrear");
        rightRear = hardwareMap.dcMotor.get("rightrear");

        liftMotor = hardwareMap.dcMotor.get("liftmotor");
        leftGrab = hardwareMap.dcMotor.get("leftgrab");
        rightGrab = hardwareMap.dcMotor.get("rightgrab");

        rightColor = hardwareMap.servo.get("rightArm");
        leftColor = hardwareMap.servo.get("leftArm");

        relicLift = hardwareMap.dcMotor.get("reliclift");
        slideLift = hardwareMap.servo.get("slidelift");
        relicTilt = hardwareMap.servo.get("relictilt");
        relicClamp = hardwareMap.servo.get("relicclamp");

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        
        rightColor.setPosition(0);
        leftColor.setPosition(0.85);

        driveTrain = new DriveTrain(leftFront, rightFront, leftRear, rightRear, gyro, telemetry);

        blockLift = new BlockLift(liftMotor, leftGrab, rightGrab);

        relicGrab = new RelicGrab(relicLift, slideLift, relicTilt, relicClamp);

        columnDetection = new ColumnDetection(vuforia, parameters, relicTrackables, relicTemplate);
    }

    public void setServos() {
        rightColor.setPosition(0);
        leftColor.setPosition(0.85);
    }

    private int rawUltrasonic() {
        return rangeSensor.rawUltrasonic();
    }

    private RelicRecoveryVuMark getCurrentColumn(int columnPassed, AllianceColor color) {
        if (color == AllianceColor.RED) {
            switch (columnPassed) {
                case 1:
                    return(RelicRecoveryVuMark.RIGHT);
                case 2:
                    return(RelicRecoveryVuMark.CENTER);
                case 3:
                    return(RelicRecoveryVuMark.LEFT);

            }
        } else {
            switch (columnPassed) {
                case 1:
                    return(RelicRecoveryVuMark.LEFT);
                case 2:
                    return(RelicRecoveryVuMark.CENTER);
                case 3:
                    return(RelicRecoveryVuMark.RIGHT);
            }
        }
        return RelicRecoveryVuMark.UNKNOWN;
    }

    private void driveDirection(AllianceColor color) {
        if (color == AllianceColor.RED) {
            driveTrain.drive(DriveTrain.Direction.W, strafePower);
        } else {
            driveTrain.drive(DriveTrain.Direction.E, strafePower);
        }
    }

    public boolean columnMove(RelicRecoveryVuMark goalColumn, AllianceColor color, Telemetry telemetry) {

        if (ultraRange != 0) {
            if ((rawUltrasonic() >= ultraRange + distanceThreshold) && !distanceChange) {
                columnPassed += 1;
                distanceChange = true;
            } else if (rawUltrasonic() >= ultraRange - distanceThreshold) {
                distanceChange = false;
            }
        }

        ultraRange = rawUltrasonic();

        currentColumn = getCurrentColumn(columnPassed, color);

        telemetry.addData("Column Passed", columnPassed);
        telemetry.addData("Goal Column", goalColumn);
        telemetry.addData("Raw Ultrasonic", rawUltrasonic());

        if (goalColumn != currentColumn) {
            driveDirection(color);
            return false;
        } else {
            driveTrain.stop();
            return true;
        }

    }

    private  DriveTrain.Direction columnAlignDirection(AllianceColor color) {
        if(color == AllianceColor.RED) {
            return(DriveTrain.Direction.W);
        } else {
            return(DriveTrain.Direction.E);
        }
    }

    public boolean columnDrive(AllianceColor color, Telemetry telemetry, int goalColumn) {

        if (ultraRange - rawUltrasonic() >= distanceThreshold && ultraRange != 0) {
            columnPassed += 1;
        }

        ultraRange = rawUltrasonic();

        telemetry.addData("Column Passed", columnPassed);
        telemetry.addData("Raw Ultrasonic", rawUltrasonic());

        if (columnPassed >= goalColumn) {
            return(driveTrain.encoderDrive(columnAlignDirection(color), strafePower, .9));
        } else {
            driveDirection(color);
            return false;
        }
    }
}
