# What is it?
The ScoringAPI allows users to create and modify scoring algorithms that filter CV contour results into a chosen selection. In English, this means the ScoringAPI takes a list of unfiltered results of contours and can run operations to determine which result is the "Best". It does this by calculating the distance from perfect, so in this case, 0 = Perfect, and anything about 0 is considered to be the worst result. Example of this include ratio of the bounding box or perfect area of the contour (see below).

**NOTE:** The ScoringAPI is brand new and I want to rework it, so this is subject to change, however, all changes should be settled by 2018.3 (Competition Ready Update)

Example Scorer Math:
```java
diffrence = Math.abs((rect.width / rect.height) - perfectRatio);
//Input Rect    = 100/150
//Rect Ratio    = 0.666
//Perect Ration = 1.0;
//Diffrence     = 0.333
```

# How to use it
## Using Defaults
By default, each detector will have 0 scorers, we do this so you can only add your own later on, however, to enable default doctors, each 2018 detector (detectors that use the DogeCVDetector class) have a useDefaults(); a function that will enable default settings and scores. So simply run useDefault before you call enable(), and you should have working detectors.

```java
detector.useDefaults();
```

## Tuning parameters of default scorers in detectors
Each detector will make use of multiple scorers with unique names (ratioScorer, areaScorer, etc.). The detector documentation will list which scorers they are you using and their names. In addition, each detector may have different tuning options, so consult the documentation below for parameters, however, an example using the ratio scorer is below:
```java
detector.ratioScorer.weight = 15;
detector.ratioScorer.perfectRatio = 1.0;
```
**NOTE:** all scorers have a parameter called weight, this is a final multiple applied to the score, this can be thought of how important this scorer is, acceptable weights are described in scorer documentation.

## Replacing Scorers in included detectors.
Now as described above, default scorers have to be enabled specifically, if you don't use `useDefault();` then the detector will be created with 0 scorers. This is where we can modify what scorers we want it to use. This leaves you with 2 options: **Add an additional** or **Set all**
### Add an additional
If you create or want to add in an additional scorer on top of the default set, you can call `useDefaults()` then `addScoerer(scorer);` to add a new scorer to the stack.
Example:
```java
detector.useDefaults();
detector.addScorer(new RatioScorer(1.0,10)); // 1.0 and 10 being parameters for the detector
```

### Replace and Set Scorers
The process is the same however you don't call `useDefaults();`. This means for all scorers you have to manually call `addScoerer(scorer);`
Example:
```java
detector.addScorer(new RatioScorer(1.0,10));
detector.addScorer(new MaxAreaScorer(50000,10));
```

## Making your own DogeCVScorer
To make your own DogeCVScorer, simply created a class that extends `DogeCVScorer` and add the one override method `Alt+Enter` in Android Studio to automatically do this. You can also look at the below template:
```java
import org.opencv.core.MatOfPoint;
import com.disnodeteam.dogecv.scoring.DogeCVScorer;
public class PerfectAreaScorer extends DogeCVScorer {
    @Override
    public double calculateDifference(MatOfPoint contours) {
        return 0; // Finally Difference
    }
}
```
The API will give you OpenCV contours (`contours`) of the object that the detector desires to be scored. You can run your operations on this and finally return a difference (`double`). A difference is made up using the basic rule (lower is better), however, try and make it as close to 0 as possible for perfect instead of going to negative numbers (best practice).

Now for parameters simply make them public variables and use the constructor, this isn't required but is simple and works, however, any parameter system you want to use should work fine.

Finally, make sure to add in a `weight` parameter and multiply for your final score by it, this allows for easy tuning.

For a simple example of this look at `RatioScorer`.

```java
public class RatioScorer extends DogeCVScorer{

    public double weight       = 1.0;
    public double perfectRatio = 1.0;
    public RatioScorer(){

    }
    public RatioScorer(double perfectRatio, double weight){
        this.weight = weight;
        this.perfectRatio = perfectRatio;
    }

    @Override
    public double calculateDifference(MatOfPoint contours) {
        // Get bounding rect of contour
        Rect rect = Imgproc.boundingRect(contours);
        double x = rect.x;
        double y = rect.y;
        double w = rect.width;
        double h = rect.height;

        double cubeRatio = Math.max(Math.abs(h/w), Math.abs(w/h)); // Get the ratio. We use max in case h and w get swapped??? it happens when u account for rotation
        double ratioDiffrence = Math.abs(cubeRatio - perfectRatio);
        return ratioDiffrence * weight;
    }
}
```

## Using in your own custom detectors
To use a scorer, look at the **Replacing Scorers in included detectors.** section and replace it with your scorer.

# Included Scorers (As of 2018.0)
## MaxAreaScorer
### About
This scorer bigger objects with less difference, resulting in the largest object being chosen. This is considered the most basic scorer as it does not involve much tuning per bot, however `PerfectAreaScorer` is recommended when possible.
**NOTE** Currently this sorcerer gives negative results a lot, soon I want to improve this detector and fix this.
### Constructor
```java
new MaxAreaScorer(double weight);
```

### Parameters
* `weight` - (`double`) - How Much the final difference should be multiplied by. (Ranges: 0.001 -> 0.01)

## RatioScorer
### About
This scorer looks for objects near a certain ratio (width/height). This is used for finding objects with a known ratio like cubes or spheres (as a bounding rent for them would be close to a ratio of 1.0). We do this by calculating the absolute difference of the width/height of a bounding rect to the perfect ration provided.
### Constructor
```java
new RatioScorer(double perfectRatio, double weight);
```
### Parameters
* `weight` - (`double`) - How Much the final difference should be multiplied by. (Ranges: 1 -> 20)
* `perfectRatio` - (`double`) - Perfect ratio.
