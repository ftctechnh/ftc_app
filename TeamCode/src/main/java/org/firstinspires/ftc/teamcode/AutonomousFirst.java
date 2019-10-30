package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "AutonomousFirst", group = "Autonomous")
public class AutonomousFirst extends LinearOpMode {
    //------------------------------------------// claw variables:
    static final double INCREMENT = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int CYCLE_MS = 50;     // period of each cycle
    static final double MAX_POS = 1.0;     // Maximum rotational position
    static final double MIN_POS = 0.0;     // Minimum rotational position
    //time
    private ElapsedTime runtime = new ElapsedTime();
    // Motor variables
    private DcMotor FL = null;
    private DcMotor FR = null;
    private DcMotor BL = null;
    private DcMotor BR = null;
    private double leftPower;
    private double rightPower;
    private double clawPosition;
    private Servo claw = null;

    @Override
    public void runOpMode() throws InterruptedException {
        runtime.reset();

    }

    void setDrivePower() {

    }

    void setClawPosition() {

    }

    void DriveForSeconds(double seconds, double rPower, double lPower) {
        double targetTime = runtime.time() + seconds;
        while (runtime.time() < targetTime && opModeIsActive()) {
            leftPower = lPower;
            rightPower = rPower;
        }
    }
}
