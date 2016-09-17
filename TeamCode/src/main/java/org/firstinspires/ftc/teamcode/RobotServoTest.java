package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by djfigs1 on 12/20/15.
 */
public class RobotServoTest extends SimpleDrive {
    @Override
    public void loop() {
        if (gamepad2.dpad_up){
            arm_1_position(get_arm_1_position() + 0.01);
        }else if (gamepad2.dpad_down){
            arm_1_position(get_arm_1_position() - 0.01);
        }
        if (gamepad2.dpad_left) {
            arm_2_position(get_arm_2_position() + 0.01);
        } else if (gamepad2.dpad_right) {
            arm_2_position(get_arm_2_position() - 0.01);
        }
        if (gamepad2.x) {
            arm_3_position(get_arm_3_position() + 0.05);
        } else if (gamepad2.b) {
            arm_3_position(get_arm_3_position() - 0.05);
        }

    }
}

