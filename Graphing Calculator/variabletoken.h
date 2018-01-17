#ifndef VARIABLETOKEN_H
#define VARIABLETOKEN_H

#include "token.h"

class variableToken: public Token // Child of Token object (inheritance)
{
public:
    variableToken();
    variableToken(MyString value);
    // Calls the parent constructor to make an object and also assigns the string to the private memember variable
    int returnType();
    // Returns the type of token it is (10)
    MyString returnVariable();
    // Returns the variable
    bool isNegative();
    // Checks if the variable is x or if its the negative number variable "n"

private:
    MyString var; // Private member variable of string type for the variable
};

#endif // VARIABLETOKEN_H
