#ifndef EVALUATE_H
#define EVALUATE_H

#include "stokenize.h" // Used to tokenize the math expression
#include "queue.h" // Used as a container for postfix
#include "stack.h" // Used as a temporary container for checking operator precedence and evaulation numbers(double)
#include "numbertoken.h" // Number object (Token Child)
#include "operatortoken.h" // Operator object (Token Child)
#include "parantoken.h" // Parantheses Object (Token Child)
#include <cmath> // Library of math functions used to calculate functions (sin,e,log,etc...)
#include "variabletoken.h" // Variable object (Token Child)
#include "functoken.h" // Function object (Token Child)

Queue<Token*> EvalInfix(MyString expression);
// Takes a Math Expression as a string and goes throught the shunting yard algorithm to produce a postfix expression and also gives each expression its own personal objects (variable, function, number, etc...)
double EvalPostfix(Queue<Token*> Q, double evaluateAt);
// Takes a postfix Queue of Token pointers (objects of parent) and evaluates the Queue, if a variable is present, the expression is evaluated with value evaluateAt

#endif // EVALUATE_H
