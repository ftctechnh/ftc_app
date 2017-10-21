supportLength = 78;
supportWidth = 30;

supportThickness = 2;

braceLength = 50;
bracePadding = 10;
braceDepth = 30;
braceThickness = 2;

mountWidth = 7;
mountDepth = 4;
mountSpacing = 20;

maxReinforcingThickness = 8;
minReinforcingThickness = 2;

tabWidth = 8;
tabGapWidth = 3;
tabRadius = 200.0;
tabDepth = 4;

bigHoleOffset = 15;


topHolesOffset = 20;
bottomHolesOffset = 60;

holeDepth = 8;
holeOffset = 8;
holeRadius = 2;

bigHoleDepth = 1.5;
bigHoleRadius = 4.75;
bottomHoleRadius = 3;

holeOffset = 8;
holeRadius = 2;

holeFacets = 24;


difference() {
    union() {
        cube([supportWidth,supportLength,supportThickness]);
        translate([supportWidth - braceLength,0,0])
            cube([braceLength,braceThickness,
                  braceDepth+bracePadding]);
        translate([0,supportLength,0])
            cube([supportWidth,
                  braceThickness,
                  bracePadding+braceDepth]);
        translate([supportWidth - mountWidth,-mountDepth,0])
            cube([mountWidth,mountDepth,braceDepth+bracePadding]);
        translate([supportWidth-braceLength,-mountDepth,0])
            cube([mountWidth,mountDepth,braceDepth+bracePadding]);

        translate([supportWidth,braceThickness,supportThickness])
            Reinforcement();
        
        translate([supportWidth,
                   supportLength,supportThickness])
            rotate([90,0,0])
                 Reinforcement();
  
    }

    translate([supportWidth/2, topHolesOffset, 0])
        Holes();
    translate([supportWidth/2,
               bottomHolesOffset, 0])
        Holes();
    translate([supportWidth-braceLength + mountWidth/2,
               0,bracePadding + braceDepth/2])
        MountHoles();
    translate([supportWidth - mountWidth + mountWidth/2,
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
