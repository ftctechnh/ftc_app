package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.modules.GamepadV2;
import org.firstinspires.ftc.teamcode.modules.MecanumDrive;
import org.firstinspires.ftc.teamcode.modules.State;
import org.firstinspires.ftc.teamcode.modules.StateMachine;

@TeleOp
public class TeleOpVortex extends OpMode {
    private HardwareVortex robot = new HardwareVortex();

    private GamepadV2 pad2 = new GamepadV2(), pad1 = new GamepadV2();

    private StateMachine setupLift;

    private boolean override = false;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void start() {
        setupLift = new StateMachine(
                new State("stop") {
                    @Override
                    public void run() {
                        if(!override) {
                            robot.leftLift.setPosition(HardwareVortex.LEFT_LIFT_INIT);
                            robot.rightLift.setPosition(HardwareVortex.RIGHT_LIFT_INIT);
                        }
                    }
                },
                new State("flipper") {
                    @Override
                    public void run() {
                        sendData("startTime", time);
                        robot.flipper.setPower(-1);
                        changeState("hooks");
                    }
                },
                new State("hooks") {
                    @Override
                    public void run() {
                        double elapsedTime = time - getDouble("startTime");
                        sendData("elapsedTime", elapsedTime);
                        if (elapsedTime > 0.75) {
                            robot.flipper.setPower(0.0);
                            sendData("startTime2", time);
                            if(override) {
                                changeState("stop");
                            }
                            else {
                                robot.leftLift.setPosition(HardwareVortex.LEFT_LIFT_PUSH);
                                robot.rightLift.setPosition(HardwareVortex.RIGHT_LIFT_PUSH);
                                changeState("cleanup");
                            }
                        }
                    }
                },
                new State("cleanup") {
                    @Override
                    public void run() {
                        double elapsedTime = time - getDouble("startTime2");
                        if (elapsedTime > 1.0) {
                            changeState("stop");
                        }
                    }
                }
        ).start();
    }

    @Override
    public void stop() {
        setupLift.stop();
    }

    @Override
    public void loop() {
        pad2.update(gamepad2);
        pad1.update(gamepad1);

        double left = pad2.left_stick_y_exponential(1);
        double right = pad2.right_stick_y_exponential(1);

        robot.frontLeft.setPower(left);
        robot.backLeft.setPower(left);
        robot.frontRight.setPower(right);
        robot.backRight.setPower(right);

        if(pad2.a) {
            override = true;
            robot.leftLift.setPosition(HardwareVortex.LEFT_LIFT_PUSH);
            robot.rightLift.setPosition(HardwareVortex.RIGHT_LIFT_PUSH);
        }
        else if(pad2.y) {
            override = true;
            robot.leftLift.setPosition(HardwareVortex.LEFT_LIFT_INIT);
            robot.rightLift.setPosition(HardwareVortex.RIGHT_LIFT_INIT);
        }

        if(pad2.x_isReleased(true)) {
            setupLift.changeState("flipper");
        }

        if(pad2.right_trigger()) {
            robot.flipper.setPower(1);
        }
        else if(pad2.left_trigger()) {
            robot.flipper.setPower(-1);
        }
        else if(setupLift.getActiveState().equals("stop")) {
            robot.flipper.setPower(0);
        }

        if(pad2.right_bumper || pad1.y) {
            robot.lift.setPower(1);
        }
        else if(pad2.left_bumper || pad1.a) {
            robot.lift.setPower(-1);
        }
        else {
            robot.lift.setPower(0);
        }

        if(pad1.dpad_left) {
            robot.pusherLeft.setPosition(1);
        }
        else if(pad1.dpad_right) {
            robot.pusherLeft.setPosition(0);
        }
        else if(pad1.dpad_up) {
            robot.pusherRight.setPosition(1);
        }
        else if(pad1.dpad_down) {
            robot.pusherRight.setPosition(0);
        }
        else {
            robot.pusherLeft.setPosition(0.5);
            robot.pusherRight.setPosition(0.5);
        }

        if(pad1.b) {
            robot.shooter.setPower(HardwareVortex.SHOOTER_POWER);
        }
        else if(pad1.x) {
            robot.shooter.setPower(-HardwareVortex.SHOOTER_POWER);
        }
        else {
            robot.shooter.setPower(0);
        }

        if(pad1.right_bumper) {
            robot.intake.setPower(0.75);
        }
        else if(pad1.left_bumper) {
            robot.intake.setPower(-0.50);
        }
        else {
            robot.intake.setPower(0);
        }

        robot.generateTelemetry(telemetry, true);
        telemetry.addData("SetupState", setupLift.getActiveState());
        Double startTime = setupLift.getDouble("startTime");
        telemetry.addLine().addData("STARTTIME", startTime).addData("TIME", time - startTime).addData("T", setupLift.getDouble("elapsedTime"));
    }
}
