mountLength = 78;
mountWidth = 28;

braceHolesOffset = -51;

railOffset = 42;
railWidth = 24;
supportWidth = railOffset+railWidth/2+(mountWidth/2);
supportLength = mountLength + 12 - braceHolesOffset;
supportThickness = 2;

bracePadding = 0;
braceOffset = 16;
braceThickness = 2;

maxReinforcingThickness = 8;
minReinforcingThickness = 2;

webThickness = 1;
webDepth = 8;


topHolesOffset = 0;
bottomHolesOffset = 64;

braceLength = mountWidth/2 - braceHolesOffset;

holeDepth = 40;
holeOffset = 8;
holeRadius = 2;


bottomGuideOffset = mountLength-9;
middleGuideOffset= bottomGuideOffset-64;

bottomHoleRadius = 4.5;

holeOffset = 8;
holeRadius = 2.1;

holeFacets = 144;
rotate([0,90,0])
difference() {
    union() {
      
        color("red")
        translate([-mountWidth/2,mountLength-supportLength,-supportThickness])
        cube([supportWidth,supportLength,supportThickness]);

        
        translate([railOffset,
                   middleGuideOffset,0])
            Guide();

        translate([railOffset,
                   middleGuideOffset+braceThickness/2,
                   0])
            Reinforcement();

        translate([railOffset,
                   bottomGuideOffset+braceThickness/2,
                   0])
                 Reinforcement();
        
        translate([railOffset,bottomGuideOffset,0])
            Guide();
  
    }

    translate([railOffset, braceHolesOffset,0])
        Holes();
    translate([railOffset-20, braceHolesOffset,0])
        Holes(left=false);
    translate([0, -32, 0])
        Holes();
    translate([0, topHolesOffset, 0])
        Holes();
    translate([0, 32, 0])
        Holes();
    translate([0,
               bottomHolesOffset, 0])
        Holes();
}

module Guide() {
    union() {
        difference() {
            union() {
                translate([-railWidth/2,-braceThickness/2,0])
                    cube([railWidth,
                          braceThickness,
                          bracePadding+braceOffset]);
                
                translate([0,
                           braceThickness/2,
                           bracePadding + braceOffset])
                    rotate([90,0,0])
                        scale([1,.75,1])
                            cylinder(r=railWidth/2,
                                     h=braceThickness,
                                     $fn=holeFacets);

            }
            translate([0,0,bracePadding + braceOffset])
                rotate([90,0,0])
                    cylinder(r=bottomHoleRadius,h=8,
                        center=true, $fn=holeFacets); 
        }
       // translate([0,0,bracePadding + braceOffset]) 
         //   rotate([0,45,0])
           // cube([bottomHoleRadius*1.65,0.5,bottomHoleRadius*1.65], center=true);
    }
        
}

module Reinforcement() {
    color("green")
    translate([railWidth/2,0,0])
    rotate([0,-90,0]) {
    linear_extrude(height=railWidth/2,
                   scale=minReinforcingThickness/
                         maxReinforcingThickness)
      polygon(points=[[-.1,-.1],
                      [maxReinforcingThickness,-.1],
                      [-.1,maxReinforcingThickness]]);

    translate([0,0,railWidth/2])
        linear_extrude(height=railWidth/2,
                       scale=maxReinforcingThickness/
                             minReinforcingThickness)
            polygon(points=[[-.1,-.1],
                            [minReinforcingThickness,-.1],
                            [-.1,minReinforcingThickness]]);

    }
}

module Holes(left=true,right=true) {
    if (left) {
    translate([holeOffset,0,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets); }
    if (right) {
    translate([-holeOffset,0,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets); }
    translate([0,holeOffset,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([0,-holeOffset,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
}
