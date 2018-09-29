package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotutil.Utils;

/**
 * Created by antonlin on 1/13/18.
 */

public class FlipTask extends TaskThread {
    private Servo flipServo;
    double flipDownPos = 1;
    double flipInterPos = .85;
    double flipUpPos = .45;

    int pos = 0;
    ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    public FlipTask(LinearOpMode opMode) {
        this.opMode = opMode;
        initialize();
    }

    @Override
    public void run() {
        timer.reset();
        while (opMode.opModeIsActive() && running) {
            if(opMode.gamepad1.dpad_right || opMode.gamepad2.dpad_right){
                pos--;
            }
            if (opMode.gamepad1.dpad_left || opMode.gamepad2.dpad_left) {
                pos++;
            }
            if(pos == -1){
                pos = 0;
            } else if (pos == 0){
                flipServo.setPosition(flipDownPos);
            } else if(pos == 1){
                flipServo.setPosition(flipInterPos);
            } else if(pos == 2){
                flipServo.setPosition(flipUpPos);
            } else if(pos == 3){
                pos = 2;
            }
            Utils.waitFor(200);
            opMode.telemetry.addData("position: ",pos);
//            opMode.telemetry.update();

        }


    }


    @Override
    public void initialize() {
        flipServo = opMode.hardwareMap.servo.get("flipServo");
        flipServo.setDirection(Servo.Direction.REVERSE);
        flipServo.setPosition(flipDownPos);
    }
}
