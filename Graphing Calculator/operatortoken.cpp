#include "operatortoken.h"

operatorToken::operatorToken()
{
    //Empty
}
operatorToken::operatorToken(MyString op): Token(op,OPERATOR_TYPE), _op(op)
{
    //Empty
}
int operatorToken::Precedence()
{
    // Returns precedence of the correct token
    if(_op.compare("^") == 0)        //E
        return EXPONENT_PRECEDENCE;
    else if(_op.compare("*") == 0)   //M
        return MULTIPLY_PRECEDENCE;
    else if(_op.compare("/") == 0)   //D
        return DIVIDE_PRECEDENCE;
    else if(_op.compare("+") == 0)   //A
        return ADD_PRECEDENCE;
    else if(_op.compare("-") == 0)   //S
        return SUBTRACT_PRECEDENCE;
    else
        throw OPERATOR_PRECEDENCE; // Throws and error if incorrect operator is found
}
int operatorToken::returnType()
{
    return OPERATOR_TYPE; // Operator type
}
MyString operatorToken::returnOperator()
{
    return _op; // Operator
}
int operatorToken::operatorType()
{
    // Returns operator type
    if(_op.compare("^") == 0)        //Exponent
        return EXPONENT_TYPE;
    else if(_op.compare("*") == 0)   //Multiply
        return MULTIPLY_TYPE;
    else if(_op.compare("/") == 0)   //Divide
        return DIVIDE_TYPE;
    else if(_op.compare("+") == 0)   //Add
        return ADD_TYPE;
    else if(_op.compare("-") == 0)   //Subtract
        return SUBTRACT_TYPE;
}
