package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import static org.firstinspires.ftc.teamcode.Utils.getBatteryVoltage;

@TeleOp(name="Sbev's first steps", group="Iterative Opmode")
public class OpModeFirst extends OpMode {
    //time
    private ElapsedTime runtime = new ElapsedTime();

    // Motor variables
    private DcMotor FL = null;
    private DcMotor FR = null;
    private DcMotor BL = null;
    private DcMotor BR = null;
    //------------------------------------------// claw variables:
    static final double INCREMENT = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int CYCLE_MS = 50;     // period of each cycle
    static final double MAX_POS = 1.0;     // Maximum rotational position
    static final double MIN_POS = 0.0;     // Minimum rotational position
    private double leftPower;
    private double rightPower;
    private double clawPosition;
    private Servo claw = null;

    @Override
    public void init() {
        //telemetry
        telemetry.addData("Status", "Initialized");

        //getting all the motors
        FL = hardwareMap.get(DcMotor.class, "fl");
        FR = hardwareMap.get(DcMotor.class, "fr");
        BL = hardwareMap.get(DcMotor.class, "bl");
        BR = hardwareMap.get(DcMotor.class, "br");
        claw = hardwareMap.get(Servo.class, "claw");

        //accounting for how the motors are mounted
        FL.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.REVERSE);
        FR.setDirection(DcMotor.Direction.FORWARD);
        BR.setDirection(DcMotor.Direction.FORWARD);

    }
    @Override
    public void start(){
        runtime.reset();
        claw.setPosition(0.50);

    }

    @Override
    public void loop() {
        //these variables store power for left wheels and right wheels

        setDrivePower();
        setClawPosition();

        FL.setPower(leftPower);
        BL.setPower(leftPower);
        FR.setPower(rightPower);
        BR.setPower(rightPower);
        claw.setPosition(clawPosition);

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);

    }

    void setDrivePower() {

        if (Utils.getBatteryVoltage(hardwareMap) < 12.0d) {
            telemetry.addLine("Warning! Battery voltage is low");
        }
        telemetry.addData("Battery", "Voltage: %.2f", getBatteryVoltage(hardwareMap));


        //stores gamepad sticks in variables
        double drive = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;

        //sets left and right power
        leftPower = Range.clip(drive + turn, -1.0, 1.0);
        rightPower = Range.clip(drive - turn, -1.0, 1.0);

        //Precision Mode
        boolean precisionMode = gamepad1.left_bumper;
        if (precisionMode) {  //if precision mode is on, left power and right power are set to 10% of what they would otherwise be.
            leftPower *= 0.1;
            rightPower *= 0.1;
        }


    }

    void setClawPosition() {
        double position = (-gamepad2.right_stick_y + 1) / 2;
        telemetry.addData("Servo", "position: %.2f", position);
        clawPosition = position;

    }


}
