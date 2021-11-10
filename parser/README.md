## Prerequisites

- cmake (3.0 or above)
- make
- clang / gcc (With C++17 support)

The project uses the `gif-h` GIF library (third party, bundled with the project).

https://github.com/charlietangora/gif-h

Ubuntu:
`sudo apt install build-essential cmake g++`
Arch:
`sudo pacman -Sy  make cmake g++`
Windows:
Install all necessary prerequisites manually.

# Build steps

1. `mkdir build && cd build && cmake ..`
2. `make`

# Run steps
Print creator and creation date of a CAFF video file and validate the file:

`./parser ../examples/in/3.caff`

Generate preview segment (specifying the start frame and the length of the segment):

`./parser ../examples/in/3.caff output.gif`

# How it works

The parser module dives into the contents of CAFF (CrySyS Animated File Format) files, verifying the file and extracting infromation from it, including:
- The Author of the CIFF file.
- The time of creation of the CIFF file.
- The resolution of the CIFF file.

The parser verifies whether the CAFF file is valid according to the specification of the CAFF video file format.
The parser marks CAFF video files as invalid if CIFF frame sizes vary during the animation.

When used as a component for a web application, the parser may write gif files to a randomized path enabling parallel serving of multiple requests. This however is up to the backend development team.

For sample outputs including logs and GIF files, please refer to the SampleParserOutputs wiki page of the project.

# Project structure

- `.idea/`: CLion project files.
- `doc/`: Any documentation files not featured in the root directory of the parser for a quick review (contains the official CIFF and CAFF specifications).
- `examples/`: sample CIFF inputs and GIF outputs
- `lib/`: external libraries (third party)
- `include/`: parser libraryheader files
- `src/`: source files
- `main.cpp`: a short demo program validating a CAFF file and transforming it into a GIF file (see the "Run steps" section for usage details)
- `CMakeLists.txt`: cmake project file
- `README.md, SampleOutputs.md`: readme file + sample outputs on the test CAFF files
- `.gitignore`

# Source and header files

### CIFF parser
`include/parser/CIFF.hpp, include/parser/CIFF.cpp`

CIFF image parser, capable of extracting the following data from a CIFF image:
- image width
- image height
- caption
- image tags
- image data

In addition, the parser can validate CIFF image content.

### CAFF parser
`include/parser/CAFF.hpp, include/parser/CAFF.cpp`

CAFF video parser, capable of extracting the following data from a CAFF video file:
- creator
- creation date
- widtht of the first CIFF image contained in the CAFF file
- height of the first CIFF image contained in the CAFF file

In addition, the parser can validate CAFF video content.
Using the CAFF parser, it is possible to generate GIF images from CAFF video files.
The CAFF parser uses the CIFF parser to parse extract necessary information from CIFF image frames belonging to the CAFF video file.

### Color parser
`include/parser/Color.hpp`

Color data class capable of storing a single pixel as RGBA values.

### Main program
`main.cpp`

A short program performing the following (using the CAFF parser):
- Open and validate the CAFF file received as the first command line argument. The parser exits if the CAFF file is incorrect.
- Print the following information about the CAFF file (see the `SampleOutputs.md` file in this folder):
  - author (=creator)
  - date of creation
  - resolution (width x height, in pixels)
  - confirmation of validity
- Write the parsed video content as an animated GIF file to the file path specified by the second command line argument.
