# Wheel Encoder Generator
*Design and print wheel encoder discs for robotics and more. Cross platform, open source!*

WheelEncoderGenerator is a cross-platform desktop Java app used to design and print wheel encoder discs for use in robotics.

It supports printing of quadrature, binary, gray-code binary, and basic single-track encoder disc patterns with resolutions 
from 1 bit (2 positions) to 11 bit (2048 positions).  You can adjust outer diameter of the encoder disc, as well as minimum 
diameter of the innermost track, and the diameter of the axle/shaft hole of the encoder.  An index track with a single 
stripe can be enabled on quadrature and basic discs. The disc can also be inverted, exchange foreground and background colors.
Finally, you can adjust the encoder for clockwise or counter-clockwise rotation.

The encoder disc settings can be saved and loaded and, of course, the final design can be printed from the application.

## Task List - updated Feb 28, 2021

Stay tuned for MVP release by Mar 1. See branch [0.3](https://github.com/shimniok/WheelEncoderGenerator/tree/0.3) 

 * ~~Initial view created~~
 * ~~Model created~~
 * ~~Prototype Controller implemented~~
 * ~~Bind model and view~~
 * ~~Render encoder disc~~
 * ~~Implement printing~~
 * ~~Implement file operations (new/open/save/save as)~~
 * ~~Implement unit tests~~
 * Implement image export
 * Implement Linux packaging
 * Implement Windows packaging
 * Implement MacOS packaging

## Update - Feb 18, 2021

Basic functionality is implemented -- configuring encoder, printing, saving, loading, starting new.

[original application](https://code.google.com/archive/p/wheel-encoder-generator/)
