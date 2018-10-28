package org.firstinspires.ftc.teamcode.Utilities.Control;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;

public class FeedbackController {
    public LynxModule leftHub;
    public LynxModule rightHub;
    public LynxGetBulkInputDataResponse leftData;
    public LynxGetBulkInputDataResponse rightData;

    private int backRightAddr    = 0;
    private int winchAddr        = 1;
    private int frontRightAddr   = 2;
    private int flipperRightAddr = 3;
    private int flipperLeftAddr  = 4;
    private int frontLeftAddr    = 5;
    private int extenderAddr     = 6;
    private int backLeftAddr     = 7;

    private static final String[] prettyNames = {
            "Back right wheel",
            "Winch",
            "Front right wheel",
            "Flipper right",
            "Flipper left",
            "Front left wheel",
            "Extender",
            "Back left wheel"
    };

    public FeedbackController (LynxModule leftHub, LynxModule rightHub) {
        leftHub = leftHub;
        rightHub = rightHub;
    }

    public void update() {
        LynxGetBulkInputDataCommand leftCommand = new LynxGetBulkInputDataCommand(leftHub);
        LynxGetBulkInputDataCommand rightCommand = new LynxGetBulkInputDataCommand(rightHub);
        try {
            leftData = leftCommand.sendReceive();
            rightData = rightCommand.sendReceive();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (LynxNackException e) {
            e.printStackTrace();
        }
    }

    private LynxGetBulkInputDataResponse getResp(int addr) {
        return (addr > 3) ? leftData : rightData;
    }

    private int getEncoderPosFromAddr(int addr) {
        return getResp(addr).getEncoder(addr % 4);
    }

    private int getMotorPowerFromAddr(int addr) {
        return getResp(addr).getVelocity(addr % 4);
    }

    // We still need to call update
    public void updateTelemetry(Telemetry telemetry) {
        for (int i = 0; i < 8; i++) {
            double velo = Math.round(getMotorPowerFromAddr(i)* 100) / 100.0;
            int ticks = getEncoderPosFromAddr(i);
            telemetry.addData(prettyNames[i], velo + " - " + ticks);
        }
    }
}
