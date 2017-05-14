package org.firstinspires.ftc.teamcode.Auto_FTC;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Libs.Enum_Libs;
import org.firstinspires.ftc.teamcode.Libs.Core_Traditional_Platform;

@Disabled
@Autonomous (name = "Plan A",group = "4Tune Blue")
public final class Blue_Plan_A extends Core_Traditional_Platform {

    @Override
    public void runOpMode(){
        initRobot(Enum_Libs.TeamColor.Blue, Enum_Libs.OpMode.Autonomous);
        runByDistance(100,1.00, Enum_Libs.RunDirection.Forward);
        turnByDegree(45,0.50, Enum_Libs.RunDirection.Right);
        runByDistance(30,1.00, Enum_Libs.RunDirection.Forward);
        turnByDegree(45,0.50, Enum_Libs.RunDirection.Left);
        clickLight(Enum_Libs.LightColor.Blue);
    }

    public String returnWhatToDo(){
        return "转向按灯\n"+"预计30分";
    }
}
