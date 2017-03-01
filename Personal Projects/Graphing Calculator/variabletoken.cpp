#include "variabletoken.h"

variableToken::variableToken()
{
    //Empty
}
variableToken::variableToken(MyString value):Token(value,VARIABLE_TYPE), var(value)
{
    //Empty
}
int variableToken::returnType()
{
    return VARIABLE_TYPE; // Variable Type
}
MyString variableToken::returnVariable()
{
    return var; // Variable
}
bool variableToken::isNegative()
{
    // Checks if variable is the negative variable or if its is a regular variable
    if(var.compare("n") == 0)
        return true;
    else
        return false;
}
