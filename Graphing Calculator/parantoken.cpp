#include "parantoken.h"

paranToken::paranToken()
{
    //Empty
}
paranToken::paranToken(MyString op):Token(op,OPEN_PARAN_TYPE), paran(op)
{
    //Empty
}
int paranToken::returnType()
{
    return OPEN_PARAN_TYPE; // Operator Type
}
int paranToken::Precedence()
{
    return PARAN_PRECEDENCE; // Parantheses Precedence
}
