package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
/**
 * This will be our Autonomous and our first try at a state machine (comment one)
 * Created by Joseph Liang on 10/30/2017.
 */

@Autonomous(name="Relic Recovery: State Machine", group="Pushbot")
public class RlcRcvryAutoSwitch {

    private int stateMachineFlow;
    HardwarePushbot robot       = new HardwarePushbot();

    @Override
    public void runOpMode(){
        robot.init(hardwareMap);
    }
    @Override
    public void init_loop(){

        stateMachineFlow = 0;
    }
    @Override
    public void start(){

    }
    @Override
    public void loop() {
        switch(stateMachineFlow){
            case 0:
                stateMachineFlow++;
                break;
            case 1:
                stateMachineFlow++;
                break;
        }
    }

}
