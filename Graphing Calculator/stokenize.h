#ifndef STOKENIZE_H
#define STOKENIZE_H

#include "token.h"

class sTokenize
{
public:
    sTokenize();
    sTokenize(const MyString str);
    // Sets up the string, sets position to 0, and sets _more to true
    Token NextToken();
    // Gets next token
    bool more();
    // Returns if there are more tokens in the string
    int pos();
    // Current position in the string
    bool fail();
    // Returns if there are no more tokens in the string

private:
    MyString _str; // String to be tokenized
    int _pos; // Position in string
    bool _more; // Set to true if token returned
};

#endif // STOKENIZE_H
