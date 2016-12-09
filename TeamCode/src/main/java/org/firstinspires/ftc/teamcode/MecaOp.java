package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.modules.GamepadV2;
import org.firstinspires.ftc.teamcode.modules.StateMachine;
import org.firstinspires.ftc.teamcode.modules.State;

public abstract class MecaOp extends OpMode {
    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private DcMotor intake, shoot, lift, flipper;

    private StateMachine liftSM, shootSM;

    @Override
    public void init() {
        // Initialize the motors
        frontLeft = hardwareMap.dcMotor.get("FL");
        frontRight = hardwareMap.dcMotor.get("FR");
        backLeft = hardwareMap.dcMotor.get("BL");
        backRight = hardwareMap.dcMotor.get("BR");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        intake = hardwareMap.dcMotor.get("intake");
        shoot = hardwareMap.dcMotor.get("shoot");
        lift = hardwareMap.dcMotor.get("lift");
        flipper = hardwareMap.dcMotor.get("flipper");

        shoot.setDirection(DcMotorSimple.Direction.REVERSE);

        // Initialize the state machines
        liftSM = new StateMachine(
                new State("stop") {
                    @Override
                    public void run() {
                        lift.setPower(0);
                    }
                },
                new State("down") {
                    @Override
                    public void run() {
                        lift.setPower(-1.00);
                        // TODO: Implement limit switches to stop lift
                        this.changeState("stop");
                    }
                },
                new State("up") {
                    @Override
                    public void run() {
                        lift.setPower(1.00);
                        // TODO: Implement encoder to stop lift after it reaches its maximum
                        this.changeState("stop");
                    }
                }
        );
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
