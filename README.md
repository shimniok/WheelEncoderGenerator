# Wheel Encoder Generator
*Design and print wheel encoder discs for robotics and more. Cross platform, 
open source!*


## Introduction
WheelEncoderGenerator is a cross-platform desktop JavaFX app used to design and
print wheel encoder discs for use in robotics.

It supports printing of quadrature, binary, gray-code binary, and basic 
single-track encoder disc patterns with resolutions from 1 bit (2 positions) to
11 bit (2048 positions).  You can adjust outer diameter of the encoder disc,
as well as minimum diameter of the innermost track, and the diameter of the
axle/shaft hole of the encoder.  An index track with a single stripe can be
enabled on quadrature and basic discs. The disc can also be inverted, exchange
foreground and background colors. Finally, you can adjust the encoder for
clockwise or counter-clockwise rotation.

The encoder disc settings can be saved and loaded and, of course, the final
design can be printed from the application.

## Directions
 * Download the archive file corresponding to your operating system family
 * Extract archive to a location of your choosing
 * Open the extracted folder and open the ```bin``` folder/directory
 * Linux/Mac: open/run the file ```weg```
 * Windows: open/run the file ```weg.bat```

## Feature List
 * Supports Mac, Windows, Linux with completely self-contained application
 * Design wheel coder with adjustable encoder tracks' outer and inner diameters, and axle/shaft diameter
 * Print encoder design to a printer
 * Save or Open configuration files or create new encoder.
 * Four encoder types: basic, quadrature, binary, and gray-coded binary
 * Index track available on quadrature and simple encoders (n/a for binary or gray encoders)
 * Adjust resolution up to 2048 positions (11-bit)
 * Invert color scheme (black on white vs white on black)
 * Clockwise or Counter-clockwise rotation (n/a for simple encoder)

## Original Application

The original Java Swing application repository can be found
[here](https://code.google.com/archive/p/wheel-encoder-generator/).

