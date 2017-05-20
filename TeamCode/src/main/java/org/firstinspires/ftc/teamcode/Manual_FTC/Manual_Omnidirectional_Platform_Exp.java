package org.firstinspires.ftc.teamcode.Manual_FTC;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Libs.Core_Omnidirectional_Platform_Exp;

@TeleOp(name = "Omnidirectional Manual Test",group = "Omnidirectional Platform")
public final class Manual_Omnidirectional_Platform_Exp extends Core_Omnidirectional_Platform_Exp {

    @Override
    public void runOpMode()throws InterruptedException,NullPointerException {
        robot.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            keyToDirection();
            if(!isAnyKedDown()) {
                setBasePower(0, 0, 0, 0);
            }
        }
    }
}
