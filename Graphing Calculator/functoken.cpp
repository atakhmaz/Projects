#include "functoken.h"

funcToken::funcToken()
{
    //Empty
}
funcToken::funcToken(MyString func): Token(func,FUNCS_TYPE), _func(func)
{
    //Empty
}
int funcToken::returnType()
{
    return FUNCS_TYPE; // Function type
}
MyString funcToken::returnFunction()
{
    return _func; // Function
}
int funcToken::Precedence()
{
    return FUNCTION_PRECEDENCE; // Function Precedence
}
int funcToken::funcType()
{
    // Function Type
    if(_func.compare("s") == 0)     //Sine
        return SINE_TYPE;
    else if(_func.compare("c") == 0)//Cosine
        return COSINE_TYPE;
    else if(_func.compare("t") == 0)//Tangent
        return TANGENT_TYPE;
    else if(_func.compare("l") == 0)//Log
        return LOG_TYPE;
    else if(_func.compare("e") == 0)//e
        return E_TYPE;
    else if(_func.compare("a") == 0)//Absolute Value
        return ABS_TYPE;
    else if(_func.compare("q") == 0)//Square Root
        return SQRT_TYPE;
    else
        throw FUNC_TYPE_ERROR; // Throws an error if the incorrect function is found
}
