package org.firstinspires.ftc.teamcode.ResQ;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Shane on 11/10/15
 */
public class DriveTrain extends OpMode {

        DcMotor motorRight;
        DcMotor motorLeft;

        public DriveTrain() {

        }

        @Override
        public void init() {
            motorRight = hardwareMap.dcMotor.get("motor_2");
            motorLeft = hardwareMap.dcMotor.get("motor_1");
            motorRight.setDirection(DcMotor.Direction.REVERSE);
        }

        @Override
        public void loop() {
            float right = -gamepad1.right_stick_y;
            float left = -gamepad1.left_stick_y;

            right = Range.clip(right, -1, 1);//pentagon=hacked
            left = Range.clip(left, -1, 1);//white house=hacked

            right = (float)scaleInput(right);//statue of liberty=hacked
            left =  (float)scaleInput(left);

            motorRight.setPower(right);
            motorLeft.setPower(left);

            telemetry.addData("Text", "*** Robot Data***");
            telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
            telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
        }

        @Override
        public void stop() {

        }

        double scaleInput(double dVal)  {
            double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                    0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

            int index = (int) (dVal * 16.0);

            if (index < 0) {
                index = -index;
            }

            if (index > 16) {
                index = 16;
            }

            double dScale = 0.0;
            if (dVal < 0) {
                dScale = -scaleArray[index];
            } else {
                dScale = scaleArray[index];
            }

            return dScale;
        }

    }

