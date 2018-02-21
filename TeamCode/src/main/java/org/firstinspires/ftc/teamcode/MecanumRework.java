package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * concept for reworked adv. mecanum drive
 *
 * @author jlemke
 *
 * this opmode is a new take on a lot of our previous ideas.
 * particularly, I wanted to work with understanding <i>magnitude.</i>
 * if we have full control over angle, what's the point of trying to tell
 * the robot to go backwards?
 *
 * l8nite.
 */

@TeleOp(name="Mecanum Rework", group="TeleOp")
public class MecanumRework extends OpMode {
    Hardware750 robot = new Hardware750();
    private ElapsedTime runtime = new ElapsedTime();
    // declare class locals for loop
    double speed = 0;
    double angle = 0;
    double rotate = 0;
    double gp1y;
    double[] voltageMultiplier = {0,0,0,0};
    boolean isGripped = true;
    boolean lastGripState = true;
    double lastRuntime;
    final double GRIPPER_POWER = .5;
    double cooldown = 0;
    boolean clawState = true;

    @Override
    public void init() {
        //robot.arm.setPosition(50);
        telemetry.addData("Status", "Uninitialized...");
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        // invert y axis to make sense. if this causes problems, remove the -1 or refactor
        // to get rid of the variable all together. (replace it with gamepad1.left_stick_y)
        gp1y = (-1 * gamepad1.left_stick_y);
        // calculations bloc
        // speed - while our technical value is within the range of [-1,1], we'll just keep it
        // positive.
        if (gamepad1.right_bumper) {
            speed = -1 * Math.pow(gamepad1.right_trigger, 2);
        } else {
            speed = Math.pow(gamepad1.right_trigger, 2);
        }
        // angle - this is calculated using the direction our stick is pointing. should be in a
        // range of [0,2pi] like a unit circle. terrible things, unit circles.
        // this means the stick held right should be 2pi.
        angle = Math.atan2(gp1y, gamepad1.left_stick_x);
        // corrections to ensure our angle is like a unit circle instead of delivering a coordinate
        if (angle <= 0) {
            angle += Math.PI * 2;
        }
        /*
        if (gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0){
            angle = (Math.PI)/2;
        }
        */

        // rotation magnitude
        if (gamepad1.left_bumper){
            rotate =  Math.pow(gamepad1.left_trigger, 2) * -1;
        } else  {
            rotate = Math.pow(gamepad1.left_trigger, 2);
        }
        // alternative rotation mag.
        if (gamepad1.right_stick_x != 0){
            rotate = gamepad1.right_stick_x;
        }


        //DEBUG: dump outputs
        /*
        telemetry.addData("REQUIRED INFORMATION", "");
        telemetry.addData("speed:", speed);
        telemetry.addData("angle:", angle);
        telemetry.addData("angle (pi):", angle/Math.PI);
        telemetry.addData("rot. magnitude:", rotate);
        */
        // calc voltage multipliers
        if (gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0) {
            speed = 0;
        }
        voltageMultiplier[0] = speed * Math.cos(angle - (Math.PI/4)) + rotate;
        voltageMultiplier[1] = speed * Math.sin(angle - (Math.PI/4)) - rotate;
        voltageMultiplier[2] = speed * Math.sin(angle - (Math.PI/4)) + rotate;
        voltageMultiplier[3] = speed * Math.cos(angle - (Math.PI/4)) - rotate;
        if (rotate == 0) {
            for (int i = 0; i < 4; i++) {
                voltageMultiplier[i] *= Math.sqrt(2);
            }
        }
        //DEBUG: voltage multiplier output
        /*
        telemetry.addData("VOLTAGE MULTIPLIERS (unnormalized)", "");
        telemetry.addData("VM1", voltageMultiplier[0]);
        telemetry.addData("VM2", voltageMultiplier[1]);
        telemetry.addData("VM3", voltageMultiplier[2]);
        telemetry.addData("VM4", voltageMultiplier[3]);
        */
        // begin normalization process
        // store the biggest voltage multiplier
        double topStore = 1;
        for (int x = 0; x < 4; x++){
            if (Math.abs(voltageMultiplier[x]) > topStore) {
                topStore = Math.abs(voltageMultiplier[x]);
            }
        }
        // followed by dividing them all by the top one
        // additionally, do a sanity check that ensures we don't actually do this
        // if our top stored number is "0" because that would make NaN and do bad
        // things ;~;
        for (int x = 0; x < 4; x++) {
            voltageMultiplier[x] /= topStore;
        }
        /*
        telemetry.addData("VOLTAGE MULTIPLIERS (normalized)", "");
        telemetry.addData("VM1", voltageMultiplier[0]);
        telemetry.addData("VM2", voltageMultiplier[1]);
        telemetry.addData("VM3", voltageMultiplier[2]);
        telemetry.addData("VM4", voltageMultiplier[3]);
        */

        if(gamepad1.y){
            robot.arm.setPosition(.78); // outwards
        }
        if(gamepad1.x){
            robot.arm.setPosition(0.16); // back up towards robot
        }

        if(gamepad2.a){
            robot.rotateBlockGrabber(Hardware750.Direction.IN);
        } else if (gamepad2.b){
            robot.rotateBlockGrabber(Hardware750.Direction.OUT);
        } else if(gamepad2.left_bumper){
            robot.halfSpinGrabber(Hardware750.Direction.IN, Hardware750.Side.LEFT);
        } else if (gamepad2.right_bumper){
            robot.halfSpinGrabber(Hardware750.Direction.IN, Hardware750.Side.RIGHT);
        } else {
            robot.rotateBlockGrabber(Hardware750.Direction.STOP);
        }





        //TODO: See if this works
        if (!((gamepad2.left_stick_y < 0) && (robot.lift.getCurrentPosition() > 4000))) {
            robot.lift.setPower(gamepad2.left_stick_y / 2);
        } else {
            robot.lift.setPower(0);
        }



        try {
            if (gamepad2.dpad_left) {
                robot.clawMotor.setPower(0.2);
            } else if (gamepad2.dpad_right) {
                robot.clawMotor.setPower(-0.2);
            } else {
                robot.clawMotor.setPower(0);
            }
        } catch (Exception e) {}

        if(gamepad2.y && runtime.seconds() > cooldown){
            clawState = !clawState;
            cooldown = runtime.seconds() + 1;
        }

        double CLOSE_POS = 0.05;
        double OPEN_POS = 0.8;

        if(clawState){
            robot.thiccClaw1.setPosition(CLOSE_POS);
        } else if (!clawState) {
            robot.thiccClaw1.setPosition(OPEN_POS);
        }

        final double extendoSpeed = 0.5;

        if(gamepad2.dpad_up) {
            robot.armExtender.setPower(extendoSpeed);
        } else if (gamepad2.dpad_down) {
            robot.armExtender.setPower(-extendoSpeed);
        } else {
            robot.armExtender.setPower(0);
        }

        telemetry.addData("ZPB of Lift", robot.lift.getZeroPowerBehavior());
        telemetry.addData("Lift Encoder Value: ", robot.lift.getCurrentPosition());
        telemetry.addData("lift power", robot.lift.getPower());
        telemetry.addData("gp2 y", gamepad2.left_stick_y);
        telemetry.addData("top limiter", robot.limitTop.getState());

        robot.flDrive.setPower(voltageMultiplier[0]);
        robot.frDrive.setPower(voltageMultiplier[1]);
        robot.rlDrive.setPower(voltageMultiplier[2]);
        robot.rrDrive.setPower(voltageMultiplier[3]);


    }

    @Override
    public void stop() {
    }

    private void toggleGrip(){
        isGripped = !isGripped;
    }
}