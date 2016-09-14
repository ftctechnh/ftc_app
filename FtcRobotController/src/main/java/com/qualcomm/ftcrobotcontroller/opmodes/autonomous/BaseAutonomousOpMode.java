package com.qualcomm.ftcrobotcontroller.opmodes.autonomous;

import com.qualcomm.ftcrobotcontroller.opmodes.autonomous.instructions.MoveInstruction;
import com.qualcomm.ftcrobotcontroller.opmodes.smidautils.Instruction;
import com.qualcomm.ftcrobotcontroller.opmodes.smidautils.InstructionSet;
import com.qualcomm.ftcrobotcontroller.opmodes.smidautils.OmniMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Nathan.Smith.19 on 9/14/2016.
 */
public class BaseAutonomousOpMode extends LinearOpMode {

    protected InstructionSet base_set;
    protected String[] drive_motors;

    @Override
    public void runOpMode() throws InterruptedException {

        final OmniMotor[] motors = {
                new OmniMotor(OmniMotor.Axis.X,"wheel_n",this),
                new OmniMotor(OmniMotor.Axis.X,"wheel_s",this),
                new OmniMotor(OmniMotor.Axis.Y,"wheel_e",this),
                new OmniMotor(OmniMotor.Axis.Y,"wheel_w",this)
        };

        base_set = new InstructionSet();

        base_set.addInstruction(new MoveInstruction(1D,0D,10L,motors));
    }
}
