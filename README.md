

<div align="center">
    <img src="https://i.imgur.com/bMAmxfZ.png" width="100%"/>
    <br></br>
  <p>
    <a href="https://discord.gg/colton"><img src="https://discordapp.com/api/guilds/345404637374971907/embed.png" alt="Discord server" /></a>
  </p>
  <b>Developed by Alex Carter of Disnode Robotics and Levi Gershon of Newton's Law</b>
    <br/>
     <i>Version 2018.2 | Updated 10/08/2018 </i>

</div>

# DogeCV
A easy to use computer vision library used for FTC Games to detect game objects. Based on Ender CV and OpenCV. 

## Project Status:
**IN DEVELOPMENT. CURRENTLY IN ALPHA. NOT COMPETITION READY**

# DISCLAIMER
### THIS REPO IS STILL UNDER HEAVY DEVELOPMENT. WE WILL BE ADDING FURTHER DOCUMENTATION, BUG FIXES AND NEW DETECTORS SOON.
Also please keep in mind due to the major rewrite, many components have not been fully tested and polished, and many Relic Recovery detectors may be broken.

## Videos
Wizards.exe have been amazing with their DogeCV coverage, so please check them out and give your support, the videos are easy to understand and well made, great for peopling who want to learn DogeCV.

Official Tutorials coming with the 2018.3 Update. (Est Sep 20th)
 - [Wizards.exe Tutorial](https://www.youtube.com/watch?v=_i-EU3v9CRc)


## Development Team
- Alex Carter FTC 7195 Mechanical Memes
- Levi Gershon FTC 12897 Newton's Law of Mass'
- Brandon FTC 8565 Technicbots

## Other Contributors
- Karter FTC 5975 Cybots (Brainstorming for Jewel Detector)
- Derek FTC 5484 Enderbots (EnderCV Classes)
- Owen Gonzalez (Testing)
- Kwon Paradigm Break (Testing)

## Known Issues
(These issues are referring to the 2018-2019 FTC Year Detectors, and not the Relic Recovery ones as those are no longer supported)
- General Instablity
- GenericDetector not ported
- Vuforia is rotated in portrait mode
- RoverRukas Package typo.

## Planned Features / TODO
- Fix Vuforia
- Add in distance scoring between samples to increase accuracy
- General Code Clean up
- Update Wiki
- Basic angle/positioning of elements
- Example Autons using DogeCV
- Port Old detectors to new systems
- Better Safety checks
- Move to our own OpenCV System
- Add to JCenter
- New Frame Input System
- ML detectors


## Install (Credit to EnderCV)
1. Download this repo, either by cloning from Git or using the zip download. 
2. Pull up Android Studio, with the FTC application SDK open
3. Navigate to **File** -> **New** -> **Import Module** from the title bar.
4. When the a dialog comes up, asking for the module source directory, navigate to this repo and select the **openCVLibrary3** folder, and then hit **Finish**
5. Repeat steps 3 and 4 except instead of selecting the **openCVLibrary3** folder, select the **DogeCV** folder instead. If Android Studio fails to import modules because it sucks sometimes, open `settings.gradle` and add these two lines: 
```
include ':openCVLibrary3'
include ':DogeCV'
```
and resync the project.    
6. In the left hand side project explorer in Android Studio, right-click **TeamCode**, and click on **Open Module Settings**.    
7. A **Project Structure** dialog should come up. Click the **Dependencies** tab.     
8. Click the green plus sign on the right hand side, then **Module dependency**, and then **:openCVLibrary3**, then press OK.     
9. Repeat step 8, except substitute **:openCVLibrary3** with **:DogeCV**.   
10. Click **OK** to exit the **Project Structure** dialog.    


# Detectors Status
- **Gold Align Detector** - Implemented. Unstable (Memory Leak)
- **Sampling Detector** - Implemented. Stable. Not Competition Ready.
- **Gold Detector** - Implemented Base (Needs more).Stable. Not Competition Ready.
- **Silver Detector** - Implemented Base (Needs more).Stable. Not Competition Ready.
- **MultiMineral Detector** - Not Yet Implemented
**See Wiki For More Info

## FAQ
- **If I use dogeCV can I still use Vuforia?**
    With the 2018 edition of DogeCV we have enabled the option to use Vuforia and DogeCV at the same time, while using the same camera and viewing both data on the RC at the same time! This is done by using DogeCV's Vuforia class `DogeForia`. See more in the wiki.
    (Current Unstable)
- **Can I still use classic OpenCV?**
    Yes! We want teams to use DogeCV to learn about vision and start to create their own vision systems. That's why OpenCV is open in all layers of DogeCV, and we keep the classic OpenCVPipepline introduced in the EnderCV lib.
- **`Dogeforia` is getting import errors:**
    This is a current bug that I am investigating, for right now simply move the Dogeforia class to your `TeamCode` package.


## Changelogs
**2018.2**:
 - Added `HoughSilverDetector`: intended for slighter slower but more precise silver mineral classification. Works nicely.
 - Added JavaDocs and in-depths comments on most detectors, scorers, and filters
 - Changed `DogeCVScorer` input from MatOfPoint to Mat in order to facilitate more diverse scoring methods
 - Detectors now have a seperate `displayMat` as opposed to a `workingMat`; one is for detection work, the other is for displaying the results
 - Added `SilverExample` OpMode for demonstrating silver detector use
 - Added `HoughSilverExample` OpMode for demonstrating Hough transform-based silver detector use
 - Gold Align Detector now has `setAlignSettings(int offset, int width)` function to set parameters
 - Updated `SamplingOrderDetector` to choose top two silver minerals instead of all
 - Ported `GenericDetector` to using `DogeCVDetector`
 - Ported `BlankDetector` to using `DogeCVDetector`
 - Added `updateSettings` to `LeviColorFilter`
 - Corrected typos within this `README`
 - Added an experimental `WHITE` option to `LeviColorFilter`
 - Cleaned up detectors and filters

**2018.1**:
 - Added `HSVRangeFilter` that uses classic `lower` and `upper` HSV ranges
 - `HSVColorFilter` now devides the range by 2 on each side of the perfect color value. (Now range acts as expected) (Issue #9)
 - Added `SilverDetector` (Same as Gold but for silver)
 - Moved `SamplingOrderDetector` to use `HSVRangeFilter` for silver
 - Tuned Sampling (Still not competition ready)
 - Cleaned up `Dogeforia` handling per suggestions of "@BillTheCat123 | Mentor | 3763" on FTC Discord
 - Fixed Gradle import issues of `:FTCRobotController` with `Dogeforia` class. (Issue #8)
 - Cleaned up Gradle Build files to lessen import errors
 - Added common Gradle Build errors to wiki
 - `DogeCVDetector` now handles printing the detector setting to the screen

**2018.0**:
 - New Versioning System
 - New Scoring API
 - New DogeCVDetector Class
 - Vuforia Support
 - Gold Align, Mineral Order, and Gold Detectors
 - General Code Cleanup
 - Cleaner Params
 - Moved Downscaling to DogeCVDetector Class
 - Updated to EnderCV 2.0 (Modfied)
 
**1.1.1 HOTFIX**:
 - Fixed Jewel Detector Blue Filter
 

**1.1**:
- New Color Filter API   
- New Generic Detector     
- Fixed Jewel Debug Scores    
- Fixed Imports for DogeLogger inside Cryptobox Detector   
- Ported all detectors to Color Filter API     
- Added Yellow to LeviColorFilter
- Added HSV color filter
- New Relic/Generic Example
 
**1.0**:
 - New Cryptobox Detector
 - YouTube Tutorials
 - Per-Detector Documentation
 - Wiki Start 
 - Added `perfectRatio` tuning for Jewels
 - Optimization
 - Removed Multiple Mat returning

**0.5**:
 - Fixed rotated preview on protrait mode.
 - Detectors return an array of images. You can cycle through them by tapping on the preview screen
## Contact
If you have any suggestions or questions feel free to contact us at:    
**VictoryForPhil@gmail.com**
or 
**VictoryForPhil#4759 or Levi -12897** on Discord

You can also usually spot us on the FTC Discord.
