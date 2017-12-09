
outerRadius = 27.5;
innerRadius = 26.0;
innerThickness = 1.0;
outerThickness = 2.0;
holeRadius = 2.05;
holeOffset = 8;
bigHoleRadius = 4;
sliderDepth = 4;
holeFacets = 16;

difference() {
    union() {
        translate([0,0,innerThickness ])
            cylinder(r = outerRadius,
                     h = outerThickness,
                     $fn=holeFacets);
                     
        linear_extrude(height=innerThickness,
                       scale = outerRadius/innerRadius)
           circle(r = innerRadius, $fn=holeFacets);
    }
    Holes();
    translate([0,0,innerThickness])
        cylinder(r = innerRadius-5,
                 h = outerThickness,
                 $fn=holeFacets);
}


module Holes() {
    cylinder(r=bigHoleRadius,h=2*sliderDepth, center=true, $fn=holeFacets);
    translate([holeOffset,0,0])
        cylinder(r=holeRadius,h=2*sliderDepth, center=true, $fn=holeFacets);
    translate([-holeOffset,0,0])
        cylinder(r=holeRadius,h=2*sliderDepth, center=true, $fn=holeFacets);
    translate([0,holeOffset,0])
        cylinder(r=holeRadius,h=2*sliderDepth, center=true, $fn=holeFacets);
    translate([0,-holeOffset,0])
        cylinder(r=holeRadius,h=2*sliderDepth, center=true, $fn=holeFacets);
}