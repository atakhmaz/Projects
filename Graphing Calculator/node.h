#ifndef NODE_H
#define NODE_H

template <typename ITEM_TYPE>
struct node
{
public:
    node();
    node(ITEM_TYPE item);
    // Node constructor, sets next to null and _item to item
    template <typename T>
    friend ostream& operator <<(ostream& outs, const node<T> &printMe);
    // Allows to print nodes

    ITEM_TYPE _item; // Item in the linked list
    node<ITEM_TYPE>* next; // Pointer to the next item in the linked list
};

template <typename ITEM_TYPE>
node<ITEM_TYPE>::node(): _item(), next(NULL)
{
}
template <typename ITEM_TYPE>
node<ITEM_TYPE>::node(ITEM_TYPE item): _item(item), next(NULL)
{
}
template <typename T>
ostream& operator << (ostream& outs, const node<T> &printMe){
    outs << "[" << printMe._item << "]-> ";
    return outs;
}

#endif // NODE_H
