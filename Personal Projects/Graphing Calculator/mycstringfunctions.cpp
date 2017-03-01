#include "mycstringfunctions.h"

int strlen(char* s)
{
    int count = 0;
    while (*s != '\0')
    {
        count++;
        s++;
    }
    return count;
}
void strcpy (char* dest, char* source)
{
    while(*source != '\0')
    {
        *dest=*source;
        dest++;
        source++;
    }
    *dest='\0';
}
void strcat(char* dest, char* source)
{
    dest = dest + strlen(dest);
    strcpy(dest,source);
}
char* StrChr (char* source, char key)
{
    while (*source != '\0')
    {
        if (*source == key)
            return source;
        else
            source++;
    }
    return NULL;
}
char* firstof (char* S, char* charset, int start)
{
    S = S + start;
    while (S <= strlen(S)+S)
    {
        if (StrChr(charset, *S) != NULL)
            return S;
        else
            S++;
    }
    return NULL;
}
char* firstnotof (char* S, char* charset, int start)
{
    S = S + start;
    while (S <= strlen(S)+S)
    {
        if (StrChr(charset, *S) == NULL)
            return S;
        else
            S++;
    }
    return NULL;
}
void strsub (char* source, char* sub, int start, int length)
{
    source = source + start;
    for (int i = 0; i < length; i++)
    {
        *sub++=*source++;
    }
    *sub = '\0';
}
int strcmp(char *s1, char *s2)
{
    while(*s1 != '\0' || *s2 != '\0')
    {
        if (*s1 != *s2)
        {
            if (*s1 > *s2)
                return -1;
            else if (*s1 < *s2)
                return 1;
        }
        s1++;
        s2++;
    }
    return 0;
}
char* StrStr(char* source, char* findThis)
{
    char* sub = new char [100];
    while ((strlen(source) - strlen(findThis)) >= 0)
    {
        strsub(source, sub, 0, strlen(findThis));
        if (strcmp(sub, findThis) == 0)
        {
            delete[] sub;
            return source;
        }
        source++;
    }
    delete[] sub;
    return NULL;
}
char* rStrChr(char* source, char key)
{
    source = source + (strlen(source)-1);
    while (*source != '\0')
    {
        if (*source == key)
            return source;
        else
            source--;
    }
    return NULL;
}
void entryreplace(char *source, int pos, int length, char* entry)
{
    int tempsize = strlen(source) - pos + length + (strlen(source) - pos) ;

    source = source + pos;

    for (int i = 0; i < tempsize; i++)
    {
        *source=*entry;
        source++;
        entry++;
    }
    *source = '\0';
}
void entrydelete(char* source, int pos, int length)
{
    int tempsize = strlen(source) - pos;

    source = source + pos;

    for (int i = 0; i < tempsize; i++)
    {
        *source=*(source + length);
        source++;
    }
    *source = '\0';
}
