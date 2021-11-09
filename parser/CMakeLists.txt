cmake_minimum_required(VERSION 3.0)
project(parser)

set(CMAKE_CXX_STANDARD 20)

include_directories("include")

add_executable(parser main.cpp src/parser/CIFF.cpp include/parser/CIFF.hpp include/parser/Color.hpp src/parser/CAFF.cpp include/parser/CAFF.hpp)

add_definitions( -DMAGICKCORE_QUANTUM_DEPTH=16 )
add_definitions( -DMAGICKCORE_HDRI_ENABLE=0 )
find_package(ImageMagick COMPONENTS Magick++)
include_directories(${ImageMagick_INCLUDE_DIRS})
target_link_libraries(parser ${ImageMagick_LIBRARIES})

