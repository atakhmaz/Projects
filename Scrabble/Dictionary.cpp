#include <fstream>
#include <iostream>
#include <stdexcept>
#include "Dictionary.h"

Dictionary::Dictionary (std::string dictionary_file_name)
{
	std::ifstream dictFile (dictionary_file_name.c_str());
	std::string word;

	if (dictFile.is_open())
	{
		while (getline (dictFile, word))
		{
			for (unsigned int i = 0; i < word.length(); ++i)
				word[i] = toupper(word[i]);
			words.insert (word);		
		}
		dictFile.close ();
	}
	else throw std::invalid_argument("Cannot open file: " + dictionary_file_name);
}

Dictionary::~Dictionary(){
}

bool Dictionary::find(std::string word){
	return (words.find(word) != words.end());
}

std::set<std::string> Dictionary::allWords () const{
	return words;
}