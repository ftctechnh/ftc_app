package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "TTL2AutoCraterSide", group = "Linear OpMode")
public class TTL2AutoCraterSide extends TTL2Auto {

    @Override
    protected void run() {
        driveVertical(25, 1.0);
    }

}
