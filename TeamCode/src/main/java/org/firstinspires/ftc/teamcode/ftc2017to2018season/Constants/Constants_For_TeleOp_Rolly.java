/*============================================================================================================================================
                                                            EDIT HISTORY



when                                      who                       Purpose/Change
-----------------------------------------------------------------------------------------------------------------------------------------------
3/29/18                                   Rohan                    Added constants to this program so we can use it more easily
3/30/18                                   Rohan                    Added all numerical constants to the program
3/30/18                                   Rohan                    Added comments showing what values are for which functions
=============================================================================================================================================*/
package org.firstinspires.ftc.teamcode.ftc2017to2018season.Constants;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Constants_For_TeleOp_Rolly{
//glyphManipulate values
    public double glyphIncrementValue = 0.05;
//glyphIntake values
    public double glyphMotorPower = 1;
//fourWheelDrive values
    public double fourWheelDriveThreshhold = 0.2;
    public double leftTopStrafeSpeed = 0.9;
    public double leftBottomStrafeSpeed = 1;
    public double rightTopStrafeSpeed = 1;
    public double rightBottomStrafeSpeed = 1;
    public double slowMovementMotorPower = 0.5;
//slideIncrement values
    public double dpad_upIncrementValue = 33.02;
    public double d_padRightIncrementValue = 20.32;
    public double d_padDownIncrementValue = 5.08;
//relicManipulator values
    public double relicMainDownPosition = 0.1;
    public double relicMainMiddlePosition = 0.6;
    public double relicMainUpPosition = 1;
    public double relicOpenClaw = 1.0;
    public double relicCloseClaw = 0.2;
    public double relicMiddleClaw = 0.5;
//moveUpInch values
    public float slideCPCM = 50;
    public double slideMotorPowerIncrementing = 0.6;
    public double slideIncrementFailSafeSec = 1.5;
}
