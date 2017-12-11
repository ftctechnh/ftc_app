supportLength = 78;
supportWidth = 30;

supportThickness = 2;

mountSeparation = 52;
mountWidth = 7;
mountDepth = 4;
mountSpacing = 20;

shaftOffset = 15;//11.5;

bracePadding = 10;
braceDepth = 30;
braceThickness = 3;


maxReinforcingThickness = 8;
minReinforcingThickness = 2;

webThickness = 1;
webDepth = 8;

braceOffset = shaftOffset + (mountWidth - supportWidth)/2;
braceLength = mountSeparation + mountWidth;

topHolesOffset = 20;
bottomHolesOffset = 60;

holeDepth = 8;
holeOffset = 8;
holeRadius = 2;

bigHoleDepth = 1.5;
bigHoleRadius = 4.75;

bottomBraceThickness=2;
bottomHoleRadius = 4;

holeOffset = 8;
holeRadius = 2;

holeFacets = 48;


difference() {
    union() {
        cube([supportWidth,supportLength,supportThickness]);
        translate([supportWidth - braceLength + braceOffset,0,0])
            cube([braceLength,braceThickness,
                  braceDepth+bracePadding]);
        translate([0,supportLength,0])
            cube([supportWidth,
                  bottomBraceThickness,
                  bracePadding+braceDepth/2]);
        
        translate([0,0,supportThickness])
            cube([webThickness, supportLength, webDepth]);
        translate([supportWidth-webThickness,0,supportThickness])
            cube([webThickness, supportLength, webDepth]);
        translate([supportWidth - mountWidth + braceOffset,
                  -mountDepth,0])
            cube([mountWidth,mountDepth,braceDepth+bracePadding]);
        translate([supportWidth-braceLength + braceOffset,
                  -mountDepth,0])
            cube([mountWidth,mountDepth,braceDepth+bracePadding]);


        translate([supportWidth,braceThickness,supportThickness])
            Reinforcement();
        
        translate([supportWidth,
                   supportLength,supportThickness])
            rotate([90,0,0])
                 Reinforcement();
        
        translate([supportWidth/2,
                   supportLength+bottomBraceThickness,
                   bracePadding + braceDepth/2])
            rotate([90,0,0])
                cylinder(r=supportWidth/2,h=bottomBraceThickness,
                         $fn=holeFacets); 
  
    }

    translate([supportWidth/2, topHolesOffset, 0])
        Holes();
    translate([supportWidth/2,
               bottomHolesOffset, 0])
        Holes();
    translate([supportWidth-braceLength +
               mountWidth/2 + braceOffset,
               0,bracePadding + braceDepth/2])
        MountHoles();
    translate([supportWidth - mountWidth +
               mountWidth/2 + braceOffset,
               0,bracePadding + braceDepth/2])
        MountHoles();
    
    translate([supportWidth/2,0,bracePadding + braceDepth/2])
        rotate([90,0,0])
            cylinder(r=bigHoleRadius,h=16,
                     center=true, $fn=holeFacets);
    
    translate([supportWidth/2,
              supportLength,bracePadding + braceDepth/2])
        rotate([90,0,0])
            cylinder(r=bottomHoleRadius,h=16,
                     center=true, $fn=holeFacets); 
}

module Reinforcement() {
    color("green")
    rotate([0,-90,0]) {
    linear_extrude(height=supportWidth/2,
                   scale=minReinforcingThickness/
                         maxReinforcingThickness)
      polygon(points=[[-.1,-.1],
                      [maxReinforcingThickness,-.1],
                      [-.1,maxReinforcingThickness]]);

    translate([0,0,supportWidth/2])
        linear_extrude(height=supportWidth/2,
                       scale=maxReinforcingThickness/
                             minReinforcingThickness)
            polygon(points=[[-.1,-.1],
                            [minReinforcingThickness,-.1],
                            [-.1,minReinforcingThickness]]);

    }
}

module MountHoles() {
    rotate([90,0,0])
        translate([0,-mountSpacing/2,0]) {
        cylinder(r=holeRadius,h=16,
                 center=true, $fn=holeFacets); 
        translate([0,mountSpacing,0])
            cylinder(r=holeRadius,h=16,
                 center=true, $fn=holeFacets); 
    }
}

module Holes() {
    translate([holeOffset,0,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([-holeOffset,0,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([0,holeOffset,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([0,-holeOffset,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
}
