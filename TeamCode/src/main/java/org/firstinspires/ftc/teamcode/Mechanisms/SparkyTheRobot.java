package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Hardware.MecanumHardware;
import org.firstinspires.ftc.teamcode.Utilities.Audio.SoundEffectManager;

public class SparkyTheRobot extends MecanumHardware {
    public LynxModule rightHub;
    public DcMotorEx leftFlipper;
    public DcMotorEx rightFlipper;
    public DcMotorEx linearSlide;
    public DcMotorEx winch;

    public ServoImplEx leftIntakeFlipper;
    public ServoImplEx rightIntakeFlipper;
    public CRServoImplEx leftIntakeRoller;
    public CRServoImplEx rightIntakeRoller;
    public Intake intake;

    public ServoImplEx markerDeployer;

    public SoundEffectManager soundEffects;

    public SparkyTheRobot(LinearOpMode oM) {super(oM);}

    @Override
    public void init(boolean calibrate) {
        rightHub = hwMap.get(LynxModule.class, "rightHub");

        leftFlipper = hwMap.get(DcMotorEx.class, "flipperLeft");
        rightFlipper = hwMap.get(DcMotorEx.class, "flipperRight");
        rightFlipper.setDirection(DcMotorEx.Direction.REVERSE);
        linearSlide = hwMap.get(DcMotorEx.class, "extender");
        winch = hwMap.get(DcMotorEx.class, "winch");

        leftIntakeFlipper = hwMap.get(ServoImplEx.class, "leftIntakeFlipper");
        rightIntakeFlipper = hwMap.get(ServoImplEx.class, "rightIntakeFlipper");
        leftIntakeRoller = hwMap.get(CRServoImplEx.class, "leftIntakeRoller");
        rightIntakeRoller = hwMap.get(CRServoImplEx.class, "rightIntakeRoller");
        intake = new Intake(leftIntakeFlipper, rightIntakeFlipper, leftIntakeRoller, rightIntakeRoller);
        markerDeployer = hwMap.get(ServoImplEx.class, "markerDeployer");

        leftFlipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFlipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        winch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFlipper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFlipper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        soundEffects = new SoundEffectManager(hwMap.appContext, SoundEffectManager.PACMAN_AUDIO);

        super.init(calibrate);
    }
}
