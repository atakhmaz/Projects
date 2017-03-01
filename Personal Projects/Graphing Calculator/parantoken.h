#ifndef PARANTOKEN_H
#define PARANTOKEN_H

#include "token.h"

class paranToken: public Token // Child of Token object (inheritance)
{
public:
    paranToken();
    paranToken(MyString op);
    // Calls the parent constructor to make an object and also assigns the string to the private memember variable
    int Precedence();
    // Returns the predence of the function (-10, second lowest precedence), probably not important to have
    int returnType();
    // Returns the type of token it is (40)

private:
    MyString paran;
};

#endif // PARANTOKEN_H
