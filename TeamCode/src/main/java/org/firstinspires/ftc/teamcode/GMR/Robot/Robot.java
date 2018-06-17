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
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.Vision;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.DriveTrain;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.RelicGrab;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pston on 11/12/2017
 */

public class Robot {

    public DriveTrain driveTrain;
    public BlockLift blockLift;
    public RelicGrab relicGrab;
    public Vision vision;

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
    public Servo relicClamp;

    private Servo rightColor;
    private Servo leftColor;

    private int ultraRange = 0;
    private int distanceThreshold = 7;
    private int columnPassed = 0;
    private boolean distanceChange = false;
    private RelicRecoveryVuMark currentColumn;
    private double strafePower = .10;
    private int strafetimes = 0;

    private OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;
    private VuforiaLocalizer.Parameters parameters;

    private List<Integer> rangeList;
    private boolean canChange = true;


    public Robot (HardwareMap hardwareMap, Telemetry telemetry, Boolean enableVuforia) {

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

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        rangeList = new ArrayList<>(22);

        rightColor.setPosition(0);
        leftColor.setPosition(0.85);

        relicTilt.setPosition(1);
        relicClamp.setPosition(0.4);

        driveTrain = new DriveTrain(leftFront, rightFront, leftRear, rightRear, gyro, telemetry);

        blockLift = new BlockLift(liftMotor, leftGrab, rightGrab);

        relicGrab = new RelicGrab(relicLift, relicTilt, relicClamp);

        if(enableVuforia){vision = new Vision(vuforia, parameters, relicTrackables, relicTemplate);}
    }

    public void setServos() {
        rightColor.setPosition(0);
        leftColor.setPosition(0.85);
    }

    public void setRelicTilt() {
        relicTilt.setPosition(1);
        relicClamp.setPosition(1);
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

    private  DriveTrain.Direction columnAlignDirection(AllianceColor color) {
        if(color == AllianceColor.RED) {
            return(DriveTrain.Direction.W);
        } else {
            return(DriveTrain.Direction.E);
        }
    }

    private double average(List<Integer> list) {
        int total = 0;
        for (int item: list) {
            total += item;
        }
        return total/list.size();
    }
}
