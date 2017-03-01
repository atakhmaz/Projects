#ifndef LINKLISTFUNCS_H
#define LINKLISTFUNCS_H

#include "node.h"

template <typename ITEM_TYPE>
void _Print(node<ITEM_TYPE>* head);
// Prints the entire linked list
template <typename ITEM_TYPE>
node<ITEM_TYPE>* _Search(node<ITEM_TYPE>* head, ITEM_TYPE key);
// Return a node pointer of the node containing the specified item
template <typename ITEM_TYPE>
void _InsertHead(node<ITEM_TYPE>*& head, ITEM_TYPE insertMe);
// Insert a new node<ITEM_TYPE> at the beggining of a linked list
template <typename ITEM_TYPE>
node<ITEM_TYPE>* _Previous(node<ITEM_TYPE>* head, node<ITEM_TYPE>* here);
// Return a node pointer of the node before the specified node
template <typename ITEM_TYPE>
void _InsertAfter(node<ITEM_TYPE> *here, ITEM_TYPE insertMe);
// Insert a new node<ITEM_TYPE> after the specified node
template <typename ITEM_TYPE>
void _InsertBefore(node<ITEM_TYPE> *&head, node<ITEM_TYPE>* here, ITEM_TYPE insertMe);
// Insert a new node<ITEM_TYPE> before the specified node
template <typename ITEM_TYPE>
void _Replace (node<ITEM_TYPE> *&head, node<ITEM_TYPE>* here, ITEM_TYPE replaceMe);
// Replaces a node at the specified node
template <typename ITEM_TYPE>
void _InsertSorted(node<ITEM_TYPE>*& head, ITEM_TYPE item);
// Inserts a node in a sorted linked list, smallest to largest
template <typename ITEM_TYPE>
ITEM_TYPE _Delete (node<ITEM_TYPE>*& head, node<ITEM_TYPE>* here);
// Deletes a specified node and returns the value
template <typename ITEM_TYPE>
node<ITEM_TYPE>* _Copy(node<ITEM_TYPE>* head);
// Copies and enitre linked list
template <typename ITEM_TYPE>
ITEM_TYPE& _At(node<ITEM_TYPE> *head, int i);
// Returns the item inside the specified node in the list
template <typename ITEM_TYPE>
int _Count(node<ITEM_TYPE>* head);
// Returns the number of nodes in a linked list
template <typename ITEM_TYPE>
void _Reverse(node<ITEM_TYPE>*& head);
// Reverses a linked list
template <typename ITEM_TYPE>
void _EraseAll(node<ITEM_TYPE>*& head);
// Erases all the nodes in a linked list
template <typename ITEM_TYPE>
bool _Empty(node<ITEM_TYPE>* head);
// Returns whether the linked list is empty
template <typename ITEM_TYPE>
node<ITEM_TYPE>* _Merge(node<ITEM_TYPE>* head1, node<ITEM_TYPE>* head2);
// Returns a new linked list that has two other linked lists merged into it; both linked lists must be sorted and unique

template <typename ITEM_TYPE>
void _Print(node<ITEM_TYPE>* head)
{
    node<ITEM_TYPE>* walker = head;
    while (walker != NULL)
    {
        cout << *walker;
        walker = walker->next;
    }
}
template <typename ITEM_TYPE>
node<ITEM_TYPE>* _Search(node<ITEM_TYPE>* head, ITEM_TYPE key)
{
    node<ITEM_TYPE>* walker = head;
    while (walker != NULL)
    {
        if(walker->_item == key)
            return walker;
        else
            walker = walker->next;
    }
    return NULL;
}
template <typename ITEM_TYPE>
void _InsertHead(node<ITEM_TYPE>*& head, ITEM_TYPE insertMe)
{
    node<ITEM_TYPE>* temp = new node<ITEM_TYPE>(insertMe);
    temp->next = head;
    head = temp;
}
template <typename ITEM_TYPE>
node<ITEM_TYPE>* _Previous(node<ITEM_TYPE>* head, node<ITEM_TYPE>* here)
{
    node<ITEM_TYPE>* walker = head;
    node<ITEM_TYPE>* shadow = head;

    while (walker != here && walker != NULL)
    {
        shadow = walker;
        walker = walker->next;
    }
    if(walker == head && here != NULL)
        return walker;
    else if(here == NULL)
        return NULL;
    else
        return shadow;
}
template <typename ITEM_TYPE>
void _InsertAfter(node<ITEM_TYPE>* here, ITEM_TYPE insertMe)
{
    node<ITEM_TYPE>* temp = new node<ITEM_TYPE>(insertMe);
    if(here != NULL)
    {
        temp->next = here->next;
        here->next = temp;
    }
    else
    {
        _InsertHead(here,insertMe);
    }
}
template <typename ITEM_TYPE>
void _InsertBefore(node<ITEM_TYPE>*& head, node<ITEM_TYPE>* here, ITEM_TYPE insertMe)
{
    if(head == NULL || head == here)
        _InsertHead(head,insertMe);
    else
    {
        node<ITEM_TYPE>* temp = new node<ITEM_TYPE>(insertMe);
        temp->next = here;
        _Previous(head,here)->next=temp;
    }
}
template <typename ITEM_TYPE>
void _Replace (node<ITEM_TYPE>*& head, node<ITEM_TYPE>* here, ITEM_TYPE replaceMe)
{
    if(head != NULL && here != NULL)
    {
        node<ITEM_TYPE>* temp = new node<ITEM_TYPE>(replaceMe);
        temp->next = here->next;
        _Previous(head, here)->next = temp;
        delete here;
    }
}
template <typename ITEM_TYPE>
void _InsertSorted (node<ITEM_TYPE>*& head, ITEM_TYPE item)
{
    node<ITEM_TYPE>* temp = new node<ITEM_TYPE>(item);
    node<ITEM_TYPE>* walker = head;
    node<ITEM_TYPE>* shadow = head;

    while(walker != NULL && walker->_item < item)
    {
        shadow = walker;
        walker = walker->next;
    }
    if(walker == NULL && shadow != walker)
    {
        shadow->next = temp;
        temp->next = NULL;
    }
    else if(walker == NULL)
    {
        _InsertHead(head,item);
    }
    else if (item != walker->_item)
    {
        _InsertBefore(head,walker,item);
    }
}
template <typename ITEM_TYPE>
ITEM_TYPE _Delete(node<ITEM_TYPE>*& head, node<ITEM_TYPE>* here)
{
    if(here != NULL)
    {
        ITEM_TYPE temp (here->_item);
        if(head == here)
        {
            head = here->next;
            delete here;
        }
        else
        {
            _Previous(head,here)->next = here->next;
            delete here;
        }
        return temp;
    }
}
template <typename ITEM_TYPE>
node<ITEM_TYPE>* _Copy (node<ITEM_TYPE>* head)
{
    node<ITEM_TYPE>* newHead = NULL;
    node<ITEM_TYPE>* walker = head;
    node<ITEM_TYPE>* shadow;
    if(head != NULL)
    {
        while (walker != NULL)
        {
            shadow = walker;
            walker = walker->next;
        }
        while(shadow != head)
        {
            _InsertHead(newHead,shadow->_item);
            shadow = _Previous(head,shadow);
        }
        _InsertHead(newHead,shadow->_item);
    }
    return newHead;
}
template <typename ITEM_TYPE>
ITEM_TYPE &_At(node<ITEM_TYPE>* head, int i)
{
    if(head != NULL && i <= _Count(head))
    {
        node<ITEM_TYPE>* walker = head;
        for (int j = 0; j<i; j++)
        {
            walker = walker->next;
        }
        return walker->_item;
    }
    exit(0);
}
template <typename ITEM_TYPE>
int _Count(node<ITEM_TYPE>* head)
{
    int count = 0;
    if(head != NULL)
    {
        node<ITEM_TYPE>* walker = head;
        while(walker != NULL)
        {
            count++;
            walker = walker->next;
        }
    }
    return count;
}
template <typename ITEM_TYPE>
void _Reverse(node<ITEM_TYPE>*& head)
{
    if(head == NULL)
        return;
    node<ITEM_TYPE>* front = NULL;
    node<ITEM_TYPE>* rear = NULL;
    node<ITEM_TYPE>* walker = head;
    while(walker != NULL)
    {
        front = walker->next;
        walker->next = rear;
        rear = walker;
        walker = front;
    }
    head = rear;
}
template <typename ITEM_TYPE>
void _EraseAll(node<ITEM_TYPE>*& head)
{
    node<ITEM_TYPE>* temp;
    node<ITEM_TYPE>* walker = head;
    while (walker != NULL)
    {
        temp = walker->next;
        delete walker;
        walker = temp;
    }
    head = NULL;
}
template <typename ITEM_TYPE>
bool _Empty(node<ITEM_TYPE>* head)
{
    if(head == NULL)
        return true;
    else
        return false;
}
template <typename ITEM_TYPE>
node<ITEM_TYPE>* _Merge(node<ITEM_TYPE>* head1, node<ITEM_TYPE>* head2)
{
    if(head1 == NULL)
        return head2;
    else if (head2 == NULL)
        return head1;

    node<ITEM_TYPE>* walker1 = head1;
    node<ITEM_TYPE>* walker2 = head2;
    node<ITEM_TYPE>* returnList = NULL;

    while (walker1 != NULL && walker2 != NULL)
    {
        if(walker1 != NULL && walker1->_item < walker2->_item)
        {
            _InsertHead(returnList,walker1->_item);
            walker1 = walker1->next;
        }
        else if(walker2 != NULL && walker1->_item > walker2->_item)
        {
            _InsertHead(returnList,walker2->_item);
            walker2 = walker2->next;
        }
    }
    if(walker1 == NULL)
    {
        while (walker2 != NULL)
        {
            _InsertHead(returnList,walker2->_item);
            walker2 = walker2->next;
        }
    }
    else if (walker2 == NULL)
    {
        while (walker1 != NULL)
        {
            _InsertHead(returnList,walker1->_item);
            walker1 = walker1->next;
        }
    }
    _Reverse(returnList);
    return returnList;
}

#endif // LINKLISTFUNCS_H
