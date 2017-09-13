package org.firstinspires.ftc.teamcode;

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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Pictogram Identification", group = "7604")
public class PictogramIdentification7604 extends LinearOpMode
{
	@Override
	public void runOpMode()
	{

		int cameraMonitorViewId = hardwareMap.appContext.getResources()
				.getIdentifier("cameraMonitorViewId", "id",
						hardwareMap.appContext.getPackageName());
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(
				cameraMonitorViewId);
		parameters.vuforiaLicenseKey = "ASv2MNj/////AAAAGQukwPKRd0YcsSlpoJYzs9EdjNGpnGv0mY+oWYr923xV6ZP+Tm9A7ZjZvdw7KY3iqJ/2AXpNLeHZLylMumJd46ZYL4zpkdjPY6OwGwUmQBrgo6MXWgIM6bKgp/0M1SJnb8yYpFjzTAqAXtXqotY5KPiLkelgBeCuPYc+NUAlf6vSxjEr7+Zezid1O2zV3dRV/FlaBJN9MQsgWOvPQfsTiKqgpEr2b4pLG8PMqL/HU3RvuEexsWSv5eN6mWtx8Vt7m+GSBC6xo9vxR+gaTLsi19RAXTPCq4UhoQvrFYIORotVeVa5zIhZXlpMc09NZT25e6DcOPTv2eloL55O2/FK81AGay8e4urLNQ5wF3vknehR";

		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
		VuforiaLocalizer vuforia = ClassFactory
				.createVuforiaLocalizer(parameters);

		// FIXME This may or may not work, since the RelicVuMark files are in
		VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
		VuforiaTrackable relicTemplate = relicTrackables.get(0);
		relicTemplate.setName("relicVuMarkTemplate");
		telemetry.addData(">", "Press Play to start");
		telemetry.update();
		waitForStart();

		relicTrackables.activate();

		while (opModeIsActive())
		{
			RelicRecoveryVuMark vuMark = RelicRecoveryVuMark
					.from(relicTemplate);
			if (vuMark != RelicRecoveryVuMark.UNKNOWN)
			{
        		telemetry.addData("VuMark", "%s visible", vuMark);

				OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) relicTemplate
						.getListener()).getPose();
				telemetry.addData("Pose", format(pose));

				if (pose != null)
				{
					VectorF trans = pose.getTranslation();
					Orientation rot = Orientation.getOrientation(pose,
							AxesReference.EXTRINSIC, AxesOrder.XYZ,
							AngleUnit.DEGREES);

					double tX = trans.get(0);
					double tY = trans.get(1);
					double tZ = trans.get(2);

					double rX = rot.firstAngle;
					double rY = rot.secondAngle;
					double rZ = rot.thirdAngle;

					telemetry.addData("Translation", "[%f,%f,%f]", tX, tY, tZ);
					telemetry.addData("Rotation", "[%f,%f,%f]", rX, rY, rZ);
				}
			}
			else
			{
				telemetry.addData("VuMark", "not visible");
			}

			telemetry.update();
		}
	}

	private String format(OpenGLMatrix transformationMatrix)
	{
		return (transformationMatrix != null)
				? transformationMatrix.formatAsTransform()
				: "null";
	}
}
