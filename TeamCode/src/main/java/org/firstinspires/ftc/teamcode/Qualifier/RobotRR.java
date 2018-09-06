package org.firstinspires.ftc.teamcode.Qualifier;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class RobotRR {
    DriveTrain driveTrain;
    JewelArm jewelArm;
    RelicArm relicArm;
    GlyphTrain glyphTrain;
    //GlyphKicker2 glyph_kicker2;


    //Shooter shooter;
    //Loader loader;
    //Sweeper sweeper;
    //BeaconColorDetector beaconColorDetector;
    //LineDetector lineDetector;

    public RobotRR()
    {
        driveTrain = new DriveTrain();
        relicArm = new RelicArm();
        jewelArm = new JewelArm();
        glyphTrain = new GlyphTrain();

        //shooter = new Shooter();
        //beaconColorDetector = new BeaconColorDetector();
        //loader = new Loader();
        //sweeper = new Sweeper();
        //lineDetector = new LineDetector();
    }

    public void init (HardwareMap hardwareMap)
    {
        driveTrain.init(hardwareMap);
        jewelArm.init(hardwareMap);
        relicArm.init(hardwareMap);
        glyphTrain.init(hardwareMap);
        //shooter.init(hardwareMap);
        //beaconColorDetector.init(hardwareMap);
        //loader.init(hardwareMap);
        //sweeper.init(hardwareMap);
        //lineDetector.init(hardwareMap);
    }
}
