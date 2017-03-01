#ifndef MYCSTRINGFUNCTIONS_H
#define MYCSTRINGFUNCTIONS_H

#include <iostream>

using namespace std;

int strlen(char* s);
// Returns the length of a cstring as an integer
void strcpy (char* dest, char* source);
// Copy source cstring into the destination cstring
void strcat(char* dest, char* source);
// Copy source cstring to the end of destingation cstring (adding two cstrings)
char* StrChr(char* source, char key);
// Search a cstring for a character and return a cahracter pointer of that position
char* firstof (char* S, char* charset, int start);
// Find first instance of any characters from charset in S
char* firstnotof (char* S, char* charset, int start);
// Find first instance of any characters from charset not in S
void strsub (char* source, char* sub, int start, int length);
// Copy a subtring of length from a source cstring from a start position to a substring
int strcmp(char* s1, char* s2);
// Compare two strings and output 0 if they are the same, -1 if s1 has higher alpabetical value, and 1 if s2 has higher alpabetical value
char* StrStr(char* source, char* findThis);
// Returns a pointer to the position that findThis is found in source cstring
char* rStrChr(char* source, char key);
// This is StrChr but going backwards in the cstring
void entrydelete(char *source, int pos, int length);
// Delete an entry starting at pos until specified length
void entryreplace(char *source, int pos, int length, char *entry);
// Replaces an entry starting at pos with the new entry


#endif // MYCSTRINGFUNCTIONS_H
