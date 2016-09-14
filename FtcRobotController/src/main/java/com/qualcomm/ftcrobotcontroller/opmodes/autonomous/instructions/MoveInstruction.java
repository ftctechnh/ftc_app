package com.qualcomm.ftcrobotcontroller.opmodes.autonomous.instructions;

import com.qualcomm.ftcrobotcontroller.opmodes.smidautils.Instruction;
import com.qualcomm.ftcrobotcontroller.opmodes.smidautils.OmniMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Arrays;

/**
 * Created by Nathan.Smith.19 on 9/13/2016.
 */
public class MoveInstruction extends Instruction {

    public MoveInstruction(double x_power, double y_power, Double seconds, OmniMotor[] motors) {
        super("MOVE", x_power, y_power, seconds, motors);
    }

    @Override
    public void execute(OpMode robot) {
        double x_power = (Double) getData().get(0);
        double y_power = (Double) getData().get(1);
        double seconds = (Double) getData().get(2);
        OmniMotor[] motors = (OmniMotor[]) getData().get(3);

        for(OmniMotor x : motors){
            switch(x.getAxis()){
                case X:
                    x.getMotor().setPower(x_power);
                    break;
                case Y:
                    x.getMotor().setPower(y_power);
                    break;
            }
        }


    }
}
