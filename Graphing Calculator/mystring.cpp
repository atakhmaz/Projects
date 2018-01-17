#include "mystring.h"

MyString::MyString():_s(NULL), _len(0)
{
}
MyString::MyString(char *s)
{
    _len = strlen(s);
    _s = new char [_len+1];
    strcpy(_s,s);
}
MyString::MyString(const MyString& other)
{
    _len = strlen(other._s);
    _s = new char [_len + 1];
    strcpy(_s, other._s);
}
MyString::~MyString()
{
    delete [] _s;
}
MyString& MyString::operator = (const MyString& RHS)
{
    if (this != &RHS)
    {
        delete[] _s;
        _len = strlen(RHS._s);
        _s = new char[_len+1];
        strcpy(_s, RHS._s);
    }
    return *this;
}
void MyString::Print()
{
    cout << _s;
}
int MyString::length()
{
    return strlen(_s);
}
int MyString::find(char ch)
{
    char* found = StrChr(_s,ch);
    if (found == NULL)
        return -1;
    else
        return found - _s;
}
void MyString::copy(MyString source)
{
    _len = _len+source._len;
    char* hold = new char [_len + 1];
    strcpy( hold, source._s);
    delete [] _s;
    _s= hold;
}
int MyString::find_first_of (char* charset, int start)
{
    char* found = firstof(_s,charset,start);
    if (found == NULL)
        return -1;
    else
        return found - _s;
}
int MyString::find_first_not_of (char* charset, int start)
{
    char* found = firstnotof(_s,charset,start);
    if (found == NULL)
        return -1;
    else
        return found - _s;
}
MyString MyString::substr (int start, int length)
{
    MyString sub;
    _len = strlen(_s);
    sub._s = new char [_len+1];
    strsub(_s,sub._s,start,length);
    return sub;
}
int  MyString::compare(MyString str)
{
    return strcmp(_s,str._s);
}
int MyString::rfind(char ch)
{
    char* found = rStrChr(_s,ch);
    if (found != NULL)
        return found - _s;
    else
        return -1;
}
char* MyString::c_str()
{
    return _s;
}
ostream& operator << (ostream& outs, const MyString& S)
{
    outs << S._s;
    return outs;
}
istream& operator >> (istream& ins, MyString& S)
{
    ins >> S._s;
    S._len = strlen(S._s);
    return ins;
}
void MyString::clear()
{
    *_s = '\0';
}
bool MyString::empty()
{
    if (strlen(_s) <= 0)
        return true;
    else
        return false;
}
char& MyString::operator[](int pos)
{
    return at(pos);
}
char& MyString::at(int pos)
{
    return *(_s + pos);
}
void MyString::operator += (const MyString& S)
{
    return append(S);
}
void MyString::append (const MyString& S)
{
    _len = _len+S._len;
    char* hold = new char [_len+1];
    strcpy(hold,_s);
    strcat(hold,S._s);
    delete [] _s;
    _s = hold;
}
void MyString::push_back (char c)
{
    _len = strlen(_s);
    _len++;
    char* hold = new char [_len + 1];
    strcpy(hold,_s);
    strcat(hold,&c);
    *(hold + _len)= '\0';
    delete [] _s;
    _s = hold;
}
void MyString::assign (const MyString& str)
{
    _len = _len+str._len;
    char* hold = new char [_len+1];
    strcpy(hold,str._s);
    delete [] _s;
    _s = hold;
}
void MyString::insert(int pos, MyString& str)
{
    MyString first;
    MyString second;

    _len = strlen(_s);
    first._s = new char [_len];
    second._s = new char [_len];

    strsub(_s,first._s,0,pos);
    strsub(_s,second._s,pos, _len);

    _len = _len + str._len;
    char* hold = new char [_len+1];
    *hold = '\0';

    strcat(hold,first._s);
    strcat(hold,str._s);
    strcat(hold,second._s);

    delete [] _s;

    _s = hold;
}
void MyString::erase(int pos, int length)
{
    entrydelete(_s, pos, length);
}
void MyString::replace(int pos, int length, MyString S)
{
    MyString first;
    MyString second;

    _len = strlen(_s);
    first._s = new char [_len];
    second._s = new char [_len];

    strsub(_s,first._s, 0, pos);
    strsub(_s,second._s, pos + length, strlen(_s));

    _len = _len + S._len + 1;
    char* hold = new char [_len + 1];
    *hold = '\0';

    strcat(hold,first._s);
    strcat(hold,S._s);
    strcat(hold,second._s);

    delete [] _s;

    _s = hold;
}
