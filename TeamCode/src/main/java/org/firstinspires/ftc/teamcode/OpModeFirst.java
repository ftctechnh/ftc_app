package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Sbev's first steps", group="Iterative Opmode")
public class OpModeFirst extends OpMode {
    //time
    private ElapsedTime runtime = new ElapsedTime();

    // Motor bois
    private DcMotor FL = null;
    private DcMotor FR = null;
    private DcMotor BL = null;
    private DcMotor BR = null;
    private DcMotor SV = null;


    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        FL = hardwareMap.get(DcMotor.class, "fl");
        FR = hardwareMap.get(DcMotor.class, "fr");
        BL = hardwareMap.get(DcMotor.class, "bl");
        BR = hardwareMap.get(DcMotor.class, "br");
        SV = hardwareMap.get(DcMotor.class,"sv" );

        FL.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.REVERSE);
        FR.setDirection(DcMotor.Direction.FORWARD);
        BR.setDirection(DcMotor.Direction.FORWARD);
        SV.setDirection(DcMotor.Direction.FORWARD);

    }
    @Override
    public void start(){
        runtime.reset();
    }

    @Override
    public void loop() {
        double leftPower;
        double rightPower;
        double servoPower:

        double drive = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;

        leftPower = Range.clip(drive + turn, -1.0, 1.0);
        rightPower = Range.clip(drive - turn, -1.0, 1.0);


        FL.setPower(leftPower);
        BL.setPower(leftPower);

        FR.setPower(rightPower);
        BR.setPower(rightPower);

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);

    }
}
