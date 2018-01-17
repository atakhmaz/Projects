#ifndef QUEUE_H
#define QUEUE_H

#include "linklistfuncs.h"

template <typename ITEM_TYPE>
class Queue
{
public:
    Queue();
    ~Queue();
    Queue(const Queue<ITEM_TYPE>& other);              // Big Three
    Queue<ITEM_TYPE>& operator= (const Queue<ITEM_TYPE>& RHS);
    void Push(ITEM_TYPE item);
    // Inserts a node to the end of a queue
    ITEM_TYPE Pop();
    // Removes a node from the beginning of a queue
    bool Empty();
    // Checks if queue is empty
    ITEM_TYPE Peek();
    // Returns first in queue
    void Print();
    // Prints queue

private:
    node<ITEM_TYPE>* _front;
    node<ITEM_TYPE>* _rear;
};

template <typename ITEM_TYPE>
Queue<ITEM_TYPE>::Queue()
{
    _front = _rear = NULL;
}
template <typename ITEM_TYPE>
Queue<ITEM_TYPE>::~Queue()
{
    _EraseAll(_front);
}
template <typename ITEM_TYPE>
Queue<ITEM_TYPE>::Queue(const Queue<ITEM_TYPE>& other)
{
    _front = _Copy(other._front);
}
template <typename ITEM_TYPE>
Queue<ITEM_TYPE>& Queue<ITEM_TYPE>::operator= (const Queue<ITEM_TYPE>& RHS)
{
    if (this != &RHS)
    {
        _EraseAll(_front);
        _front = _Copy(RHS._front);
    }
    return *this;
}
template <typename ITEM_TYPE>
void Queue<ITEM_TYPE>::Push(ITEM_TYPE item)
{
    if(Empty())
    {
        _InsertHead(_front,item);
        _rear = _front;
    }
    else
    {
        _InsertAfter(_rear,item);
        _rear = _rear->next;
    }
}
template <typename ITEM_TYPE>
ITEM_TYPE Queue<ITEM_TYPE>::Pop()
{
    return _Delete(_front,_front);
}
template <typename ITEM_TYPE>
bool Queue<ITEM_TYPE>::Empty()
{
    return _Empty(_front);
}
template <typename ITEM_TYPE>
ITEM_TYPE Queue<ITEM_TYPE>::Peek()
{
    return _front->_item;
}
template <typename ITEM_TYPE>
void Queue<ITEM_TYPE>::Print()
{
    _Print(_front);
}

#endif // QUEUE_H
