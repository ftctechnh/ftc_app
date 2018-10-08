# What is it?
The color filter API is a plug-n-play way to have our detectors (and your own) change how detect color. You can use it in many different ways, either by trying out different filters in our current detectors (however we usually set the defaults to best one for the job), making your own detector using our GenericDetector and feeding in a color filter, making your own color filter and using it in DogeCV, or using these filters in your own custom detector. No matter your use, the Color Filter API makes it easy.

# How to use it
## Replacing Color Filters in existing detectors
All of our detectors that use color have a parameter for a colorfilter. For detectors like jewel or cryptobox that do red and blue, there are separate parameters. These parameters are usually named either `colorFilter` or `colorFilterRed/Blue` in each detector, acorse all the parameters for each detector can been seen their wiki pages.

In order to replace a color filter you must choose a color filter and create it. This is done by using `new`. Also feed in the apprioate parameters for this color filter as listed below for each detector. Then set the detectors parameter to this class. Examples below:
```java
genericDetector.colorFilter = new LeviColorFilter(LeviColorFilter.ColorPreset.YELLOW);
genericDetector.colorFilter = new HSVColorFilter(new Scalar(30,200,200), new Scalar(15,50,50));
cryptoboxDetector.colorFilterBlue = new LeviColorFilter(LeviColorFilter.ColorPreset.BLUE);
cryptoboxDetector.colorFilterRed = new LeviColorFilter(LeviColorFilter.ColorPreset.RED);
```
And that's all!

## Using with the Generic Detector
The generic detector has a full wiki page dedicated to it [Here!](), so for a full run down go there since here we will just briefly go in to using color filters with this detector.

The Generic detector is the best use for Color Filters since its made to accept a wide range of colors and for you to make it your own. To set the color filter simply set `colorFilter` to your filter. Currently if your using RED/BLUE/YELLOW colors found on the field, `LeviColorFilter` is the best way to go, however HSV, although not the most robust, is the most customizable as you can feed it in custom color ranges.

## Making your own DogeCVColorFilter
To make your own DogeCVColorFilter, simply created a class that extends `DogeCVColorFilter` and add the one override method `Alt+Enter` in Android Studio to automatically do this. You can also look at the below templated:
```java
package com.disnodeteam.dogecv.filters;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MyColorFilter extends DogeCVColorFilter{
    public MyColorFilter (){
    }

    @Override
    public void process(Mat input, Mat mask) {
    }
}
```

I recommend making this class inside the DogeCV moduel and in the package `com.disnodeteam.dogecv.filters`

Once created you can have it take in any parameters inside its constructor. Inside the `process` function you must set `mask` to a binary `Mat` (aka Black and White). This is usually returned in functions like `Core.inRange` so it shouldn't be much trouble. For a simple example of this look at `HSVColorDetector`.

```java
public void process(Mat input, Mat mask) {
        Imgproc.cvtColor(input,input,Imgproc.COLOR_RGB2HSV_FULL);
        Imgproc.GaussianBlur(input,input,new Size(3,3),0);

        Scalar lower = new Scalar(perfect.val[0] - range.val[0], perfect.val[1] - range.val[1],perfect.val[2] - range.val[2]);
        Scalar upper = new Scalar(perfect.val[0] + range.val[0], perfect.val[1] + range.val[1],perfect.val[2] + range.val[2]);
        Core.inRange(input,lower,upper,mask);
        input.release();
}
```
**NOTE:** Run `input.release()` at the end to avoid memory leaks.

## Using in your own custom detectors
To use a color filter simply create it as a variable like:
```java
public DogeCVColorFilter colorFilter = new HSVColorFilter(new Scalar(50,50,50), new Scalar(50,50,50));
```
Then to run it pass it a Mat `input` and your desired output mat `mask`. NOTE THE INPUT MAT IS CHANGED SO ITS RECOMMENDED TO MAKE ITS OWN MAT AND RELEASE IT RIGHT AFTER. Example below.
```java
Mat preConvert = workingMat.clone();
colorFilter.process(preConvert,mask);
preConvert.release();
```
# Installed Color Filters
## LeviColorFilter
### About
These set of pre-defined color filters were devloped by Levi to deliver the cleanest masks for the colors found in this years FTC games. If doing anything with game elements that are of these colors, it recommened to use these filters.

They work by converting to a diffrent color space (listed below), then picking a channel which makes the image a greyscale of that one channel, and finally running a threshold of these grey image. The result is a binary mask of the color.
* **RED** -> _Lab Colorspace_ (Channel 2)
* **BLUE** -> _YUV Colorspace_ (Channel 2)
* **YELLOW** -> _YUV Colorspace_ (Channel 2)

### Parameters
* `filterColor` - (`ColorPreset`) - The color to find (`RED`, `BLUE`, `YELLOW`)
* `filterThreshold` - (_optional_ `double`) - The threshold of the converted channel / grey image. Leave to use the defaults for each color
## HSVColorFilter
### About
This is your standard HSV inRange mask generator. This is usually the simplest approach to filtering a color. To use it simply define your color (in HSV Color Space) and then your threshold for each channel. (We +/- each channel value in your color by this threshold. Your color should be the middle of this range)
### Parameters
* `color` - (`Scaler`) - The color to find (HSV Color Space)
* `threshold` - (`Scaler`) - The threshold of the color (in all 3 channels)