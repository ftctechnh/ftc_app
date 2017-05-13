package org.firstinspires.ftc.teamcode.Plan_For_Manual;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Libs.Core_Traditional_Platform;
import org.firstinspires.ftc.teamcode.Libs.Enum_Libs;

@TeleOp(name = "Manual Practise", group = "4Tune.Practise")
public final class Manual_Practise extends Core_Traditional_Platform {

    @Override
    public void runOpMode() {
        initRobot(Enum_Libs.TeamColor.Red, Enum_Libs.OpMode.Manual);
        while (opModeIsActive()) {
            //  举大球电机 功率[ -0.2，0.8 ]
            liftBall(-0.20,0.60);
            //  机器主要运动 功率[ -0.5,0.8 ]
            controlByJoystick(0.80, -0.50);
            manualClickLight();
            manualShootBall(0.80,0.30);
            collectBall(1.00,0.40);
            ballClawRelease();
            //  检测一次有效性即可，此处将使有效性检测跳过
            setChecked();
        }
        runtime.reset();
    }
}
