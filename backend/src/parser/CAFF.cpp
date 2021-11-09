#include "parser/CAFF.hpp"

#include <iostream>

CAFF::CAFF(std::istream& caffContent) {
	try {
		parseBlocks(caffContent);

		valid = true;
	} catch(const std::invalid_argument& exception) {
		/// TODO: Handle errors
		std::cout << exception.what() << std::endl;
	}
}

void CAFF::parseBlocks(std::istream& caffContent) {
	parseHeader(caffContent);

	do {
		auto blockId = BlockID(caffContent.peek());
		if(caffContent.eof()) {
			break;
		}

		switch(blockId) {
			case BlockID::HEADER:
				parseHeader(caffContent);
				break;
			case BlockID::CREDITS:
				parseCredits(caffContent);
				break;
			case BlockID::FRAME:
				parseFrame(caffContent);
				break;
			default:
				throw std::invalid_argument("The given content is not a valid CAFF file. Invalid block id.");
		}
	} while(!caffContent.eof());

	unsigned long actualFrameCount = frames.size();
	if(actualFrameCount != expectedFrameCount) {
		throw std::invalid_argument("The given content is not a valid CAFF file. Missing frames. Expected: " +
		                            std::to_string(expectedFrameCount) + " Got: " + std::to_string(
				actualFrameCount));
	}
}

void CAFF::parseHeader(std::istream& caffContent) {
	uint64_t blockSize = CAFF::extractBlockInformation(caffContent, BlockID::HEADER);

	if(blockSize != REQUIRED_HEADER_LENGTH) {
		throw std::invalid_argument("The given content is not a valid CAFF file. Header size must match block size.");
	}

	std::string fileMagic(MAGIC_LENGTH, '\0');
	caffContent.read(fileMagic.data(), MAGIC_LENGTH);

	if(!caffContent.good() || fileMagic != MAGIC) {
		throw std::invalid_argument("The given content is not a valid CAFF file. Magic header mismatch.");
	}

	int64_t headerLength;
	caffContent.read(reinterpret_cast<char*>(&headerLength), sizeof headerLength);
	if(!caffContent.good() || headerLength != REQUIRED_HEADER_LENGTH) {
		throw std::invalid_argument("The given content is not a valid CAFF file. Invalid header size.");
	}

	int64_t previousFrameCount = expectedFrameCount;
	caffContent.read(reinterpret_cast<char*>(&expectedFrameCount), sizeof expectedFrameCount);
	if(!caffContent.good() || expectedFrameCount <= 0) {
		throw std::invalid_argument("The given content is not a valid CAFF file. Invalid frame count.");
	}

	if(previousFrameCount != 0 && previousFrameCount != expectedFrameCount) {
		throw std::invalid_argument(
				"The given content is not a valid CAFF file. Multiple header found with different frame counts.");
	}
}

void CAFF::parseCredits(std::istream& caffContent) {
	uint64_t blockSize = CAFF::extractBlockInformation(caffContent, BlockID::CREDITS);

	int16_t yearValue;
	caffContent.read(reinterpret_cast<char*>(&yearValue), sizeof yearValue);
	int8_t monthValue;
	caffContent.read(reinterpret_cast<char*>(&monthValue), sizeof monthValue);
	int8_t dayValue;
	caffContent.read(reinterpret_cast<char*>(&dayValue), sizeof dayValue);
	int8_t hourValue;
	caffContent.read(reinterpret_cast<char*>(&hourValue), sizeof hourValue);
	int8_t minuteValue;
	caffContent.read(reinterpret_cast<char*>(&minuteValue), sizeof minuteValue);
	{
		using namespace std::chrono;
		createdAt =
				sys_days{year(yearValue) / month(monthValue) / day(dayValue)} + hours(hourValue) + minutes(minuteValue);
	}

	int64_t createdByLength;
	caffContent.read(reinterpret_cast<char*>(&createdByLength), sizeof createdByLength);
	if(!caffContent.good() || createdByLength < 0) {
		throw std::invalid_argument(
				"The given content is not a valid CAFF file. The creator length can not be negative.");
	}

	if(createdByLength + sizeof createdByLength != blockSize - CREATED_AT_LENGTH) {
		throw std::invalid_argument(
				"The given content is not a valid CAFF file. The creator information exceeds it's block size.");
	}

	try {
		createdBy.resize(createdByLength);
	} catch (const std::bad_alloc &) {
		throw std::invalid_argument("Could not parse creator info, as it exceeds the memory limit.");
	}
	caffContent.read(createdBy.data(), createdByLength);
}

void CAFF::parseFrame(std::istream& caffContent) {
	uint64_t blockSize = CAFF::extractBlockInformation(caffContent, BlockID::FRAME);

	const auto& blockStartPos = caffContent.tellg();
	int64_t duration;
	caffContent.read(reinterpret_cast<char*>(&duration), sizeof duration);
	if(!caffContent.good() || duration < 0) {
		throw std::invalid_argument("The given content is not a valid CAFF file. The duration can not be negative.");
	}

	CIFF imageFrame(caffContent);
	if(!imageFrame.isValid()) {
		throw std::invalid_argument(
				"The given content is not a valid CAFF file. The " + std::to_string(frames.size() + 1) +
				"th frame is invalid.");
	}

	long actualBlockSize = caffContent.tellg() - blockStartPos;
	if(blockSize != actualBlockSize) {
		throw std::invalid_argument(
				"The given content is not a valid CAFF file. The embedded CIFF file not matched it's container block size.");
	}

	frames.emplace_back(duration, imageFrame);
}

uint64_t CAFF::extractBlockInformation(std::istream& caffContent, const CAFF::BlockID& blockId) {
	auto extractedBlockId = BlockID(caffContent.get());
	if(extractedBlockId != blockId) {
		throw std::invalid_argument("The given content is not a valid CAFF file. Expected block id differs from actual."
		                            " Maybe first block is not header?");
	}

	int64_t blockSize;
	caffContent.read(reinterpret_cast<char*>(&blockSize), sizeof blockSize);
	if(blockSize <= 0) {
		throw std::invalid_argument("The given content is not a valid CAFF file. Block size must be positive.");
	}

	return blockSize;
}
