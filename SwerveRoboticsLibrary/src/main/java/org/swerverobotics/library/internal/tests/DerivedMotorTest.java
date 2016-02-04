package org.swerverobotics.library.internal.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

@TeleOp(name="Derived Motor Test (Raw)", group="Swerve Tests")
public class DerivedMotorTest extends OpMode
    {
    Team25DcMotor leftMotor;
    Team25DcMotor rightMotor;
    DcMotorController easyMotorController;

    @Override
    public void init()
        {
        DcMotor lTmp = hardwareMap.dcMotor.get("motorLeft");
        DcMotor rTmp = hardwareMap.dcMotor.get("motorRight");

        leftMotor = new Team25DcMotor(null, lTmp.getController(), 1);
        rightMotor = new Team25DcMotor(null, rTmp.getController(), 2);

        easyMotorController = ClassFactory.createEasyMotorController(this, leftMotor, rightMotor);
        }

    @Override
    public void loop()
        {
        }

    @Override
    public void stop()
        {
//         easyMotorController.close();
//         easyMotorController = null;
        }
    }
