package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Mecanum Drive Test", group="TeleOp")
public class ImplMecanumDrive extends OpMode
{
    Hardware750 robot = new Hardware750();
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
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
       telemetry.addData("LeftTrig", gamepad1.left_trigger);
       telemetry.addData("RightTrig", gamepad1.right_trigger);
       telemetry.addData("lstick X", gamepad1.left_stick_x);
       telemetry.addData("lstick Y", (-1 * gamepad1.left_stick_y));
       double presquare = Math.pow(gamepad1.left_stick_x, 2) + Math.pow(gamepad1.left_stick_y, 2);
       telemetry.addData("presquare", presquare);
        double magnitude = ((-1 * gamepad1.left_stick_y) < 0) ? -1*Math.sqrt(presquare) : Math.sqrt(presquare);
        if (magnitude > 1) {
            magnitude = 1;
        }
       telemetry.addData("magnitude", magnitude);
        double angle = Math.atan2((-1*gamepad1.left_stick_y),gamepad1.left_stick_x);
        if (angle < 0) {
            angle += Math.PI*2;
        }
        double triggerVal = (-1 * gamepad1.left_trigger) + gamepad1.right_trigger;
        telemetry.addData("ang le", angle);
        telemetry.addData("number of pi", (angle / Math.PI));
        double extraPi = angle / Math.PI;
        double VM1 = ((magnitude) * (Math.sin(extraPi + (Math.PI / 4))) + triggerVal);
        double VM2 = ((magnitude) * (Math.cos(extraPi + (Math.PI / 4))) - triggerVal);
        double VM3 = ((magnitude) * (Math.cos(extraPi + (Math.PI / 4))) + triggerVal);
        double VM4 = ((magnitude) * (Math.sin(extraPi + (Math.PI / 4))) - triggerVal);
        double [] thing = {1, VM1, VM2, VM3, VM4};
        int idk = 0;
        for (int i = 1; i < 5; i++) {
          if ((Math.abs(thing [i]) > 1) && (Math.abs(thing [i]) > Math.abs(thing [idk]))) {
                idk = i;
          }
        }
        double [] VM20 = new double [4];
        for (int i = 0; i < 4; i++) {
            VM20[i] = 0;
        }
        for (int i = 1; i < 5; i++) {
            VM20[i - 1] = (thing[i]/thing[idk]);
        }
        for (int i = 0; i < 4; i++) {
            if (VM20[i] > 1) {
                VM20[i] = 1;
            } else if (VM20[i] < -1) {
                VM20[i] = -1;
            }
        }
        telemetry.addData("1", thing [1]);
        telemetry.addData("2", thing [2]);
        telemetry.addData("3", thing [3]);
        telemetry.addData("4", thing [4]);
        telemetry.addData("idk", idk);
        telemetry.addData("VM1", VM20 [0]);
        telemetry.addData("VM2", VM20 [1]);
        telemetry.addData("VM3", VM20 [2]);
        telemetry.addData("VM4", VM20 [3]);

        try {
            robot.flDrive.setPower(VM20[0]);
            robot.frDrive.setPower(VM20[1]);
            robot.rlDrive.setPower(VM20[2]);
            robot.rrDrive.setPower(VM20[3]);
        } catch (Exception ex) {
            ex.printStackTrace();
            robot.setAllDriveMotors(0);
        }
    }
    @Override
    public void stop() {
    }
}
