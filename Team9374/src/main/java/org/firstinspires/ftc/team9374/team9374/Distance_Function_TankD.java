package org.firstinspires.ftc.team9374.team9374;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/*
=======
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
>>>>>>> e920dc6351b8ece876e59ccd20e250de48f454da
 * Created by darwin on 9/20/16.
 */

@TeleOp(name="Basic Tank Drive!", group="Noramal_Opmode")
@Disabled

public class Distance_Function_TankD extends OpMode {
    DcMotor left;
    DcMotor right;

    public void init()  {
        left = hardwareMap.dcMotor.get("Motor-left");
        right = hardwareMap.dcMotor.get("Motor-right");
    }


    @Override
    public void loop() {
        float leftDC = gamepad1.left_stick_y;
        float rightDC =  gamepad1.right_stick_y;


        left.setPower(leftDC);
        right.setPower(rightDC);
        //if(leftDCy > 0)



    }
}
