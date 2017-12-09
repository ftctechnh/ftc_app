package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.impl.HDriveTrain;
import org.firstinspires.ftc.teamcode.mechanism.impl.VisionHelper;
import org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.GlyphLift;
import org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.Intake;

/**
 * This class represents the Relic Recovery robot.
 */
public class RelicRecoveryRobot extends Robot {
    protected HDriveTrain hDriveTrain;
    protected GlyphLift glyphLift;
    protected Intake intake;
    protected VisionHelper visionHelper;
    protected ColorSensor balancingStoneSensor;
    protected ColorSensor jewelSensor;

    /**
     * Construct a new Relic Recovery robot, with an op-mode that is using this robot.
     *
     * @param opMode the op-mode that this robot is using.
     */
    public RelicRecoveryRobot(OpMode opMode) {
        super(opMode);

        this.hDriveTrain = new HDriveTrain(this);
        this.glyphLift = new GlyphLift(this);
        this.intake = new Intake(this);
        this.visionHelper = new VisionHelper(this);
        this.balancingStoneSensor = new ColorSensor() {
            @Override
            public int red() {
                return 0;
            }

            @Override
            public int green() {
                return 0;
            }

            @Override
            public int blue() {
                return 0;
            }

            @Override
            public int alpha() {
                return 0;
            }

            @Override
            public int argb() {
                return 0;
            }

            @Override
            public void enableLed(boolean enable) {

            }

            @Override
            public void setI2cAddress(I2cAddr newAddress) {

            }

            @Override
            public I2cAddr getI2cAddress() {
                return null;
            }

            @Override
            public Manufacturer getManufacturer() {
                return null;
            }

            @Override
            public String getDeviceName() {
                return null;
            }

            @Override
            public String getConnectionInfo() {
                return null;
            }

            @Override
            public int getVersion() {
                return 0;
            }

            @Override
            public void resetDeviceConfigurationForOpMode() {

            }

            @Override
            public void close() {

            }
        };
        this.jewelSensor = new ColorSensor() {
            @Override
            public int red() {
                return 0;
            }

            @Override
            public int green() {
                return 0;
            }

            @Override
            public int blue() {
                return 0;
            }

            @Override
            public int alpha() {
                return 0;
            }

            @Override
            public int argb() {
                return 0;
            }

            @Override
            public void enableLed(boolean enable) {

            }

            @Override
            public void setI2cAddress(I2cAddr newAddress) {

            }

            @Override
            public I2cAddr getI2cAddress() {
                return null;
            }

            @Override
            public Manufacturer getManufacturer() {
                return null;
            }

            @Override
            public String getDeviceName() {
                return null;
            }

            @Override
            public String getConnectionInfo() {
                return null;
            }

            @Override
            public int getVersion() {
                return 0;
            }

            @Override
            public void resetDeviceConfigurationForOpMode() {

            }

            @Override
            public void close() {

            }
        };

        visionHelper.initializeVuforia(VuforiaLocalizer.CameraDirection.BACK);
    }
}
