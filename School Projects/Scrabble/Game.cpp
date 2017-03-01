#include "Game.h"
#include <stdexcept>

using namespace std;

Game::Game(char* config_file_name)
{
	readConfigFile (config_file_name,
					dictionaryFileName, boardFileName, bagFileName,
					numTiles, initFileName);

	dict = new Dictionary(dictionaryFileName);
	board = new Board(boardFileName, initFileName);
	bag = new Bag(bagFileName, 794);
	num_players = 0;
	currentPlayer = 0;
	numberOfPasses = 0;
}

Game::~Game(){
	delete dict;
	delete board;
	delete bag;
	for(unsigned int i = 0; i < players.size(); ++i){
		delete players[i];
	}
}

void Game::readConfigFile (string config_file_name,
							string & dictionary_file_name,
							string & board_file_name,
							string & bag_file_name,
							int & hand_size,
							string & init_file_name)
{
	ifstream configFile (config_file_name.c_str());
	string line;
	bool number = false, board = false, tiles = false, dictionary = false;

	if (!configFile.is_open())
		throw invalid_argument("Cannot open file: " + config_file_name);
	while (getline (configFile, line))
	{
		stringstream ss (line);
		string parameter;
		ss >> parameter;
		if (parameter == "NUMBER:")
			{ ss >> hand_size; number = true; }
		else if (parameter == "BOARD:")
				{ ss >> board_file_name; board = true; }
		else if (parameter == "TILES:")
			{ ss >> bag_file_name; tiles = true; }
		else if (parameter == "DICTIONARY:")
			{ ss >> dictionary_file_name; dictionary = true; }		
		else if (parameter == "INIT:")
			{ ss >> init_file_name;}
	}
	if (!number)
		throw invalid_argument("Hand size not specified in config file");
	if (!board)
		throw invalid_argument("Board file name not specified in config file");
	if (!tiles)
		throw invalid_argument("Bag file name not specified in config file");
	if (!dictionary)
		throw invalid_argument("Dictionary file name not specified in config file");
}

string Game::performPlay(char direction, string word, string wordToUse, int row, int col, string& drawnTiles){
	numberOfPasses = 0;
	int bonus = 0, score = 0;
	int size = word.size();

	if(size == numTiles)
		bonus = 50;

	if(word.empty())
		throw invalid_argument("No tiles selected to be placed.");
	if(direction != '-' && direction != '|')
		throw invalid_argument("Please click on a square before placing tiles.");
	if(!players[currentPlayer]->findInHand(word))
		throw invalid_argument("You do not have the proper tiles.");
	if(initFileName.empty() && !board->checkPosition(direction, row, col, size))
		throw invalid_argument("Invalid position.");
	vector<string> wordsFormed = board->findAdjacentWords(direction, row, col, wordToUse, score, players[currentPlayer]);
	for(unsigned int i = 0; i < wordsFormed.size(); ++i){
		if(!dict->find(wordsFormed[i])){
			throw invalid_argument("Invalid word formed.");
		}
	}

	board->placeWord(direction, row, col, wordToUse, players[currentPlayer]);
	players[currentPlayer]->updateScore(score+bonus);
	players[currentPlayer]->removeFromHand(word);
	drawnTiles = players[currentPlayer]->drawTiles(word.size(), bag);

	return wordsFormed[wordsFormed.size()-1];
}

bool Game::checkAllHands(){
	for(int i = 0; i < num_players; ++i)
		if(players[i]->emptyHand())
			return true;
	return false;
}

void Game::findWinners(vector<Player*>& winners){
	int maxScore = 0;
	for(int i = 0; i < num_players; ++i){
		if(players[i]->score > maxScore){
			winners.clear();
			maxScore = players[i]->score;
			winners.push_back(players[i]);
		}
		else if(players[i]->score == maxScore)
			winners.push_back(players[i]);
	}
}

void Game::updateNumberOfPlayers(int numberOfPlayers){
	num_players = numberOfPlayers;
}

void Game::addPlayer(std::string playerName){
	Player* player;
	player = new Player(playerName);
	player->drawTiles(numTiles, bag);
	players.push_back(player);
}

int Game::numberOfPlayers(){
	return num_players;
}

std::string Game::currentPlayerName(){
	return players[currentPlayer]->name;
}

std::string Game::playerName(int i){
	return players[i]->name;
}

int Game::playerScore(int i){
	return players[i]->score;
}

int Game::currentPlayerScore(){
	return players[currentPlayer]->score;
}

int Game::currentPlayerHandSize(){
	return players[currentPlayer]->hand.size();
}

std::string Game::currentPlayersTile(int i){
	return players[currentPlayer]->specificTile(i);
}

int Game::boardRows(){
	return board->boardRows();
}

int Game::boardCols(){
	return board->boardCols();
}

std::string Game::tileAt(int row, int col){
	return board->tileAt(row, col);
}

void Game::nextPlayer(){
	currentPlayer = (currentPlayer+1)%num_players;
}

string Game::exchangeHand(std::string exchangeString){
	numberOfPasses = 0;
	return players[currentPlayer]->exchangeHand(exchangeString, bag);
}

void Game::pass(){
	++numberOfPasses;
	nextPlayer();
}

int Game::numberofPasses(){
	return numberOfPasses;
}

string Game::updateScores(){
	int winBonus = 0;
	int howMuchLost = 0;
	string returnString;
	if(numberOfPasses < num_players){
		for(int i = 0; i < num_players; ++i){
			howMuchLost = players[i]->leftOverHand();
			winBonus += howMuchLost;
			returnString += "Player " + to_string(i+1) + " lost " + to_string(howMuchLost) + " points.\n";
		}
		for(int i = 0; i < num_players; ++i)
			if(players[i]->emptyHand()){
				players[i]->score += winBonus;
			returnString += "Player " + to_string(i+1) + " gained " + to_string(winBonus) + " points.\n";
			}
	}
	return returnString;
}

bool Game::isTileUsed(int row, int col){
	return board->isUsed(row, col);
}

Dictionary* Game::getDictionary(){
	return dict;
}

Board* Game::getBoard(){
	return board;
}

Player* Game::getPlayer(){
	return players[currentPlayer];
}

std::map<char,int> Game::initialTileCount(){
	return bag->initialTileCount();
}