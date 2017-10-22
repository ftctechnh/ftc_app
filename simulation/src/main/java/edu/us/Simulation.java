package edu.us;

import com.borsch.OpModeSimulation;
import com.borsch.sim.RobotConfiguration;
import com.borsch.sim.SimulatedVuforiaTrackable;
import com.borsch.sim.VuforiaConfiguration;

public class Simulation {

    public static void main (String[] args) {
        System.out.println("Starting simulation...");

        RobotConfiguration robot = new RobotConfiguration();
        robot.addDcMotor("fr");
        robot.addDcMotor("fl");
        robot.addDcMotor("br");
        robot.addDcMotor("bl");
        robot.addDcMotor("harvester");
        robot.addDcMotor("lft");
        robot.addDcMotor("sr");
        robot.addDcMotor("sl");
        robot.addServo("ls");
        robot.addServo("los");
        robot.addColorSensor("cs");
        robot.addColorSensor("brcs");
        robot.addColorSensor("blcs");
        robot.addDeviceInterfaceModule("dim");

        VuforiaConfiguration vuforiaConfig = new VuforiaConfiguration();
        vuforiaConfig.addAsset("FTC2017-18", new SimulatedVuforiaTrackable(null, null, null));

        OpModeSimulation sim = new OpModeSimulation("edu.usrobotics.opmode.mecanumbot.MecanumBotAutoBlue", robot);
        sim.init ();
        sim.start ();

        System.out.println("Simulation ended.");
        System.exit(0);
    }
}
