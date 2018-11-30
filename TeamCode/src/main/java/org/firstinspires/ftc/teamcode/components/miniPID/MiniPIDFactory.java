package org.firstinspires.ftc.teamcode.components.miniPID;

import org.firstinspires.ftc.teamcode.components.configs.IConfig;

public class MiniPIDFactory {
    /**
     * Returns a new Miniature PID controller given a configuration file
     * @param config The configuration the miniature PID
     * @return Returns a new Miniature PID controller
     */
    public static MiniPID getMiniPIDFromConfig(IConfig config) {
        MiniPID miniPID = new MiniPID(config.getDouble("P"), config.getDouble("I"), config.getDouble("D"));
        miniPID.setOutputLimits(config.getDouble("OutputLimits"));
        return miniPID;
    }
}