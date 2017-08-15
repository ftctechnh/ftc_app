package org.firstinspires.ftc.teamcode.seasons.velocityvortex.utilities;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.hardware.motors.NeveRest40Gearmotor;
import com.qualcomm.hardware.motors.NeveRest60Gearmotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.teamcode.seasons.velocityvortex.LinearOpModeBase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Created by aburger on 3/5/2017.
 */

@TeleOp(name = "Zoidberg3Test", group = "utilities")
public class Zoidberg3Test extends LinearOpMode {
    public static final String NOT_IMPLEMENTED_YET = "Not implemented yet";
    private TouchSensor ts;
    private Servo b1;
    private Servo r2;
    private Servo d3;
    private Servo l4;
    private Servo p5;
    private Servo l6;
    private DcMotor intake;
    private DcMotor launcher;
    private DcMotor fr;
    private DcMotor fl;
    private DcMotor s1;
    private DcMotor s2;
    private DcMotor bl;
    private DcMotor br;
    private DeviceInterfaceModule deviceInterfaceModule1;
    private ColorSensor clr;
    private ColorSensor clr2;
    private ColorSensor lcCs;
    private ModernRoboticsI2cRangeSensor frs;
    private GyroSensor gy;
    private OpticalDistanceSensor diskOds;
    private OpticalDistanceSensor lOds;
    private OpticalDistanceSensor rOds;
    private ArrayList<HardwareDevice> hardware = new ArrayList<HardwareDevice>();
    private HardwareDevice device = null;
    private ElapsedTime fromstart = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        telemetry.addData("ArraySize",hardware.size());
        ListIterator<HardwareDevice> iterator = hardware.listIterator();
        telemetry.update();

        waitForStart();

        ElapsedTime timer = new ElapsedTime();
        while (this.opModeIsActive()) {

//            telemetry.addData("Timer",timer.seconds());

            if(gamepad1.right_bumper && timer.milliseconds() > 500) {
                telemetry.addData("right_bumper", "pressed at " + fromstart.seconds());
                if (iterator.hasNext()){
                    device = iterator.next();
                    telemetry.addData("device", String.format("[ previous index = %d, name = %s ]",
                            iterator.previousIndex(), device.getDeviceName()));
                } else {
                    telemetry.addData("array", "does not have next");
                }
                telemetry.update();
                timer.reset();
            }


            if(gamepad1.left_bumper && timer.milliseconds() > 500) {
                telemetry.addData("left_bumper", "pressed at " + fromstart.seconds());
                if (iterator.hasPrevious()){
                    device = iterator.previous();
                    telemetry.addData("device", String.format("[ next index = %d, name = %s ]",
                            iterator.nextIndex(), device.getDeviceName()));
                } else {
                    telemetry.addData("array", "does not have previous");
                }
                telemetry.update();
                timer.reset();
            }

            if (device != null && timer.milliseconds() > 2000){
                telemetry.addData(">",getDeviceInfo(iterator,device));
                telemetry.update();
            }
//
//            if (gamepad1.dpad_down && timer.milliseconds() > 500) {
//                telemetry.addData("Dpad", "Down");
//                if (iterator.hasPrevious()){
//                    telemetry.addData("array", "has prev");
//                    device = iterator.previous();
//                }
//                timer.reset();
//                telemetry.update();
//
//            }
//
//            if(gamepad1.dpad_up && timer.milliseconds() > 500) {
//                telemetry.addData("Dpad", "Up");
//                if (iterator.hasNext()) {
//                    telemetry.addData("array", "has next");
//                    device = iterator.next();
//                }
//                timer.reset();
//                telemetry.update();
//
//            }
//
//
//            runMotor(gamepad1.x, fl);
//            runMotor(gamepad1.y, fr);
//            runMotor(gamepad1.a, bl);
//            runMotor(gamepad1.b, br);


        }
    }

    private String getDeviceInfo(Iterator<HardwareDevice> iterator, HardwareDevice device) {
        return "["+ "]" + device.getDeviceName() + "=" + reading(device);
    }

    private String reading(HardwareDevice device) {
        if (device instanceof DcMotor ){
            return reading((DcMotor) device);
        } else if (device instanceof ColorSensor){
            return reading((ColorSensor) device);
        } else if (device instanceof ModernRoboticsI2cRangeSensor) {
           return reading((ModernRoboticsI2cRangeSensor) device);
        } else if (device instanceof ModernRoboticsI2cGyro) {
           return reading((ModernRoboticsI2cGyro) device);
        } else if (device instanceof TouchSensor) {
            return reading((TouchSensor) device);
        } else if (device instanceof OpticalDistanceSensor) {
            return reading((OpticalDistanceSensor) device);
        } else {
            return NOT_IMPLEMENTED_YET;
        }
    }

    private void initializeHardware() {
        //ServoController ServoController1 = hardwareMap.get(ServoController.class, "Servo Controller 1");
        b1 = hardwareMap.get(Servo.class, "b1");
        r2 = hardwareMap.get(Servo.class, "r2");
        d3 = hardwareMap.get(Servo.class, "d3");
        l4 = hardwareMap.get(Servo.class, "l4");
        p5 = hardwareMap.get(Servo.class, "p5");
        l6 = hardwareMap.get(Servo.class, "l6");

        intake = hardwareMap.get(DcMotor.class, "intake");
        launcher = hardwareMap.get(DcMotor.class, "launcher");
        fr = hardwareMap.get(DcMotor.class, "fr");
        fl = hardwareMap.get(DcMotor.class, "fl");
        s1 = hardwareMap.get(DcMotor.class, "s1");
        s2 = hardwareMap.get(DcMotor.class, "s2");
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");

        //deviceInterfaceModule1 = hardwareMap.get(DeviceInterfaceModule.class, "Device Interface Module 1");
        clr = hardwareMap.get(ColorSensor.class, "clr");
        hardware.add(clr);
        clr2 = hardwareMap.get(ColorSensor.class, "clr2");
        hardware.add(clr2);
        lcCs = hardwareMap.get(ColorSensor.class, "lcCs");
        hardware.add(lcCs);

        frs = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "frs");
        hardware.add(frs);
        gy = hardwareMap.get(GyroSensor.class, "gy");
        hardware.add(gy);
        diskOds = hardwareMap.get(OpticalDistanceSensor.class, "diskOds");
        hardware.add(diskOds);
        lOds = hardwareMap.get(OpticalDistanceSensor.class, "lOds");
        hardware.add(lOds);
        rOds = hardwareMap.get(OpticalDistanceSensor.class, "rOds");
        hardware.add(rOds);
        ts = hardwareMap.get(TouchSensor.class, "ts");
        hardware.add(ts);

    }


    private void runMotor(boolean button, DcMotor motor) {
        if (button) {
            motor.setPower(.3);
        } else {
            motor.setPower(0);
        }
    }

    private String reading(DcMotor m) {
        StringBuilder string = new StringBuilder();
        string.append(": p=");
        string.append(m.getCurrentPosition());
        return string.toString();
    }

    private String reading(ModernRoboticsI2cRangeSensor range) {
        StringBuilder string = new StringBuilder();
        string.append("ld(");
        string.append(range.getLightDetected());
        string.append(")");
        string.append("rld(");
        string.append(range.getRawLightDetected());
        string.append(")");
        string.append("rldm(");
        string.append(range.getRawLightDetectedMax());
        string.append(")");
        string.append("cmO:");
        string.append(range.cmOptical());
        string.append("cmU:");
        string.append(range.cmUltrasonic());
        return string.toString();
    }

    private String reading(OpticalDistanceSensor ods) {
        StringBuilder string = new StringBuilder();
        string.append("ld(");
        string.append(new DecimalFormat("##.###").format(ods.getLightDetected()));
        string.append(")");
        string.append("rld(");
        string.append(new DecimalFormat("##.###").format(ods.getRawLightDetected()));
        string.append(")");
        string.append("rldm(");
        string.append(new DecimalFormat("##.###").format(ods.getRawLightDetectedMax()));
        string.append(")");
        return string.toString();
    }

    private String reading(ModernRoboticsI2cGyro gyroSensor) {
        StringBuilder string = new StringBuilder();
        string.append("x(");
        string.append(gyroSensor.rawX());
        string.append(") ");
        string.append("y(");
        string.append(gyroSensor.rawY());
        string.append(") ");
        string.append("z(");
        string.append(gyroSensor.rawZ());
        string.append(") ");
        return string.toString();

    }

    private String reading(ColorSensor colorSensor) {
        StringBuilder string = new StringBuilder();
        string.append("R(");
        string.append(colorSensor.red());
        string.append(") G(");
        string.append(colorSensor.green());
        string.append(") B(");
        string.append(colorSensor.blue());
        string.append(") A(");
        string.append(colorSensor.alpha());
        string.append(")");

        return string.toString();
    }
    private String reading(TouchSensor touchSensor) {
        StringBuilder string = new StringBuilder();
        string.append("Pressed(");
        string.append(touchSensor.isPressed());
        string.append(")");

        return string.toString();
    }
}
