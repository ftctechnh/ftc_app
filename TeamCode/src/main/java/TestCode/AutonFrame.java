package TestCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Jake Mueller on 8/11/2017.
 */

public class AutonFrame extends LinearOpMode {

    private DcMotor motorLeft;
    private DcMotor motorRight;

    public AutonFrame(){

    }

    @Override
    public void runOpMode() throws InterruptedException {
        motorLeft = hardwareMap.dcMotor.get("LeftDrive");
        motorRight = hardwareMap.dcMotor.get("RightDrive");

        //insert any autonomous code here

    }
}
