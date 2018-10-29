package org.firstinspires.ftc.teamcode;
import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

import java.util.ArrayList;
import java.util.List;


public class NewHardware {
    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public DcMotor liftBot;
    public DcMotor liftBot2;
    //public Servo latch;
    //public ColorSensor sensor;

    HardwareMap hwMap;

    public void initialize(HardwareMap hwMap)
    {
        this.hwMap = hwMap;

        leftDrive = hwMap.get(DcMotor.class,"leftDrive");
        rightDrive = hwMap.get(DcMotor.class,"rightDrive");
        liftBot = hwMap.get(DcMotor.class,"liftBot");
        liftBot2 = hwMap.get(DcMotor.class,"liftBot2");
        //latch = hwMap.get(Servo.class,"latch");
       // sensor = hwMap.get(ColorSensor.class,"sensor");*/
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        liftBot.setPower(0);
        liftBot2.setPower(0);
        leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
    }
}



