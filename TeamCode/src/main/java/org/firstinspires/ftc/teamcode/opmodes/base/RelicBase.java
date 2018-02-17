package org.firstinspires.ftc.teamcode.opmodes.base;

import android.content.SharedPreferences;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.phone.PrefLoader;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.general.Updatable;
import org.firstinspires.ftc.teamcode.control.ControllerWrapper;
import org.firstinspires.ftc.teamcode.robot.peripherals.locomotion.Meccanum;
import org.firstinspires.ftc.teamcode.robot.peripherals.Peripheral;
import org.firstinspires.ftc.teamcode.robot.peripherals.gripper.SingleServoGripper;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

/**
 * Created by Derek on 2/6/2018.
 *
 * The base op mode for all other Relic Recovery OpModes to extend
 */

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public class RelicBase extends OpMode {

    private static final String PREFS_NAME = "RelicOpPrefs";
    private PrefLoader prefLoader;

    protected ControllerWrapper wrapper1,wrapper2;

    //Hardware Devices
    protected DcMotor arm;
    private   DcMotor a,b,c,d;
    protected Servo   gripperServo,colorServo;
    protected ColorSensor colorSensor;

    //peripherals
    protected SingleServoGripper gripper;
    protected Meccanum drivetrain;

    protected SharedPreferences prefs;
    protected String[] tuneArr;

    //Tune-able Coefficients for general movement
    protected double clawIncrement;
    protected double straightFactor;
    protected double turnFactor;
    protected double strafeFactor;
    protected double boomUpFactor;
    protected double boomDownFactor;

    //lists for updating
    private List<Peripheral> peripheralList = new LinkedList<>();
    private List<Updatable>  updatableList  = new LinkedList<>();

    @Override
    public void init() {

        //self initialization
        prefs = hardwareMap.appContext.getSharedPreferences(PREFS_NAME,0);
        String[] prefArr = hardwareMap.appContext.getResources().getStringArray(R.array.PREFS);

        prefLoader = new PrefLoader(prefs,prefArr);

        Map<String, Double> tuneMap = prefLoader.loadAll().getTunemap();
        tuneArr      = tuneMap.keySet().toArray(new String[0]);

        //Initialization of tune coefficients
        //todo: replace 1.337 with calibrated defaults
        clawIncrement  = Double.parseDouble(prefs.getString("clawIncrement"     ,"1.337"));
        straightFactor = Double.parseDouble(prefs.getString("straightMultiplier","1.337"));
        turnFactor     = Double.parseDouble(prefs.getString("turnMultiplier"    ,"1.337"));
        strafeFactor   = Double.parseDouble(prefs.getString("strafeMultiplier"  ,"1.337"));
        boomDownFactor = Double.parseDouble(prefs.getString("boomDownMultiplier","1.337"));
        boomUpFactor   = Double.parseDouble(prefs.getString("boomUpMultiplier"  ,"1.337"));

        //Initialization of Controller wrappers
        wrapper1 = new ControllerWrapper(gamepad1);
        wrapper1.update();
        updatableList.add(wrapper1);

        wrapper2 = new ControllerWrapper(gamepad2);
        wrapper2.update();
        updatableList.add(wrapper2);

        //Robot Initialization
        b = hardwareMap.dcMotor.get("frontLeft");
        c = hardwareMap.dcMotor.get("frontRight");
        a = hardwareMap.dcMotor.get("backLeft");
        d = hardwareMap.dcMotor.get("backRight");

        arm          = hardwareMap.dcMotor    .get("arm");
        gripperServo = hardwareMap.servo      .get("claw");
        colorServo   = hardwareMap.servo      .get("colorServo");
        colorSensor  = hardwareMap.colorSensor.get("color");

        //hardware configuration
        c.setDirection(REVERSE);
        d.setDirection(REVERSE);
        arm.setDirection(REVERSE);
        arm.setZeroPowerBehavior(BRAKE);

        gripper = new SingleServoGripper("Main Arm Gripper", gripperServo);
        gripper.getClampPositions().OPEN  .setPosition(1);
        gripper.getClampPositions().CLOSED.setPosition(0);
        gripper.getClampPositions().CENTER.setPosition(0.5);

        drivetrain = new Meccanum("Meccanum Drivetrain", a, b, c, d);

        addPeripheral(gripper);
        addPeripheral(drivetrain);

        //Test everything and print to Telemetry
        for (Peripheral peripheral : peripheralList) {
            telemetry.addLine(peripheral.getName() +
                    " tested " +
                    peripheral.test()
            );
        }
    }

    @Override
    public void start() {
        super.start();
        telemetry.clearAll();
    }

    public void update() {
        //update everything that needs it
        for(Updatable updatable : updatableList) {
            updatable.update();
        }
    }

    @Override
    public void loop() {
        update();
    }

    @Override
    public void stop() {
        //Just in case ;)
    }

    //private methods (utility)
    private void addPeripheral(Peripheral peripheral) {
        peripheralList.add(peripheral);
        addUpdatable(peripheral);
    }

    private void addUpdatable(Updatable updatable) {
        this.updatableList.add(updatable);
    }
}
