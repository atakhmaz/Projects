#include "token.h"

Token::Token():_str()
{
}
Token::Token(MyString str, int type):_str(str), _type(type)
{
}
ostream& operator << (ostream& outs, const Token& S)
{
    outs <<"[" << S._str << "," << S._type << "]";
    return outs;
}
int Token::returnType()
{
    return _type;
}
MyString Token::returnToken()
{
    return _str;
}
