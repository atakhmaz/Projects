#ifndef FUNCTOKEN_H
#define FUNCTOKEN_H

#include "token.h"

class funcToken: public Token // Child of Token object (inheritance)
{
public:
    funcToken();
    funcToken(MyString func);
    // Calls the parent constructor to make an object and also assigns the string to the private memember variable
    int returnType();
    // Returns the type of token it is (60)
    MyString returnFunction();
    // Returns the function
    int Precedence();
    // Returns the predence of the function (-11, lowest precedence)
    int funcType();
    // Checks what type of function the object is and returns the correct function type to be evaluated

private:
    MyString _func; // Private member variable of string type for the function
};

#endif // FUNCTOKEN_H
