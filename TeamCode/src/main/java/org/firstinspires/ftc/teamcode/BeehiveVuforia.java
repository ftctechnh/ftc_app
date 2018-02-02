package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 12/26/17.
 */

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

public class BeehiveVuforia {
    private VuforiaLocalizer vuforia;
    private RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;
    private HardwareMap hardwareMap;
    private Telemetry telemetry;
    private VuforiaTrackable relicTemplate;
    private VuforiaTrackables relicTrackables;
    private ElapsedTime time;
    private Servo servo;
    private final double LEFT_POSITION = 0;
    private final double RIGHT_POSITION = 1;
    private final double MIDDLE_POSITION = 0.5;
    public BeehiveVuforia(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AQfaFkD/////AAAAGXGcl3Y64kcghjX73kddwxOG8QFmwZwdDenQL/6cYT4JrZ70fydV0F5+iIWald5VzqX9BOtH9HwJ93W9oSnZmSwZSEQbnV3ELVR08qyIoujP5Z7O5p9yyepVydgdsjNw2shES0SmGoqhJF25ZIBN2YRVAYM++FTu4nuEEpLxN9LzbnrYLEfZB6mcuV9jea6D+CLXoQW7VpRpey73HjKCxPw1Hs3CjRx9/80Z6AR8YNjr3Yqx5MSZWNIn48rSR+nC0urM6YLs8xBwNA662icRKwkgAoCUXehfvxjK6LcSCnuQKG76IlOmSp3SZB9MFJ1HasxaFLxfS1xEa+6fdA7jE/WhukyuNvzmOVrWatS2WWDm";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        time = new ElapsedTime();
        vuMark = RelicRecoveryVuMark.UNKNOWN;
        servo = hardwareMap.servo.get("s7");
    }
    public RelicRecoveryVuMark getMark() {
        relicTrackables.activate();
        time.start();
        while(vuMark == RelicRecoveryVuMark.UNKNOWN && time.getElapsedTime()<=3000) {
          vuMark = RelicRecoveryVuMark.from(relicTemplate);
        }
        if(time.getElapsedTime()<=3000) {
          setServoPosition(LEFT_POSITION);
          time.start();
          while(vuMark == RelicRecoveryVuMark.UNKNOWN && time.getElapsedTime()<=3000) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
          }
        }
        setServoPosition(MIDDLE_POSITION);
        telemetry.addData("Pictograph", "%s visible", vuMark);
        telemetry.update();
        return vuMark;
      }
    public void setServoPosition(double position) {
      servo.setPosition(position);
      servo.setPosition(position);
    }
}
