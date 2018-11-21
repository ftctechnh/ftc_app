package org.firstinspires.ftc.teamcode.robotutil

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.Utils.Logger
import org.firstinspires.ftc.teamcode.Utils.Utils

public class DriveTrain2(val opMode: OpMode){
    val l:Logger = Logger("DRIVETRAIN2")
    val lDrive = opMode.hardwareMap.dcMotor.get("lDrive")
    val rDrive  = opMode.hardwareMap.dcMotor.get("rDrive")
    val allMotors = arrayOf<DcMotor>(lDrive,rDrive)
    val driveMotors:MotorGroup = MotorGroup(allMotors)

    init {
        lDrive.direction = DcMotorSimple.Direction.FORWARD
        rDrive.direction = DcMotorSimple.Direction.REVERSE
        driveMotors.resetEncoders();
        driveMotors.setBrake();
        l.log("Initialized settings");
    }

    fun setPowers(lPower:Double,rPower:Double){

        lDrive.power = lPower
        rDrive.power = rPower
    }

}