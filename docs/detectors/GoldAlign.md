![]()
**STATUS:** _NOT COMPETETION READY (Needs Accuracy Improvements)_   
**LAST UPDATED:** _9/22/2018 DogeCV 2018.0_

# About
This detector is also made for the auton sampling section of the game, however this detector only requires the gold to be visable as it returns if the phone is aligned with the gold mineral.

### Todo
- Idk its pretty neato, ig ill tune it /shrug

### Known Issues
- Somehow works, could use tuning tho
### Credits

# Details
This detector uses a yellow LeviColorFilter to locate the gold mineral, then checks if the mineral's x positiob is with in a margin relative to the center of the screen.

# Usage
Find the location of the bot compare to the mineral in which you want to score the mineral, then tune the margins and position of the detector to match, after that all you you need to in auton is drive until the detector reports that the bot is aligned, then score.

## Scorers (See ScoringAPI for tuning each.)
- `ratioScorer` - a `RatioScorer` to find perfect squares.
- `maxAreaScorer` - a `MaxAreaScorer` to find perfect large objects
- `perfectAreaScorer` - a `PerfectAreaScorer` to find tuned size objects.
**NOTE:** In this detector, setting `areaScoringMethod` will determine which area scorer to use. 

## Parameters
- `areaScoringMethod` - (`DogeCV.AreaScoringMethod `) - an enum to determine which area scorer to use. (`MAX_AREA` or `PERFECT_AREA`)
- `ratioScorer` - **SEE ABOVE**
- `maxAreaScorer` - **SEE ABOVE**
- `perfectAreaScorer` -  **SEE ABOVE**
- `yellowFilter` - (`DogeCVColorFilter `) - Filter used to find gold (Defaults to `LeviColorFilter(LeviColorFilter.ColorPreset.YELLOW,70)`)
- `alignPosOffset`- (`double`) - Offset from the center of the screen to which to base the align margin. 
- `alignSize`- (`double`) - How large around the alignPos is the margin

## Returned Data
Currently this detector returns the following:
- `isFound()` - Is the mineral row detected?
- `getAligned()` - Is the mineral within the margins (aligned)
- `getXPosition()` -  Get the current x position of the mineral

# Changelogs
- **2018.0**
  - First Public Release.