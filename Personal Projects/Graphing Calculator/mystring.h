#ifndef MYSTRING_H
#define MYSTRING_H

#include "mycstringfunctions.h"

class MyString
{
public:
    MyString();
    // Default constructor
    MyString(char *s);
    // Constructor to initilize a variable
    MyString(const MyString& other);
    // Copy constructor
    ~MyString();
    // Destructor
    void Print();
    // Print string
    int length();
    // Return length of string as integer
    int find(char ch);
    // Find position of character in string
    void copy(MyString source);
    // Copy one string into another
    friend ostream& operator << (ostream& outs,const MyString& S);
    // Insertion operator
    friend istream& operator >> (istream& ins,MyString& S);
    // Extraction operator
    int find_first_of (char* charset, int start);
    // Find first instance of any characters from charset in string
    int find_first_not_of (char* charset, int start);
    // Find first instance of any characters from charset not in string
    MyString substr (int start, int length);
    // Exract a substring from position with specified length
    int compare(MyString str);
    // Compare two strings and output 0 if they are the same, -1 if s1 has higher alpabetical value, and 1 if s2 has higher alpabetical value
    int rfind(char ch);
    // Find the position of the last occurance of the character in string
    char* c_str();
    // Return cstring equivalent of string
    void clear();
    // Clear string
    bool empty();
    // Check of string is empty
    char& operator[](int pos);
    // Bracket operator, same as "at"
    char& at(int pos);
    // Retruns character in the specified position of string
    void operator += (const MyString& S);
    // Append to a string
    void append(const MyString& S);
    // Add and string to the end of a string
    void push_back (char c);
    // Add a character to the end of a string
    void assign (const MyString& str);
    // Assign new content to a string and remove old content
    void insert(int pos, MyString &str);
    // Insert a string at specified position
    void erase(int pos, int length);
    // Erase part of specified length at specified specified position
    void replace(int pos, int length, MyString S);
    // Replace part of a string starting and specified position with specified length with the string
    MyString& operator = (const MyString& RHS);
    // Assignment operator

private:

    char* _s;
    int _len;

};

#endif // MYSTRING_H
