#ifndef TOKEN_H
#define TOKEN_H

#include "mystring.h"
#include "constants.h"

class Token
{
public:
    Token();
    Token(MyString str, int type);
    // Sets up token object (assigns private variables)
    friend ostream& operator << (ostream& outs,const Token& S);
    // Overloaded operator to print a token
    virtual int returnType();
    // Returns the type of token the object is, virtual so child classes can use the same function
    MyString returnToken();
    // Returns the current token

private:
    MyString _str; // The token
    int _type; // The Token type
};

#endif // TOKEN_H
