cmake_minimum_required(VERSION 3.0)
project(backend)

set(CMAKE_CXX_STANDARD 20)

include_directories("include")

add_executable(backend main.cpp src/parser/CIFF.cpp include/parser/CIFF.hpp include/parser/Color.hpp src/parser/CAFF.cpp include/parser/CAFF.hpp)
