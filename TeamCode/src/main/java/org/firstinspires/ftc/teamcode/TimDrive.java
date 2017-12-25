package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.Date;
import java.util.Hashtable;
import java.util.Enumeration;

//@Disabled
@TeleOp(name="TimDrive", group="Pushbot")
public class TimDrive extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareDRive robot = new HardwareDRive();   // Use a Pushbot's hardware

    private class State extends Hashtable<String, Integer> {
        public String toString() {
            String key;
            String output = "Settings:\n";
            Enumeration<String> keys = keys();

            while (keys.hasMoreElements()) {
                key = keys.nextElement();
                output += key + String.valueOf(get(key));
            }

            return output;
        }

        public void set(String key, int val) {
            put(key, val);
        }
    }

    private class TimeTuple {
        long last = 0;
        int delta = 0;

        public TimeTuple(int timeDelta) {
            delta = timeDelta;
            last = System.currentTimeMillis();
        }

        public TimeTuple(int timeDelta, long lastTime) {
            last = lastTime;
            delta = timeDelta;
        }

        public boolean needsUpdate(int time) {
            return (time > last + delta);
        }

        public boolean needsUpdate() {
            return (System.currentTimeMillis() > last + delta);
        }

        public void markUpdated() {
            last = System.currentTimeMillis();
        }

        public void markUpdated(int time) {
            last = time;
        }

        public long lastTime() {
            return last;
        }

        public int timeDelta() {
            return delta;
        }
    }

    private class Timing extends Hashtable<String, TimeTuple> {
        public String toString() {
            String key;
            String output = "Settings:\n";
            Enumeration<String> keys = keys();

            while (keys.hasMoreElements()) {
                key = keys.nextElement();
                output += key + String.valueOf(get(key));
            }

            return output;
        }

        public void set(String key, TimeTuple val) {
            put(key, val);
        }
    }

    class Toggle {
        static final int
                Inactive = 0,
                Active = 1;
    }

    State settings = new State();
    Timing timings = new Timing();

    double rightCorrection = 0.63;

    public void resetSettings() {
        settings.set("claw", Toggle.Inactive);
        settings.set("speed-modifier", 100);
        settings.set("s-modifier", Toggle.Inactive);
        settings.set("lock-modifier", Toggle.Active);

        timings.set("claw", new TimeTuple(500, 0));
        timings.set("lmod", new TimeTuple(200, 0));
        timings.set("smod", new TimeTuple(100, 0));
    }

    double clawClosedPosition = 0;
    double clawOpenPosition = 0.5;

    public void openClaw() {
        robot.clawleft.setPosition(clawClosedPosition);
        robot.clawright.setPosition(clawOpenPosition);
    }

    public void closeClaw() {
        robot.clawleft.setPosition(clawOpenPosition);
        robot.clawright.setPosition(clawClosedPosition);
    }

    public void moveDirection(double x, double y) {
        robot.BLMotor.setPower(-y);
        robot.FLMotor.setPower(-y);
        robot.FRMotor.setPower(-rightCorrection * y);
        robot.BRMotor.setPower(-rightCorrection * y);

        robot.SideMotor.setPower(x);
    }

    public void moveCardinalDirection(double x, double y) {
        if (Math.abs(x) > Math.abs(y)) {
            moveDirection(x, 0);
        } else {
            moveDirection(0, y);
        }
    }

    public void updateArm() {
        if (gamepad2.right_stick_y != 0) {
            double armPower = gamepad2.right_stick_y * settings.get("arm-modifier") / 100;
            robot.arm.setPower(armPower);
        } else {
            robot.arm.setPower(0);
        }
    }

    public boolean updateMovement() {
        if (gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0) return false;
        if (settings.get("lock-modifier") == Toggle.Inactive) {
            moveDirection(gamepad1.left_stick_x * settings.get("speed-modifier") / 100,
                    gamepad1.left_stick_y * settings.get("speed-modifier") / 100);
        } else {
            moveCardinalDirection(gamepad1.left_stick_x * settings.get("speed-modifier") / 100,
                    gamepad1.left_stick_y * settings.get("speed-modifier") / 100);
        }
        return true;
    }

    public void updateLeftArm() {
        if (gamepad1.a) {
            robot.armleft.setPosition(0);
        }

        if (gamepad1.b) {
            robot.armleft.setPosition(0.75);
        }
    }

    public void updateClaw() {
        if (gamepad2.a && timings.get("claw").needsUpdate()) {
            timings.get("claw").markUpdated();

            if (settings.get("claw") == Toggle.Inactive) {
                openClaw();

                settings.set("claw", Toggle.Active);
            } else {
                closeClaw();

                settings.set("claw", Toggle.Inactive);
            }
        }
    }

    public void updateRotation() {
        rotate(gamepad1.right_stick_x * settings.get("speed-modifier") / 100);
    }

    public void rotate(double magnitude) {
        if (Math.abs(magnitude) > 1) return;

        robot.BLMotor.setPower(-magnitude);
        robot.FLMotor.setPower(-magnitude);
        robot.FRMotor.setPower(magnitude);
        robot.BRMotor.setPower(magnitude);

        robot.SideMotor.setPower(0);
    }

    public void resetEncoders() {
        robot.FLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.BLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.FRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.BRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        idle();

        robot.FLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.BLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.FRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.BRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void initSequence() {
        robot.init(hardwareMap);
        resetEncoders();
        resetSettings();
    }

    public void settingsTelemetry() {
        telemetry.addData("Claw Position: ",
                (settings.get("claw") == Toggle.Inactive) ? "Open" : "Closed");
        telemetry.addData("Cardinal Movement Lock: ",
                ((settings.get("lock-modifier") == Toggle.Inactive) ? "Off" : "On"));
        telemetry.addData("Speed Modifier: ", settings.get("speed-modifier") + "%");
        telemetry.update();
    }

    @Override
    public void runOpMode() {
        initSequence();

        // Wait for driver to press play
        waitForStart();

        boolean lastLeftBumper = false;
        boolean lastRightBumper = false;

        while (opModeIsActive()) {
            if (gamepad1.right_bumper && !lastRightBumper && timings.get("smod").needsUpdate()) {
                timings.get("smod").markUpdated();

                if (settings.get("s-modifier") == Toggle.Inactive) {
                    settings.set("s-modifier", Toggle.Active);
                    settings.set("speed-modifier", 35);
                } else {
                    settings.set("s-modifier", Toggle.Inactive);
                    settings.set("speed-modifier", 100);
                }
            }

            if (gamepad1.left_bumper && !lastLeftBumper && !timings.get("lmod").needsUpdate()) {
                timings.get("lmod").markUpdated();

                if (settings.get("lock-modifier") == Toggle.Inactive) {
                    settings.set("lock-modifier", Toggle.Active);
                } else {
                    settings.set("lock-modifier", Toggle.Inactive);
                }
            }

            if (gamepad2.right_bumper) {
                settings.set("arm-modifier", 5);
            } else {
                settings.set("arm-modifier", 25);
            }

            updateArm();

            if (!updateMovement()) {
                updateRotation();
            }

            updateClaw();
            updateLeftArm();

            settingsTelemetry();
            lastLeftBumper = gamepad1.left_bumper;
            lastRightBumper = gamepad1.right_bumper;
        }
    }
}
