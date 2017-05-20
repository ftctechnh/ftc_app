package org.firstinspires.ftc.teamcode.Libs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardWareMaps.Hardware_Omnidirectional_Platform;

public abstract class Core_Omnidirectional_Platform extends LinearOpMode {

    protected Hardware_Omnidirectional_Platform robot = new Hardware_Omnidirectional_Platform();
    protected ElapsedTime runtime = new ElapsedTime();

    private boolean []isOutput = {false,false,false,false,false,false,false,false};
    private boolean []currentState = {false,false,false,false,false,false,false,false};
    // ------------------------------------通用-----------------------------------------------
    public void initRobot(Enum_Libs.TeamColor teamColor,Enum_Libs.OpMode opMode){
        robot.init(hardwareMap);
        telemetry.addLine(goal());
        displayTeamColor(teamColor);
        initPins();
        if(opMode == Enum_Libs.OpMode.Autonomous){

        }else if(opMode == Enum_Libs.OpMode.Manual){

        }
        telemetry.update();
    }

    protected abstract String goal();

    private void initPins(){
        int index = 0;
        do{
            if(isOutput[index]){
                robot.InterfaceModule.setDigitalChannelMode(index, DigitalChannel.Mode.OUTPUT);
            }else {
                robot.InterfaceModule.setDigitalChannelMode(index, DigitalChannel.Mode.INPUT);
            }
        }while (index++ < 7);
    }

    private void setPinState(int Channel,boolean State){
        currentState[Channel] = State;
        robot.InterfaceModule.setDigitalChannelState(Channel,currentState[Channel]);
    }

    private void displayTeamColor(Enum_Libs.TeamColor teamColor){
        switch (teamColor){
            case Blue:
                robot.InterfaceModule.setLED(0,false);
                robot.InterfaceModule.setLED(1,true);
                break;
            case Red:
                robot.InterfaceModule.setLED(0,true);
                robot.InterfaceModule.setLED(1,false);
                break;
            default:
                robot.InterfaceModule.setLED(0,false);
                robot.InterfaceModule.setLED(1,false);
        }
    }

    protected boolean isAnyKeyDown(){
        return gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_up || gamepad1.dpad_right ||
                gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y ||
                gamepad1.left_bumper || gamepad1.right_bumper ||
                gamepad1.left_stick_button || gamepad1.right_stick_button ||
                gamepad2.dpad_down || gamepad2.dpad_left || gamepad2.dpad_up || gamepad2.dpad_right ||
                gamepad2.a || gamepad2.b || gamepad2.x || gamepad2.y ||
                gamepad2.left_bumper || gamepad2.right_bumper ||
                gamepad2.left_stick_button || gamepad2.right_stick_button;
    }

    protected boolean isAnyKeyDownOnPadA(){
        return gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_up || gamepad1.dpad_right ||
                gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y ||
                gamepad1.left_bumper || gamepad1.right_bumper ||
                gamepad1.left_stick_button || gamepad1.right_stick_button;
    }

    protected boolean isAnyKeyDownOnPadB(){
        return gamepad2.dpad_down || gamepad2.dpad_left || gamepad2.dpad_up || gamepad2.dpad_right ||
                gamepad2.a || gamepad2.b || gamepad2.x || gamepad2.y ||
                gamepad2.left_bumper || gamepad2.right_bumper ||
                gamepad2.left_stick_button || gamepad2.right_stick_button;
    }
    // ----------------------------------自动阶段--------------------------------------------------

    // ----------------------------------手动阶段--------------------------------------------------

    //---------------------------------------------------------------------------------------------
}
