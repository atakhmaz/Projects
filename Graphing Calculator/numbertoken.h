#ifndef NUMBERTOKEN_H
#define NUMBERTOKEN_H

#include "token.h"
#include <cstdlib>

class numberToken: public Token // Child of Token object (inheritance)
{
public:
    numberToken();
    numberToken(MyString num);
    // Calls the parent constructor to make an object and also assigns the string to the private memember variable
    numberToken(double num);
    // Constructs the object using a double
    int returnType();
    // Returns the type of token it is (20)
    double returnNumber();
    // Returns the value of the object (double number)

private:
    double d; // Private member variable of double type for the number
};

#endif // NUMBERTOKEN_H
