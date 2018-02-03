package org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by FTC 4316 on 1/14/2018
 */

public class Vision {

    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;

    VuforiaTrackables relicTrackables;
    VuforiaTrackable relicTemplate;
    VuforiaLocalizer.Parameters parameters;

    private int columnNum = 0;

    public Vision(VuforiaLocalizer vuforia, VuforiaLocalizer.Parameters parameters, VuforiaTrackables relicTrackables, VuforiaTrackable relicTemplate){
        this.vuforia = vuforia;
        this.parameters = parameters;
        this.relicTrackables = relicTrackables;
        this.relicTemplate = relicTemplate;


        parameters.vuforiaLicenseKey = "AUkgO0T/////AAAAGaYeMjdF+Us8tdP9fJcRhP9239Bwgzo0STjrR4II0s58wT/ja6GlSAQi/ptpHERhBhdNq8MMmlxC6bjyebsGnr/26IxYKhFFdC67Q7HE0jhDrsrEfxfJMFnsk2zSdt5ofwm2Z1xNhdBg2kfFCzdodI7aHFEdUQ6fddoTioTSPu9zzU9XqBr7Ra+5mTaIwp10heZmlXIjWfu8220ef/tZQ8QSmDX1GSqRLBjUJspesff8Nv9pkQAK3Nvp8YFHKJoFNkSV7QJW7mi/liHYq6DxYqhWk977WYGwzhHA003HNV4OUWhTLJGiPsiFhAlcJVbnVMn6ldnsSauT4unjXA9VBIzaYtSJc29UJYmWyin3MxPz";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        this.relicTemplate = relicTrackables.get(0);
        this.relicTemplate.setName("relicVuMarkTemplate");


        relicTrackables.activate();
    }

    public RelicRecoveryVuMark detectPictograph(){
        return RelicRecoveryVuMark.from(relicTemplate);
    }

    public int keyColumnDetect(AllianceColor color){
        RelicRecoveryVuMark column = detectPictograph();

        if(column == RelicRecoveryVuMark.UNKNOWN){
            columnNum = 0;
        } else if(color == AllianceColor.RED){
            if(column == RelicRecoveryVuMark.RIGHT){
                columnNum = 1;
            } else if(column == RelicRecoveryVuMark.CENTER){
                columnNum = 2;
            } else if(column == RelicRecoveryVuMark.LEFT){
                columnNum = 3;
            }
        } else if(color == AllianceColor.BLUE){
            if(column == RelicRecoveryVuMark.LEFT){
                columnNum = 1;
            } else if(column == RelicRecoveryVuMark.CENTER){
                columnNum = 2;
            } else if(column == RelicRecoveryVuMark.RIGHT){
                columnNum = 3;
            }
        }
        return columnNum;
    }
}
