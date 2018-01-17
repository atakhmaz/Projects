#include "Board.h"
#include <fstream>
#include <iostream>
#include <sstream>
#include <stdexcept>
#include <iomanip> 
#include <set>
#include "Tile.h"

using namespace std;

Board::Board (string board_file_name, std::string init_file_name)
{
	ifstream boardFile (board_file_name.c_str());
	ifstream initFile;
	if(!init_file_name.empty())
		initFile.open(init_file_name.c_str());
	string row, initTileBlock;

	_x = _y = _startx = _starty = 0;
	if (!boardFile.is_open())
		throw invalid_argument("Cannot open file: " + board_file_name);
	if (!init_file_name.empty() && !initFile.is_open())
		throw invalid_argument("Cannot open file: " + init_file_name);
	getline (boardFile, row);
	stringstream s1 (row);
	s1 >> _x >> _y;
	getline (boardFile, row);
	stringstream s2 (row);
	s2 >> _startx >> _starty;
	_startx --; _starty --;

	board = new Square**[_y];

	for(int i = 0 ; i < _y; ++ i)
	{
		getline (boardFile, row);
		board[i] = new Square*[_x];
		for(int j = 0; j < _x; ++ j)
		{
			if (i == _starty && j == _startx) 
				board[i][j] = new Square(1,1,true);
			else switch (row[j]) {
			case '.' :
				board[i][j] = new Square(1,1,false);
				break;
			case '2' :
				board[i][j] = new Square(2,1,false);
				break;
			case '3' :
				board[i][j] = new Square(3,1,false);
				break;
			case 'd' :
				board[i][j] = new Square(1,2,false);
				break;
			case 't' :
				board[i][j] = new Square(1,3,false);
				break;
			default:
				string error = "Improper character in Board file: ";
				throw invalid_argument(error + row[j]);
			}
		}
	}
	if(!init_file_name.empty())
		for(int i = 0 ; i < _y; ++ i)
		{
			if(!getline (initFile, row))
				throw invalid_argument("Init file not properly formatted.");
			for(int j = 0; j < _x*3; j+=3)
			{
				initTileBlock = row.substr(j,3);
				if(initTileBlock.size() < 3)
					throw invalid_argument("Init file not properly formatted.");
				if(initTileBlock != "..."){
					board[i][j/3]->placeLetter(toupper(initTileBlock[0]), stoi(initTileBlock.substr(1,2)));
				}
			}
		}
	boardFile.close();
	if(!init_file_name.empty())
		initFile.close();
}

Board::~Board(){
	for(int i = 0 ; i < _y; ++ i){
		for(int j = 0; j < _x; ++ j)
			delete board[i][j];
		delete[] board[i];
	}
	delete[] board;
	board = NULL;
}

void Board::placeWord(char direction, int row, int col, std::string word, Player* player){
	int size = word.size();
	if(direction == '-'){
		int i = 0, j = 0;
		while(j < size)
			if(board[row][col+i]->isUsed()){
				++i;
			}
			else{
				std::set<Tile*>::iterator it = player->findTile(word[i]);
				int points = 0;
				if(it != player->hand.end())
					points = (*it)->getPoints();
				board[row][col+i]->placeLetter(word[j], points);
				++i;
				++j;
			}
		}
	else if(direction == '|'){
		int i = 0, j = 0;
		while(j < size)
			if(board[row+i][col]->isUsed()){
				++i;
			}
			else{
				std::set<Tile*>::iterator it = player->findTile(word[i]);
				int points = 0;
				if(it != player->hand.end())
					points = (*it)->getPoints();
				board[row+i][col]->placeLetter(word[j], points);
				++i;
				++j;
			}
		}
}

std::vector<std::string> Board::findAdjacentWords(char direction, int row, int col, std::string word, int& score, Player* player){
	vector<std::string> wordsFormed;
	string wordFormed;
	score = 0;
	int size = word.size();
	if(direction == '-'){
		for(int x = 0; x < size; ++x)
			if(board[row][x+col]->isUsed())
				++size;
		for(int i = 0; i < size; ++i){
			if(!board[row][i+col]->isUsed()){
				wordFormed = findAdjacentWord('|', row, col+i, string()+word[i], score);
				if(wordFormed.size() > 1)
					wordsFormed.push_back(wordFormed);
			}
		}
	}
	else if(direction == '|'){
		for(int x = 0; x < size; ++x)
			if(board[x+row][col]->isUsed())
				++size;
		for(int i = 0; i < size; ++i){
			if(!board[i+row][col]->isUsed()){
				wordFormed = findAdjacentWord('-', row+i, col, string()+word[i], score);
				if(wordFormed.size() > 1){
					wordsFormed.push_back(wordFormed);
				}
			}
		}
	}
	string mainWord = findAdjacentWord(direction, row, col, word, score);
	if(!mainWord.empty())
		wordsFormed.push_back(mainWord);
	if(player != NULL)
		score += calculateScore(direction, row, col, word, player);
	return wordsFormed;
}

string Board::findAdjacentWord(char direction, int row, int col, std::string word, int& score){
	int pos = 0, i = 0;
	int j = 0, size = word.size();
	std::string wordFormed;

	if(direction == '-'){
		for(int x = 0; x < size; ++x)
			if(x+col < _y && board[row][x+col]->isUsed())
				++size;
		pos = col-1;
		while(pos > 1 && board[row][pos]->isUsed()){
			wordFormed.insert(0, string() + board[row][pos]->getLetter());
			score += board[row][pos]->getScore();
			--pos;
		}
		while(i < size){
			if(board[row][i+col]->isUsed()){
				wordFormed += string() + board[row][i+col]->getLetter();
				score += board[row][i+col]->getScore();
				++i;
			}
			else{
				wordFormed += word[j];
				++j;
				++i;
			}
		}
		pos = col+size;
		while(pos < _x && board[row][pos]->isUsed()){
			wordFormed += string() + board[row][pos]->getLetter();
			score += board[row][pos]->getScore();
			++pos;
		}
	}
	else if(direction == '|'){
		for(int x = 0; x < size; ++x)
			if(x+row < _x && board[x+row][col]->isUsed())
				++size;
		pos = row-1;
		while(pos > 1 && board[pos][col]->isUsed()){
			wordFormed.insert(0, string() + board[pos][col]->getLetter());
			score += board[pos][col]->getScore();
			--pos;
		}
		while(i < size){
			if(board[row+i][col]->isUsed()){
				wordFormed += string() + board[row+i][col]->getLetter();
				score += board[row+i][col]->getScore();
				++i;
			}
			else{
				wordFormed += word[j];
				++j;
				++i;
			}
		}
		pos = row+size;
		while(pos < _y && board[pos][col]->isUsed()){
			wordFormed += string() + board[pos][col]->getLetter();
			score += board[pos][col]->getScore();
			++pos;
		}
	}
	return wordFormed;
}

bool Board::checkPosition(char direction, int row, int col, int wordLength){

	if(row < 0 || col <0)
		return false;

	if(wordLength == 0)
		return true;

	if(board[row][col]->isUsed())
		return false;

	if(direction == '-'){
		for(int x = 0; x < wordLength; ++x)
			if(x+col < _y && board[row][x+col]->isUsed())
				++wordLength;

		if(col+wordLength > _x)
			return false;

		for(int i = 0; i < wordLength; ++i){
			if((row > 0 && board[row-1][col+i]->isUsed()) || (col > 0 && board[row][col+i-1]->isUsed()) || 
			   (row+i+1 < _y && board[row+1][col+i]->isUsed()) || (col+i+1 < _x && board[row][col+i+1]->isUsed()) ||
			   board[row][col+i]->isStart())
				return true;
		}
	}
	else if(direction == '|'){
		for(int x = 0; x < wordLength; ++x)
			if(x+row < _x && board[x+row][col]->isUsed())
				++wordLength;

		if(row+wordLength > _y)
			return false;

		for(int i = 0; i < wordLength; ++i){
			if((row > 0 && board[row+i-1][col]->isUsed()) || (col > 0 && board[row+i][col-1]->isUsed()) || 
			   (row+1 < _y && board[row+i+1][col]->isUsed()) || (col+1 < _x && board[row+i][col+1]->isUsed()) ||
			   board[row+i][col]->isStart())
				return true;
		}
	}
	return false;
}

int Board::boardRows(){
	return _y;
}

int Board::boardCols(){
	return _x;
}

std::string Board::tileAt(int row, int col){
	if(board[row][col]->isStart())
		return "S";
	else if(board[row][col]->getLetter() == ' ' && board[row][col]->getLMult() == 1 && board[row][col]->getWMult() == 1)
		return "";
	else if(board[row][col]->getLMult() == 2){
		return "2L";
	}
	else if(board[row][col]->getLMult() == 3){
		return "3L";
	}
	else if(board[row][col]->getWMult() == 2){
		return "2W";
	}
	else if(board[row][col]->getWMult() == 3){
		return "3W";
	}
	else{
		string returnString;
		if(board[row][col]->getScore() < 10)
			returnString = string() + board[row][col]->getLetter() + "0" + to_string(board[row][col]->getScore());
		else
			returnString = string() + board[row][col]->getLetter() + to_string(board[row][col]->getScore());
		return returnString;
	}
}

bool Board::isUsed(int row, int col){
	return board[row][col]->isUsed();
}

bool Board::isStart(int row, int col){
	return board[row][col]->isStart();
}

int Board::getScore(int row, int col){
	return board[row][col]->getScore();
}

int Board::calculateScore(char direction, int row, int col, std::string word, Player* player){
	int score = 0;
	int originalWordScore = 0;
	if(player != NULL){
		for(unsigned int i = 0; i < word.size(); ++i){
			std::set<Tile*>::iterator it = player->findTile(word[i]);
			if(it != player->hand.end())
				originalWordScore += (*it)->getPoints();
		}
	}
	int j = 0;
	if(direction == '-'){
		for(unsigned int i = 0; i < word.size();){
			if(board[row][col+j]->isUsed()){
				++j;
			}
			else{				
				if(player == NULL){
					score += board[row][col+j]->getScore();
				}
				else{
					std::set<Tile*>::iterator it = player->findTile(word[i]);
					if(it != player->hand.end()){
						score += (*it)->getPoints() * board[row][col+j]->getLMult();
						if(board[row][col+j]->getWMult() > 1){
							score += originalWordScore * board[row][col+j]->getWMult();
						}
					}
				}
			++i;
			++j;
			}
		}
	}
	else if(direction == '|'){
		for(unsigned int i = 0; i < word.size();){
			if(board[row+j][col]->isUsed()){
				++j;
			}
			else{
				if(player == NULL){
					score += board[row][col+j]->getScore();
				}
				else{
					std::set<Tile*>::iterator it = player->findTile(word[i]);
					if(it != player->hand.end()){
						score += (*it)->getPoints() * board[row+j][col]->getLMult();
						if(board[row+j][col]->getWMult() > 1)
							score += originalWordScore * board[row+j][col]->getWMult();
					}
				}
			++i;
			++j;
			}
		}
	}
	return score;
}