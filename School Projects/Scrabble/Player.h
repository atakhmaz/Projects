#ifndef PLAYER_H
#define PLAYER_H

#include "Tile.h"
#include "Bag.h"
#include <set>
#include <string>

class Player{
public:
	Player(const std::string& s, int n = 0);
	~Player();

	std::set<Tile*> displayHand();
	std::string drawTiles(unsigned int numTiles, Bag* bag);
	std::string exchangeHand(std::string exchangeString, Bag* bag);
	bool findInHand(std::string word);
	void removeFromHand(std::string word);
	bool emptyHand();
	int leftOverHand();
	std::string specificTile(int i);
	std::string getHand ();
	void updateScore(int score);

	std::set<Tile*>::iterator findTile(char letter);
	std::string name;
	int score;
	std::set<Tile*> hand;

private:
};
#endif //PLAYER_H