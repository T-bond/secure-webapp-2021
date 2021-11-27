cmake_minimum_required(VERSION 3.0)
project(parser)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_FLAGS "-fstack-protector-all")

find_package(JNI REQUIRED)

include_directories("include")
include_directories("lib/gif-h")
include_directories(${JNI_INCLUDE_DIRS})
add_executable(parser_demo main.cpp src/parser/CIFF.cpp include/parser/CIFF.hpp include/parser/Color.hpp src/parser/CAFF.cpp include/parser/CAFF.hpp)
add_library(parser SHARED src/parser/CIFF.cpp include/parser/CIFF.hpp include/parser/Color.hpp src/parser/CAFF.cpp include/parser/CAFF.hpp include/JNI.h src/JNI.cpp)
