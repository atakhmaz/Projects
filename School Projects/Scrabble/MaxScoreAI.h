#ifndef MAXSCOREAI_H
#define MAXSCOREAI_H

#include <string>
#include <map>
#include <set>

#include "Dictionary.h"
#include "Board.h"
#include "Player.h"

class MaxScoreAI {
public:
	void initialize (Dictionary* dict);
	void getMove (Board* board, Player* player, char& direction, std::string& word, std::string& wordToUse, int& row, int& col);

private:
	Dictionary* d;
	std::set<std::string> prefixes;
	void permutations(std::vector<std::string>& perms, int pos, std::string str);
	bool getMoveHelper(Board* board, Player* player, char& direction, std::string& word, std::string& wordToUse, int& row, int& col, std::string tiles, int& score);
	bool findPrefix(std::string word);
};
#endif //MAXSCOREAI_H