
mountHeight=128;
holeShift=mountHeight/2 - 16;
holeDepth = 6;
holeOffset = 8;
holeRadius = 2;
overlapLength=30;


mountWidth=20;

wallThickness = 1;


sqrt2 = sqrt(2);

edgeLength = 15;
innerLength = edgeLength+wallThickness*sqrt2;
slantSide = 6;

gridWidth=59;
barWidth = gridWidth/2 + mountWidth;

innerSquareWidth = gridWidth * 59;
braceLength=gridWidth*sqrt(2)/2+2;

barOffset=13.2;

holeFacets = 144;

Half();
scale([-1,1,1])
    Half();

//cube([gridWidth,5,15],center=true);
//translate([gridWidth/2,0,0])//cube([30,edgeLength,4]);

//translate([gridWidth/2+slantSide,0+slantSide,0])
//cube([30,edgeLength,2]);

module Half() {
    union() {
        difference() {
            union() {
                

                translate([barOffset, -wallThickness, -mountHeight/2])
                cube([barWidth-barOffset, wallThickness, mountHeight]);
                
                translate([gridWidth/2-wallThickness,0,-mountHeight/2])
                cube([wallThickness, innerLength-wallThickness, mountHeight]);
                
                translate([0,-(gridWidth/2-innerLength),0])
                rotate([0,0,45])
                translate([-wallThickness,-wallThickness,-mountHeight/2])
                cube([52.205, wallThickness, mountHeight]);
            }
            
            translate([0,slantSide+edgeLength+wallThickness,0])
            translate([gridWidth/2,0,-(mountHeight+2)/2])
                cube([100,edgeLength, mountHeight+2]);
            
            translate([(32+gridWidth)/2,0,holeShift])
                rotate([90,0,0])
                    Holes(right=false,left=false);
            
            translate([(32+gridWidth)/2,0,holeShift-32])
                rotate([90,0,0])
                    Holes(top=false,bottom=false,left=false);
            
            translate([(32+gridWidth)/2,0,holeShift-64])
                rotate([90,0,0])
                    Holes(right=false,left=false); 
            
            translate([(32+gridWidth)/2,0,holeShift-96])
                rotate([90,0,0])
                    Holes(top=false,bottom=false,left=false);
        }
    
        translate([gridWidth/2+slantSide,edgeLength+slantSide,-mountHeight/2])
        cube([overlapLength,wallThickness, mountHeight]);
    }
}


module Holes(top=true,left=true,right=true,bottom=true) {
    if (left) {
        translate([holeOffset,0,0])
            cylinder(r=holeRadius,h=holeDepth,
                     center=true, $fn=holeFacets); }
    if (right) {
        translate([-holeOffset,0,0])
            cylinder(r=holeRadius,h=holeDepth,
                     center=true, $fn=holeFacets); }
    if (bottom) {            
        translate([0,holeOffset,0])
            cylinder(r=holeRadius,h=holeDepth,
                     center=true, $fn=holeFacets); }
    if (top) {
        translate([0,-holeOffset,0])
            cylinder(r=holeRadius,h=holeDepth,
                     center=true, $fn=holeFacets); }
}
