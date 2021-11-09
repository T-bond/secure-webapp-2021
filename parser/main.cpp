#include <parser/CAFF.hpp>

#include <fstream>
#include <iostream>
#include <iomanip>

int main(int argc, char** argv) {
	if(argc >= 2) {
        try {
            std::ifstream caffFile(argv[1], std::ios::binary);
            CAFF caff(caffFile);

            std::cout << "Parsing CAFF video file..." << std::endl;

            auto creator = caff.getCreator();
            std::cout << "Author: " << creator << std::endl;

            const auto &createdAt = caff.getCreatedAt();
            time_t createdAt_time_t = std::chrono::system_clock::to_time_t(createdAt);
            std::cout << "Date of creation: " << asctime(gmtime(&createdAt_time_t));

            int64_t width = caff.getWidth();
            int64_t height = caff.getHeight();
            std::cout << "Resolution: " << width << "x" << height << std::endl;

            std::cout << "Valid: " << std::boolalpha << caff.isValid() << std::endl;

            if (argc == 3) {
                std::cout << "Creating gif segment..." << std::endl;

                std::stringstream rawImage;
                caff.to_gif(rawImage);

                char *outputPath = argv[2];
                std::ofstream imageFileStream(outputPath, std::ofstream::binary);
                imageFileStream << rawImage.rdbuf() << std::flush;
                std::cout << "Output written to file '" << outputPath << "'." << std::endl;
            }

            return 0;
        } catch (std::exception& e) {
            std::cerr << "An exception has occurred: " << e.what() << std::endl;
        }
    }

    std::cerr << "Usage: ./parser input.caff [output.gif]" << std::endl;
	return 1;
}
