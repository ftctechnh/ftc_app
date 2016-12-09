package org.firstinspires.ftc.teamcode; /**
 * Created by billcipher1344 on 10/24/2016.
 */
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.modules.GamepadV2;

import org.firstinspires.ftc.teamcode.modules.MecanumDrive;

@TeleOp
public class MecanumTest extends OpMode{

    public DcMotor frontLeft, frontRight, backLeft, backRight;
    public GamepadV2 pad1 = new GamepadV2();

    public void init(){

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void loop(){

        pad1.update(gamepad1);
        MecanumDrive.loop(frontLeft, frontRight, backLeft, backRight, pad1);
    }
}
