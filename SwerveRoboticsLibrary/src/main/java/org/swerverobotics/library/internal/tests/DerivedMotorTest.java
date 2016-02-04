package org.swerverobotics.library.internal.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.robot.Robot;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

@TeleOp(name="Derived Motor Test (Raw)", group="Swerve Tests")
@Disabled
public class DerivedMotorTest extends OpMode
    {
    public static class DerivedMotor extends DcMotor
        {
        public DerivedMotor(DcMotorController controller, int portNumber)
            {
            super(controller, portNumber);
            }
        }

    DerivedMotor leftMotor;
    DerivedMotor rightMotor;
    DcMotorController easyMotorController;

    @Override
    public void init()
        {
        DcMotor lTmp = hardwareMap.dcMotor.get("motorLeft");
        DcMotor rTmp = hardwareMap.dcMotor.get("motorRight");

        leftMotor = new DerivedMotor(lTmp.getController(), 1);
        rightMotor = new DerivedMotor(rTmp.getController(), 2);

        easyMotorController = ClassFactory.createEasyMotorController(this, leftMotor, rightMotor);
        }

    @Override
    public void loop()
        {
        }
    }
