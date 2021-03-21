# WheelEncoderGenerator

## v0.3.2 Release Notes

*Feature enhancement and bugfixes for WheelEncoderGenerator 0.3*

### Fixes and Enhancements
 * Added help button accessing online help website
 * When changing units diameter dimensions are automatically converted to the new units (e.g. 1.000 inch -> 25.4 mm)
 * Changing units also changes the number of significant digits displayed (#.### for inch, #.# for mm)
 * Fixed input validation for diameter fields
 * Fixed input validation and formatting for resolution spinner
 * Errors in dimensions are indicated and saving prevented along with displaying a warning dialog
 * Fixed window resize layout bug
 * Added About screen
 * Improved load time for help window
 * Fixed bug with multiple windows not closing on exit

### Known Issues
 * Image export not yet implemented

## v0.3.1 Release Notes

*Minor update of WheelEncoderGenerator 0.3*

### Fixes and Enhancements
 * Prompt to save changes upon New, Open, Exit
 * Added saved/unsaved status indicator
 * Added tooltips
 * Inverted button toggles between "Yes" and "No" text
 * Implemented check for application updates 

### Known Issues
 * Issues with input formatting and validation on some fields
 * Image export not yet implemented
 * No online help available
 * No "About..." screen
 * No menu bar; all functionality available through toolbar, however

## v0.3-Beta Release Notes

*Initial MVP release of WheelEncoderGenerator 0.3 with most of the basic functionality implemented*

### Features
 * Supports Mac, Windows, Linux with self-contained application
 * Design wheel coder with adjustable diameters for: outer track, inner track, axle/shaft
 * Print design to a printer
 * Save or Open configuration files or create new.
 * Four encoder types: basic, quadrature, binary, and gray-coded binary
 * Index track available on quadrature and simple encoders (n/a for binary or gray encoders)
 * Adjust resolution to 2048 (11-bit) positions
 * Invert color scheme (black on white vs white on black)
 * Clockwise or Counter-clockwise rotation (n/a for simple encoder)

### Directions
 * Download file corresponding to your operating system familyo
 * Extract archive to a location of your choosing
 * Open the extracted folder and open the ```bin``` folder/directory
 * Linux/Mac: open/run the file ```WEG```
 * Windows: open/run the file ```WEG.bat```

### Known Issues
 * Image export not yet implemented
 * No menu bar; all functionality available through toolbar, however
 * Few or no tooltips or help available
 * Inverted button does not toggle between "Yes" and "No" text
 * Issues with input formatting and validation on some fields
 * No indication of saved/changed status
 * No prompt to save changes upon New or Exit
 * No automatic check for updates
 * No "About..." screen
