#ifndef DICTIONARY_H_
#define DICTIONARY_H_

#include <string>
#include <set>

class Dictionary {
public:
	Dictionary (std::string dictionary_file_name);
	~Dictionary();
	bool find(std::string word);
	std::set<std::string> allWords () const;

private:
	std::set<std::string> words;
};


#endif /* DICTIONARY_H_ */
