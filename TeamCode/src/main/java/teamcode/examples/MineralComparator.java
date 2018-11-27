package teamcode.examples;

//import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.Comparator;

public class MineralComparator implements Comparator<Mineral> {

    public MineralComparator() {
    }

    @Override
    public int compare(Mineral mineral, Mineral t1) {
        if (mineral.getBottom() < t1.getBottom()) {
            return -1;
        }

        if (mineral.getBottom() > t1.getBottom()) {
            return 1;
        }

        return 0;
    }
}
