#include "Player.h"
#include <stdexcept>
#include <iostream>
using namespace std;
Player::Player(const std::string& s, int n)
{
	name = s; 
	score = n;
}

Player::~Player(){
	for(std::set<Tile*>::iterator it = hand.begin(); it != hand.end(); ++it)
		delete *it;
}

std::set<Tile*> Player::displayHand(){
	return hand;
}

std::string Player::drawTiles(unsigned int numTiles, Bag* bag){
	std::string returnString;
	if(bag->tilesRemaining() == 0)
		return returnString;
	std::set<Tile*> drawn = bag->drawTiles(numTiles);
	for(std::set<Tile*>::iterator it = drawn.begin(); it != drawn.end(); ++it){
		hand.insert(*it);
		returnString += std::string() + (*it)->getLetter() + " ";
	}
	return returnString;
}

std::string Player::exchangeHand(std::string exchangeString, Bag* bag){
	if(exchangeString.empty())
		throw std::invalid_argument("No tiles selected to exchange.");
	if(exchangeString.find('?') != std::string::npos && exchangeString.size() < 2)
		throw std::invalid_argument("You must exchange at least one letter.");
	if(bag->tilesRemaining() == 0)
		throw std::invalid_argument("Cannot exchange hand, bag is empty.");
	unsigned int num_tiles = exchangeString.size();
	for(unsigned int i = 0; i < num_tiles; ++i){
		std::set<Tile*>::iterator it = findTile(exchangeString[i]);
		if(it == hand.end())
			throw std::invalid_argument("Cannot exchange tiles you do not have.");
	}
	for(unsigned int i = 0; i < num_tiles; ++i){
		std::set<Tile*>::iterator it = findTile(exchangeString[i]);
		bag->addTile(*it);
		hand.erase(it);
	}
	return drawTiles(num_tiles, bag);
}

bool Player::findInHand(std::string word){
	size_t pos = word.find('?');
	while(pos != std::string::npos){
		word.erase(pos+1,1);
		pos = word.find('?', pos+1);
	}

	int letters[28];
	for(int i = 0; i < 28; ++i)
		letters[i] = 0;
	for(std::set<Tile*>::iterator it = hand.begin(); it != hand.end(); ++it)
		++letters[(*it)->getLetter()-63];

	for(unsigned int i = 0; i < word.size(); ++i){
		--letters[word[i]-63];
	}
	for(int i = 0; i < 28; ++i)
		if(letters[i] < 0)
			return false;
	return true;
}

void Player::removeFromHand(std::string word){
	for(unsigned int i = 0; i < word.size(); ++i){
		std::set<Tile*>::iterator it = findTile(word[i]);
		if(it == hand.end())
			throw std::invalid_argument("Fatal Error: Could not remove a tile from the hand");
		hand.erase(it);
		delete *it;
	}
}

void Player::updateScore(int score){
	this->score += score;
}

std::set<Tile*>::iterator Player::findTile(char letter){
	std::set<Tile*>::iterator it = hand.begin();
	while(it != hand.end()){
		if((*it)->getLetter() == letter){
			break;
		}
		else
			++it;
	}
	return it;
}

bool Player::emptyHand(){
	return hand.empty();
}

int Player::leftOverHand(){
	int leftOverPenalty = 0;
	for(std::set<Tile*>::iterator it = hand.begin(); it != hand.end(); ++it)
		leftOverPenalty += (*it)->getPoints();
	score -= leftOverPenalty;
	if(score < 0)
		score = 0;
	return leftOverPenalty;
}

std::string Player::specificTile(int i){
	int size = hand.size()-1;
	if(i > size)
		return std::string();
	std::set<Tile*>::iterator it = hand.begin();
	for(int j = 0; j < i; ++j)
		++it;
	return (*it)->getLetter() + std::to_string((*it)->getPoints());
}

std::string Player::getHand (){
	std::string returnString;
	for(std::set<Tile*>::iterator it = hand.begin(); it != hand.end(); ++it)
		returnString += (*it)->getLetter();
	return returnString;
}