package org.firstinspires.ftc.teamcode;

import android.content.SharedPreferences;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Java Mecanum TeleOp")
public class MecanumTeleOpJava extends OpMode {

    private boolean tankStyleControl = false;
    private Rebound drivetrain;

    @Override
    public void init() {
        DcMotor lf = hardwareMap.dcMotor.get("lf");
        DcMotor lb = hardwareMap.dcMotor.get("lb");
        DcMotor rf = hardwareMap.dcMotor.get("rf");
        DcMotor rb = hardwareMap.dcMotor.get("rb");
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        drivetrain = new Rebound(telemetry, hardwareMap, lf, lb, rf, rb, imu);
        drivetrain.initialize(DcMotor.RunMode.RUN_WITHOUT_ENCODER, DcMotor.ZeroPowerBehavior.BRAKE);
        telemetry.addData("Initial Orientation: ", drivetrain.getOrientation());
        SharedPreferences sharedPrefs = hardwareMap.appContext.getSharedPreferences("app_preferences.xml", 0);
        telemetry.addLine(String.valueOf(sharedPrefs.contains("start_point")));

    }

    @Override
    public void loop() {
        telemetry.addData("Orientation: ", drivetrain.getOrientation());
        if (gamepad1.a) {tankStyleControl = !tankStyleControl;}
        if (tankStyleControl) {
            drivetrain.tankMecanum(gamepad1);
        } else {drivetrain.arcadeMecanum(gamepad1);}
    }
}
