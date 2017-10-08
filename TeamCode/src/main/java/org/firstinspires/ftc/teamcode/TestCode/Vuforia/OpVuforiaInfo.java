package org.firstinspires.ftc.teamcode.TestCode.Vuforia;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


@TeleOp(name = "Vuforia Test" , group = "Prototypes")
@Disabled
@SuppressWarnings("unused")
public class OpVuforiaInfo extends LinearOpMode
{
    @SuppressWarnings("FieldCanBeLocal")
    private VuforiaLocalizer vuforia;

    public void runOpMode() throws InterruptedException
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);


        parameters.vuforiaLicenseKey = "AVL5SH7/////AAAAGQHMTrmZFkVaqLgyIFXlUot8Bj4E0gdKiux+X0Pw/1vkCO02YQNTTWDYnCV2DpnMYwq1E4S745dGDU0oUTRAD8XmI4WPL5MpPRCjvPPUaixm+D4uUrjpGH7+P8v+ZqqSKINM3NayE6lzg96ZrdLhGVnchvIpFsanlkm6hQfQeUSompiOXU8wZdbz+Ryc5WM4nDdI1R7VdmWZGejnMx4OgoqzAcOJbP7tO/FXdsoWFNUZGcO2Az0nnaAX9W5OdDy+78grYk+R9kdYu6eBNwZ/G01zlkiJM24slrCR7wlfiWtH+ywOZpWYRZTGIrsrMLIk3USWLRRO/Yf0wKzyu1nHiqBuRQeVlZgm35T19WQk55FO";


        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);


        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);

        waitForStart();

        relicTrackables.activate();

        while(opModeIsActive())
        {
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
                telemetry.addData("VuMark", "%s visible", vuMark);

                /* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
                 * it is perhaps unlikely that you will actually need to act on this pose information, but
                 * we illustrate it nevertheless, for completeness. */
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
//                telemetry.addData("Pose", format(pose));

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    // Extract the X, Y, and Z components of the offset of the target relative to the robot
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);

                    // Extract the rotational components of the target relative to the robot
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;


                    /*
                         X is forward/backward pitch
                         Y is side to side
                         Z is rotating
                     */
                    telemetry.addData("VuMark rx" , rX);
                    telemetry.addData("VuMark ry" , rY);
                    telemetry.addData("VuMark rz" , rZ);


                    telemetry.addLine();


                    /*
                        X is left/right
                        Y is up/down
                        Z is forward/backward
                     */
                    telemetry.addData("VuMark tx" , tX);
                    telemetry.addData("VuMark tY" , tY);
                    telemetry.addData("VuMark tZ" , tZ);
                }
            }
            else {
                telemetry.addData("VuMark", "not visible");
            }

            telemetry.update();
        }
    }

    String format(OpenGLMatrix transformationMatrix)
    {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
