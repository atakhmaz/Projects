#ifndef GAME_H
#define GAME_H

#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <cstdlib>
#include <vector>

#include "Dictionary.h"
#include "Bag.h"
#include "Board.h"
#include "Player.h"

class Game
{
public:
	Game(char* config_file_name);
	~Game();

	void updateNumberOfPlayers(int numberOfPlayers);
	void addPlayer(std::string playerName);
	int numberOfPlayers();
	std::string currentPlayerName();
	std::string playerName(int i);
	int playerScore(int i);
	int currentPlayerScore();
	int currentPlayerHandSize();
	std::string currentPlayersTile(int i);
	int boardRows();
	int boardCols();
	std::string tileAt(int row, int col);
	void nextPlayer();
	std::string exchangeHand(std::string exchangeString);
	std::string performPlay(char direction, std::string word, std::string wordToUse, int row, int col, std::string& drawnTiles);
	void pass();
	int numberofPasses();
	bool checkAllHands();
	void findWinners(std::vector<Player*>& winners);
	std::string updateScores();
	bool isTileUsed(int row, int col);
	Dictionary* getDictionary();
	Board* getBoard();
	Player* getPlayer();
	std::map<char,int> initialTileCount();
	
private:
	std::vector<Player*> players;
	std::string dictionaryFileName, boardFileName, bagFileName, initFileName;
	int numTiles, num_players;
	Dictionary* dict;
	Board* board;
	Bag* bag;
	int currentPlayer;
	int numberOfPasses;

	void readConfigFile (std::string config_file_name,
						 std::string & dictionary_file_name,
						 std::string & board_file_name,
						 std::string & bag_file_name,
						 int & hand_size,
						 std::string & init_file_name);
};
#endif //GAME_H