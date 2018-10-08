

<div align="center">
    <img src="https://i.imgur.com/sjEURcc.png" width="100%"/>
    <br></br>
  <p>
    <a href="https://discord.gg/colton"><img src="https://discordapp.com/api/guilds/345404637374971907/embed.png" alt="Discord server" /></a>
  </p>
  <b>Created by Alex Carter of Disnode Robotics</b>
    <br/>
     <i>Version 2018.0 (UNRELEASED) | Updated 9/19/2018 </i>

</div>

# DogeCV
A easy to use computer vision library used for FTC Games to detect game objects. Based on Ender CV and OpenCV. 

# Docs
[Detectors](/detectors)
[ScoringAPI](/ScoringAPI)
[ColorFilterAPI](/ColorFilterAPI)

## Project Status:
**IN DEVELOPMENT. ALPHA RELEASE COMING SOON. NOT COMPETETION READY**

# DISCLAIMER
### THIS REPO IS STILL UNDER HEAVY DEVELOPMENT. I WILL BE ADDING FURTHER DOCUMENTATION, BUG FIXES AND NEW DETECTORS SOON. MANY OF THE 
However, although many of the detectors are currently pretty basic, I am putting alot of time in effort into this lib, and open sourced it to let the community work or learn from my mistakes. This is the exact code my team will be running so I do have a decent motivation to work on it ;)



## Credits
- Levi 8148 AlephBots (Amazing color filtering and Cryptobox Detector)
- Karter FTC 5975 Cybots (Brainstorming for Jewel Detector)
- Derek FTC 5484 Enderbots (EnderCV Classes)
- Owen Gonzalez (Testing)

## Known Issues
(These issues are referring to the 2018-2019 FTC Year Detectors, and not the Relic Recovery ones as those are no longer supported)
- Vuforia Mode Unstable (Crashes alot!)
- Memeory Leak in Gold Align Detectors
- General Unstablity

## Planned Features / TODO
- Fix Vuforia
- Add in distance scoring between samples to increase accuracy
- General Code Clean up
- Add back in Perfect Area Scoring
- Update Wiki
- Basic angle/positioning of elements
- Example Autons using DogeCV
- Generic Silver Detector


## Install (Credit to EnderCV)
1. Download this repo, either by cloning from Git or using the zip download. 
2. Open up your FTC Application
3. Navigate to **File** -> **New** -> **Import Module** from the title bar.
4. When the a dialog comes up, asking for the module source directory, navigate to this repo and select the **openCVLibrary320** folder, and then hit **Finish**
5. Repeat steps 3 and 4 except instead of selecting the **openCVLibrary320** folder, select the **DogeCV** folder instead.
6. In the left hand side project explorer in Android Studio, right-click **TeamCode**, and click on **Open Module Settings**.
7. A **Project Struture** dialog should come up. Click the **Dependencies** tab.
8. Click the green plus sign on the right hand side, then **Module dependency**, and then **:openCVLibrary320**, then press OK.
9. Repeat step 8, except substitute **:openCVLibrary320** with **:dogecv**
10. Click **OK** to exit the **Project Structure** dialog.


# Detectors Status
- **Gold Align Detecotr** - Implemented. Unstable (Memory Leak)
- **Sampling Detecotr** - Implemented. Stable. Not Competetion Ready.
- **Gold Detector** - Implemented Base (Needs more).Stable. Not Cmpetetion Ready.
- **Silver Detector** - Not Yet Implemented
- **MultiMineral Detector** - Not Yet Implemented
**See Wiki For More Info

## FAQ
- **If I use dogeCV can I still use Vuforia?**
    With the 2018 edition of DogeCV we have enabled the option to use Vuforia and DogeCV at the same time, while using the same camera nad viewing both data on the RC at the same time! This is done by using DogeCV's Vuforia class `DogeForia`. See more in the wiki.
    (Current Unstable)
- **Can I still use classic OpenCV**
    Yes! We want teams to use DogeCV to learn about vision and start to create their own vision systems. That's why OpenCV is open in all layers of DogeCV, and we keep the classic OpenCVPipepline introduced in the EnderCV lib.


## Changelogs
**2018.0 (UNRELEASED)**:
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
 - Youtube Tutorials
 - Per-Detector Documentation
 - Wiki Start 
 - Added `perfectRatio` tuning for Jewels
 - Optimiziation
 - Removed Multiple Mat returning

**0.5**:
 - Fixed rotated preview on protrait mode.
 - Detectors return an array of images. You can cycle through them by tapping on the preview screen
## Contact
If you have any suggestions or questions feel free to contact me at:    
**VictoryForPhil@gmail.com**
or 
**VictoryForPhil#4759** on Discord

You can also usually spot me on the FTC Discord.
