#ifndef STACK_H
#define STACK_H

#include "linklistfuncs.h"

template <typename ITEM_TYPE>
class Stack
{
public:
    Stack();
    ~Stack();
    Stack(const Stack<ITEM_TYPE>& other);              // Big Three
    Stack<ITEM_TYPE>& operator= (const Stack<ITEM_TYPE>& RHS);
    void Push(ITEM_TYPE item);
    // Inserts a node to the top of a Stack
    ITEM_TYPE Pop();
    // Removes a node from the top of a Stack
    bool Empty();
    // Checks if Stack is empty
    ITEM_TYPE Peek();
    // Returns top of Stack
    void Print();
    // Prints Stack

private:
    node<ITEM_TYPE>* _top;
};

template <typename ITEM_TYPE>
Stack<ITEM_TYPE>::Stack(): _top(NULL)
{
}
template <typename ITEM_TYPE>
Stack<ITEM_TYPE>::~Stack()
{
    _EraseAll(_top);
}
template <typename ITEM_TYPE>
Stack<ITEM_TYPE>::Stack(const Stack<ITEM_TYPE>& other)
{
    _top = _Copy(other._top);
}
template <typename ITEM_TYPE>
Stack<ITEM_TYPE>& Stack<ITEM_TYPE>::operator= (const Stack<ITEM_TYPE>& RHS)
{
    if (this != &RHS)
    {
        _EraseAll(_top);
        _top = _Copy(RHS._top);
    }
    return *this;
}
template <typename ITEM_TYPE>
void Stack<ITEM_TYPE>::Push(ITEM_TYPE item)
{
    _InsertHead(_top,item);
}
template <typename ITEM_TYPE>
ITEM_TYPE Stack<ITEM_TYPE>::Pop()
{
    return _Delete(_top,_top);
}
template <typename ITEM_TYPE>
bool Stack<ITEM_TYPE>::Empty()
{
    return _Empty(_top);
}
template <typename ITEM_TYPE>
ITEM_TYPE Stack<ITEM_TYPE>::Peek()
{
    return _top->_item;
}
template <typename ITEM_TYPE>
void Stack<ITEM_TYPE>::Print()
{
    _Print(_top);
}

#endif // STACK_H
