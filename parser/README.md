# Prerequisites

- cmake (3.0 or above)
- make
- clang / gcc (With C++20 support)

The project uses the `gif-h` GIF library (third party, bundled with the project).

https://github.com/charlietangora/gif-h

Ubuntu:
`sudo apt install build-essential cmake g++`
Arch:
`sudo pacman -Sy  make cmake g++`
Windows:
Install all necessary prerequisites manually.

## Build steps

1. `mkdir build && cd build && cmake ..`
2. `make`

## Run steps
Print creator and creation date of a CAFF video file and validate the file:

`./parser ../examples/3.caff`

Generate preview segment (specifying the start frame and the length of the segment):

`./parser ../examples/3.caff output.gif`

## How it works

The parser module dives into the contents of CAFF (CrySyS Animated File Format) files, verifying the file and extracting infromation from it, including:
- The Author of the CIFF file.
- The time of creation of the CIFF file.
- The resolution of the CIFF file.

The parser verifies whether the CAFF file is valid according to the specification of the CAFF video file format.
The parser marks CAFF video files as invalid if CIFF frame sizes vary during the animation.

When used as a component for a web application, the parser may write gif files to a randomized path enabling parallel serving of multiple requests. This however is up to the backend development team.

For sample outputs including logs and GIF files, please refer to the SampleParserOutputs wiki page of the project.
