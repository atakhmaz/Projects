#ifndef OPERATORTOKEN_H
#define OPERATORTOKEN_H

#include "token.h"

class operatorToken: public Token // Child of Token object (inheritance)
{
public:
    operatorToken();
    operatorToken(MyString op);
    // Calls the parent constructor to make an object and also assigns the string to the private memember variable
    int Precedence();
    // Returns the predence of the operator based on basic math logic
    int returnType();
    // Returns the type of token it is (30)
    MyString returnOperator();
    // Returns the operator as a string
    int operatorType();
    // Returns the correct type of operator that object is to be evaluated using the correct function

private:
    MyString _op; // Private member variable of string type for the operator
};

#endif // OPERATORTOKEN_H
