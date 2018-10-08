![](https://i.imgur.com/AhfWrGI.png)
**STATUS:** _90% Competition Ready_   
**LAST UPDATED:** _1/1/2017 with DogeCV 1.1_

# About
This detector detects the jewels on their stand and returns their orientation.

### Todo
- Better filters/bluring
- Quality Modes
- Roundness checking
- Further scoring based on retaliative positioning
### Known Issues
- Background objects can still affect it.
### Credits
- Levi

# Details
This detector uses Levi color filters to generate a blue and red mask. Using these masks we find contours (edges) and draw boxes around them. We then go into scoring these boxes.

To score these boxes, we calculate the difference from "Perfect" in different attributes such as ration (height/width of the box) and area of it. That way the box that is closest to each of these perfect values is deemed the best result.

The two attributes we use for this scoring are ratio of the cube and area of it in pixels. We uses these two attributes to find a perfect square that is the size of a jewel (or the largest object). Both of these values and how important are they to the final score can be tuned.

# Usage
THis detector is made to be used in the place of a color sensor in order to detect the jewels orientation in autonomous periods. The detector returns a enum representing the orientation/status. This can be fed to your bots code to have it knock off the correct jewel.

## Tuning
_Still have to do this. Theres alot :/_

## Parameters
- `colorFilterRed` - Color filter to use for red jewel
- `colorFilterBlue` - Color filter to use for blue jewel
- `downScaleFactor` - double representing how much to downscale each frame.
- `detectionMode` - Mode used to score results, 
  - `MAX_AREA` returns the largest objects in the frame (Good for quick and easy since it doesn't need tuning, but less accurate)
  - `PERFECT_AREA` returns the objects closest to a desired area (Needs tuning, use debugContours to get areas)
- `rotateMat` - Rotate the image when processing (wont be visible on preview, change this if you see detections working horizontally) [Usually: Landscape = false, Portrait = true]
- `areaWeight` - How much does area affect the results (should always be a decimal under 0.1 due to the large numbers returned by areas)
- `perfectArea` - The area of an object (in pixels) that is deemed "perfect" for jewels.
- `debugContours` - Show debug information for contours in the image, including areas.
- `maxDiffrence` - Max diffrence for objects (lower is more restrictive, recommend 10-20)
- `ratioWeight` - How is the ratio of the object's width and height weighted (recommend 10-20)
- `minArea` - Min area for objets (this depends on downScale, but I recommend 500+);

## Returned Data
Currently this detector returns the following:
- `getLastOrder()` - Get the last known jewel order (`BLUE_RED` or `RED_BLUE`)
- `getCurrentOrder()` -  Get the current jewel order (`BLUE_RED`, `RED_BLUE` or `UNKNOWN`)


# Changelogs
- **1.1**
  - Now uses Color Filter API
  - Debug Text Cleaned up