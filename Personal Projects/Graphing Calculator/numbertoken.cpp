#include "numbertoken.h"

numberToken::numberToken()
{
    //Empty
}
numberToken::numberToken(MyString num):Token(num,NUMBER_TYPE), d(atof(num.c_str()))
{
    //Empty
}
numberToken::numberToken(double num):d(num)
{
    //Empty
}
int numberToken::returnType()
{
    return NUMBER_TYPE; // Number type
}
double numberToken::returnNumber()
{
    return d; // Number
}
