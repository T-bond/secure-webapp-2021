#include <parser/CAFF.hpp>

#include <fstream>
#include <iostream>

int main(int argc, char** argv) {
	if(argc != 2) {
		return 1;
	}

	std::ifstream caffFile(argv[1], std::ios::binary);
	CAFF caff(caffFile);

	std::cout << "Valid: " << std::boolalpha << caff.isValid() << std::endl;

	return 0;
}
