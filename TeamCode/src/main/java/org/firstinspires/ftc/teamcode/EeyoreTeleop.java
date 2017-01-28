package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.EeyoreHardware;

@TeleOp(name="Eeyore TeleOp", group="Iterative Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class EeyoreTeleop extends OpMode {
    EeyoreHardware robot = new EeyoreHardware();

    int reverseDirection = 1; // If set to 1, drive forward. -1, drive backwards

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double left;
        double right;

        double speed = -gamepad1.left_stick_y; // How fast we want the robot to move
        double direction = gamepad1.left_stick_x; // how much we want the robot to turn
        double maxSpeed; // This multiplier determines how fast we allow the robot to move for this loop

        if(Math.abs(direction) < 0.2) {
            maxSpeed = 0.6;
        } else if(Math.abs(direction) > 0.85) {
            maxSpeed = 0;
        } else {
            maxSpeed = 1;
        }

        if(gamepad1.dpad_left) {
            reverseDirection = -1;
        } if(gamepad1.dpad_right) {
            reverseDirection = 1;
        }


        // Now, we need to work out how much power each motor gets
        left = Range.clip((maxSpeed * speed * reverseDirection) + direction, -1, 1);
        right = Range.clip((maxSpeed * speed * reverseDirection) - direction, -1, 1);

        robot.l1.setPower(left);
        robot.l2.setPower(left);
        robot.r1.setPower(right);
        robot.r2.setPower(right);

        // Collection control
        if(gamepad1.a) {
            if(robot.collection.getPower() == 0) {
                robot.collection.setPower(1);
                try { // Not sure this portion will work, we need to test it and find out if it's accurate
                    Thread.sleep(150);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } else {
                robot.collection.setPower(0);
                try { // Not sure this portion will work, we need to test it and find out if it's accurate
                    Thread.sleep(150);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }

        } else if(gamepad1.y) {
            if(robot.collection.getPower() == 0) {
                robot.collection.setPower(-1);
                try { // Not sure this portion will work, we need to test it and find out if it's accurate
                    Thread.sleep(150);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } else {
                robot.collection.setPower(0);
                try { // Not sure this portion will work, we need to test it and find out if it's accurate
                    Thread.sleep(150);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        // Shooter control
        if(gamepad1.x) {
            if(robot.shooter1.getPower() == 0) {
                robot.shooter1.setPower(0.75);
                robot.shooter2.setPower(0.75);
                try { // Not sure this portion will work, we need to test it and find out if it's accurate
                    Thread.sleep(150);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } else {
                robot.shooter1.setPower(0);
                robot.shooter2.setPower(0);
                try { // Not sure this portion will work, we need to test it and find out if it's accurate
                    Thread.sleep(150);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        } else if(gamepad1.b) {
            if(robot.shooter1.getPower() == 0) {
                robot.shooter1.setPower(-0.75);
                robot.shooter2.setPower(-0.75);
                try { // Not sure this portion will work, we need to test it and find out if it's accurate
                    Thread.sleep(150);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

            } else {
                robot.shooter1.setPower(0);
                robot.shooter2.setPower(0);
                try { // Not sure this portion will work, we need to test it and find out if it's accurate
                    Thread.sleep(150);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        // Send telemetry message to signify robot running;
        telemetry.addData("Left Power", "%.2f", left);
        telemetry.addData("Right Power", "%.2f", right);
        telemetry.addData("Sensor Color", robot.color.alpha());
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}