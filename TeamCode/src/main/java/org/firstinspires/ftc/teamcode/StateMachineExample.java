package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.modules.State;
import org.firstinspires.ftc.teamcode.modules.StateMachine;

public class StateMachineExample extends OpMode {
    private DcMotor leftDrive, rightDrive;
    private DcMotor lift;

    private TouchSensor liftLimit;

    private StateMachine driveSM;
    private long driveTarget;

    private StateMachine liftSM;
    private long liftTarget;

    private StateMachine autonomous;

    @Override
    public void init() {
        //Initialize motors
        leftDrive = hardwareMap.dcMotor.get("leftDrive");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");

        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        lift = hardwareMap.dcMotor.get("lift");

        //Initialize sensors
        liftLimit = hardwareMap.touchSensor.get("liftLimit");

        //Initialize state machines
        driveSM = new StateMachine(
                new State("stop") {
                    @Override
                    public void run() {
                        leftDrive.setPower(0.00);
                        rightDrive.setPower(0.00);
                    }
                },
                new State("driveToPos") {
                    @Override
                    public void run() {
                        if (leftDrive.getCurrentPosition() < driveTarget && rightDrive.getCurrentPosition() < driveTarget) {
                            leftDrive.setPower(1.00);
                            rightDrive.setPower(1.00);
                        } else {
                            changeState("stop");
                        }
                    }
                }
        );
        liftSM = new StateMachine(
                new State("stop") {
                    @Override
                    public void run() {
                        lift.setPower(0.00);
                    }
                },
                new State("up") {
                    @Override
                    public void run() {
                        if (lift.getCurrentPosition() < liftTarget) {
                            lift.setPower(1.00);
                        } else {
                            changeState("stop");
                        }
                    }
                },
                new State("down") {
                    @Override
                    public void run() {
                        if (liftLimit.isPressed() == false) {
                            lift.setPower(-0.50);
                        }
                        else {
                            changeState("stop");
                        }
                    }
                }
        );

        autonomous = new StateMachine(
                new State("initial") {
                    @Override
                    public void run() {
                        liftTarget = 3000;
                        liftSM.changeState("up");

                        driveTarget = 2500;
                        driveSM.changeState("driveToPos");

                        if (driveSM.getActiveState().equals("stop")) { //Drive train finished its task?
                            changeState("final");
                        }
                    }
                },
                new State("final") {
                    @Override
                    public void run() {
                        liftSM.changeState("down");
                    }
                }
        );
    }

    @Override
    public void start() {
        liftSM.start();
        driveSM.start();
        autonomous.start();
    }

    @Override
    public void stop() {
        liftSM.stop();
        driveSM.stop();
        autonomous.stop();
    }

    @Override
    public void loop() {

    }
}
