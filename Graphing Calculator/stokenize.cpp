#include "stokenize.h"

sTokenize::sTokenize()
{
}
sTokenize::sTokenize(const MyString str):_str(str), _pos(0), _more(true)
{
}
Token sTokenize::NextToken()
{
    if(_str.empty())
        throw EMPTY;

    MyString  SPACE (" "), VARIABLE ("x"), NUMBER ("0123456789."), OPERATOR  ("^*/+-"), PARAGRAPH ("\n"),
            OPEN_PARAN ("("), CLOSE_PARAN (")"), FUNCS("lscteaq"), NEGATIVE("n");

    char  Variable [2] = {"x"}, Number [12]= {"0123456789."}, Operator [6] = {"^*/+-"} ,
            open_paran[2] = {"("}, close_paran[2] = {")"}, funcs[8] = {"lscteaq"}, negative[2] = {"n"};

    int start, length;
    MyString returnString;

    if (_pos > 0)
        _pos--;

    if (SPACE.find(_str.at(_pos)) >= 0)
    {
        throw SPACE;
    }
    else if (VARIABLE.find(_str.at(_pos)) >= 0)
    {
        start = _str.find_first_of(Variable,_pos);
        length = _str.find_first_not_of(Variable,_pos);
        _pos = length + 1;
        returnString = _str.substr(start , length - start);
        _more = true;
        return Token(returnString,VARIABLE_TYPE);
    }
    else if (NUMBER.find(_str.at(_pos)) >= 0)
    {
        start = _str.find_first_of(Number,_pos);
        length = _str.find_first_not_of(Number,_pos);
        _pos = length + 1;
        returnString = _str.substr(start , length - start);
        _more = true;
        return Token(returnString,NUMBER_TYPE);
    }
    else if (OPERATOR.find(_str.at(_pos)) >= 0)
    {
        start = _str.find_first_of(Operator,_pos);
        _pos = start + 2;
        returnString = _str.substr(start ,1);
        _more = true;
        return Token(returnString,OPERATOR_TYPE);
    }
    else if (PARAGRAPH.find(_str.at(_pos)) >= 0)
    {
        throw PARAGRAPH;
    }
    else if (OPEN_PARAN.find(_str.at(_pos)) >= 0)
    {
        start = _str.find_first_of(open_paran,_pos);
        _pos = start + 2;
        returnString = _str.substr(start ,1);
        _more = true;
        return Token(returnString,OPEN_PARAN_TYPE);
    }
    else if (CLOSE_PARAN.find(_str.at(_pos)) >= 0)
    {
        start = _str.find_first_of(close_paran,_pos);
        _pos = start + 2;
        returnString = _str.substr(start ,1);
        _more = true;
        return Token(returnString,CLOSE_PARAN_TYPE);
    }
    else if (FUNCS.find(_str.at(_pos)) >= 0)
    {
        start = _str.find_first_of(funcs,_pos);
        _pos = start + 2;
        returnString = _str.substr(start ,1);
        _more = true;
        return Token(returnString,FUNCS_TYPE);
    }
    else if (NEGATIVE.find(_str.at(_pos)) >= 0)
    {
        start = _str.find_first_of(negative,_pos);
        _pos = start + 2;
        returnString = _str.substr(start ,1);
        _more = true;
        return Token(returnString,NEGATIVE_TYPE);
    }
    else
        throw UNIDENTIFIED_CHAR;
}
bool sTokenize::more()
{
    if(_pos < _str.length()+1)
        return true;
    else
        return false;
}
int sTokenize::pos()
{
    return _pos;
}
bool sTokenize::fail()
{
    return _more;
}
