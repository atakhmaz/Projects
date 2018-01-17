#ifndef BOARD_H_
#define BOARD_H_

#include <string>
#include <vector>
#include "Square.h"
#include "Player.h"

class Board {
public:
	Board (std::string board_file_name, std::string init_file_name);
	~Board();

	void placeWord(char direction, int row, int col, std::string word, Player* player);
	std::vector<std::string> findAdjacentWords(char direction, int row, int col, std::string word, int& score, Player* player);
	bool checkPosition(char direction, int row, int col, int wordLength);
	int boardRows();
	int boardCols();
	std::string tileAt(int row, int col);
	bool isUsed(int row, int col);
	bool isStart(int row, int col);	
	int getScore(int row, int col);

private:
	int _x, _y, _startx, _starty;
	Square*** board;
	std::string findAdjacentWord(char direction, int row, int col, std::string word, int& score);
	int calculateScore(char direction, int row, int col, std::string word, Player* player);
};


#endif /* BOARD_H_ */
