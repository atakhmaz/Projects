#include "MaxLettersAI.h"
#include <iostream>

using namespace std;

void MaxLettersAI::initialize (Dictionary* dict){
	d = dict;
	set<string> allWords = d->allWords();
	set<string>::iterator it = allWords.begin();
	for(; it != allWords.end(); ++it){
		int size = (*it).size();
		for(int i = 0; i < size; ++i){
			for(int j = 0; j < size; ++j){
				prefixes.insert((*it).substr(i,i-j+1));
			}
		}
	}
}

void MaxLettersAI::getMove (Board* board, Player* player, char& direction, std::string& word, std::string& wordToUse, 
							int& row, int& col){
	std::vector<char> directions;
	std::vector<string> words, wordsToUse;
	std::vector<int> rows;
	std::vector<int> cols;
	std::vector<int> scores;
	int score = 0;

	for(int row = 0; row < board->boardRows(); ++row){
		for(int col = 0; col < board->boardCols(); ++col){
			if(board->isUsed(row,col)){
				int temRow = row;
				int tempCol = col;
				direction = '|';
				score = 0;
				getMoveHelper(board, player, direction, word, wordToUse, temRow, tempCol, player->getHand(), score);
				if(score > 0){
					directions.push_back(direction);
					words.push_back(word);
					wordsToUse.push_back(wordToUse);
					rows.push_back(temRow);
					cols.push_back(tempCol);
					scores.push_back(score);
				}
				word.clear();
				wordToUse.clear();
				score = 0;
				direction = '-';
				getMoveHelper(board, player, direction, word, wordToUse, temRow, tempCol, player->getHand(), score);
				if(score > 0){
					directions.push_back(direction);
					words.push_back(word);
					wordsToUse.push_back(wordToUse);
					rows.push_back(temRow);
					cols.push_back(tempCol);
					scores.push_back(score);
				}
				score = 0;
				word.clear();
				wordToUse.clear();
			}
		}
	}
	unsigned int moveToChose = 0;
	int maxLetters = 0, size;
	for(unsigned int i = 0; i < words.size(); ++i){
		size = words[i].size();
		if(size > maxLetters){
			maxLetters = size;
			moveToChose = i;
		}
	}
	if(!words.empty()){
		direction = directions[moveToChose];
		word = words[moveToChose];
		wordToUse = wordsToUse[moveToChose];
		row = rows[moveToChose];
		col = cols[moveToChose];
	}
	else{
		word.clear();
	}
}


bool MaxLettersAI::getMoveHelper(Board* board, Player* player, char& direction, string& word, std::string& wordToUse,
								int& row, int& col, string tiles, int& score){		
	if(!board->checkPosition(direction, row, col, word.size())){
		return false;
	}
	vector<string> wordsFormed;
	if(!word.empty())
		wordsFormed = board->findAdjacentWords(direction, row, col, word, score, player);
	if(wordsFormed.empty() && !word.empty()){
		return false;
	}
	bool validMove = true;
	for(unsigned int l = 0; l < wordsFormed.size(); ++l){
		if(!d->find(wordsFormed[l])){
			validMove = false;
			break;
		}
	}
	if(validMove && !wordsFormed.empty()){
		return true;
	}
	else if(!wordsFormed.empty()){
		return false;
	}

	int otherSideOfWordRow = row;
	while(otherSideOfWordRow < board->boardRows() && board->isUsed(otherSideOfWordRow, col))
		++otherSideOfWordRow;
	int otherSideOfWordCol = col;
	while(otherSideOfWordCol < board->boardCols() && board->isUsed(row, otherSideOfWordCol))
		++otherSideOfWordCol;

	string tileSubset;
	std::vector<std::string> perms;
	int size = tiles.size();
	vector<size_t> blankPlaces;

	for(int i = size; i >= 0; --i){
		for(int j = 0; j < size-i; ++j){
			tileSubset = tiles.substr(j,i-j+1);
			size_t blank = tileSubset.find('?');
			if(blank == string::npos)
				permutations(perms, 0, tileSubset);
			while(blank != string::npos){
				blankPlaces.push_back(blank);
				for(int z = 0; z < 26; ++z){
					tileSubset[blank] = (char)z+65;
					permutations(perms, 0, tileSubset);
				}
				blank = tileSubset.find('?');
			}
			int size2 = perms.size();
			for(int k = 0; k < size2; ++k){
				if(findPrefix(perms[k])){
					int tempRow = row - 1;

					string actualPerm = perms[k];
					string tempTiles = tiles;
					for(size_t n = 0; n < actualPerm.size(); ++n){
						size_t pos = tempTiles.find(actualPerm[n]);
						if(pos == string::npos)
							actualPerm[n] = '?';
						else
							tempTiles.erase(pos,1);
					}


					if(row > 0 && row < board->boardRows() && 
					getMoveHelper(board, player, direction, perms[k], perms[k], tempRow, col , tiles, score)){ //check prefixes
						word = perms[k];
						wordToUse = actualPerm;
						row = tempRow;
						return true;
					}
					int tempCol = col - 1;
					score = 0;
					if(col > 0 && col < board->boardCols() && 
					getMoveHelper(board, player, direction, perms[k], perms[k], row, tempCol, tiles, score)){ //check prefixes
						word = perms[k];
						wordToUse = actualPerm;
						col = tempCol;
						return true;
					}
					score = 0;
					if(otherSideOfWordRow < board->boardRows() && 
						getMoveHelper(board, player, direction, perms[k], perms[k], otherSideOfWordRow, col , tiles, score)){
						word = perms[k];
						wordToUse = actualPerm;
						row = otherSideOfWordRow;
						return true;
					}
					score = 0;
					if(otherSideOfWordCol < board->boardRows() && 
						getMoveHelper(board, player, direction, perms[k], perms[k], row, otherSideOfWordCol , tiles, score)){
						word = perms[k];
						wordToUse = actualPerm;
						col = otherSideOfWordCol;
						return true;
					}
					score = 0;
				}
			}
			perms.clear();
			blankPlaces.clear();
		}
	}
	return false;
}

void MaxLettersAI::permutations(std::vector<std::string>& perms, int pos, string str){
	string permutation;
	int size = str.size();
	for(int i = pos; i < size; ++i)
	{
		permutation = str;
		char temp = permutation[pos];
		permutation[pos] = permutation[i];
		permutation[i] = temp;
		if(i != pos || !pos)
			perms.push_back(permutation);
		permutations(perms, pos+1, permutation);
	}
}

bool MaxLettersAI::findPrefix(std::string word){
	return prefixes.find(word) != prefixes.end();
}