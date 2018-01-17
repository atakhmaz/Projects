#include "evaluate.h"

Queue<Token*> EvalInfix(MyString expression)
{
    try // Trys to execute this code and if something is wrong, it throws the appropriate error
    {
        sTokenize stk (expression); // Sets up the expression to be tokenized
        Queue <Token*>Q; // Sets up a Queue of Token pointers as a container
        Stack <Token*>S; // Sets up a Stack of Token pointers to hold operators for checking precedence
        Token token; // The tokens that are tokenized
        while(stk.more()) // A while to keep tokenizing if there are more tokens
        {
            token = stk.NextToken(); // Gives a token
            switch(token.returnType()) // A swtich statement that checks that type of token that was given
            {
            case(VARIABLE_TYPE):    // Variable Token
            {
                variableToken* varToken = new variableToken(token.returnToken()); // Creates a new object of variableToken type with the variable token
                Q.Push(varToken); // Pushes the variableToken into the queue
                break;
            }
            case(NUMBER_TYPE):    // Number Token
            {
                numberToken* numToken = new numberToken(token.returnToken()); // Creates a new object of numberToken type with the number token
                Q.Push(numToken); // Pushes the numberToken into the queue
                break;
            }
            case(OPERATOR_TYPE):    // Operator Token
            {
                operatorToken* opToken = new operatorToken(token.returnToken()); // Creates a new object of operatorToken type with the operator token
                while(!S.Empty() && S.Peek()->returnType() != OPEN_PARAN_TYPE && opToken->Precedence() >= static_cast<operatorToken*>(S.Peek())->Precedence()) // Checks precedence of the current token and the precedence of the token at the top of the stack
                {
                    Q.Push(S.Pop()); // Pops the token from the top of the stack and puts it into the queue as long as the precedence is lower or equal to the current operator token, the stacks isnt empty, or if the top of the stack isnt a parantheses
                }
                S.Push(opToken); // Pushes the current operator token into the stack
                break;
            }
            case(OPEN_PARAN_TYPE):    // ( Token
            {
                paranToken* paran = new paranToken(token.returnToken()); // Creates a new object of paranToken type with the open parantheses token
                S.Push(paran); // Pushes the paranToken into the queue
                break;
            }
            case(CLOSE_PARAN_TYPE):    // ) Token
            {
                while(!S.Empty() && S.Peek()->returnType() != OPEN_PARAN_TYPE) // Checks if stack isnt empty and the top of the stack isnt the open parantheses
                {
                    Q.Push(S.Pop()); // Pops everything from the stack and puts it into the queue as until it hits a open parantheses
                }
                S.Pop(); // Pops the open parantheses to complete the parantheses process
                break;
            }
            case(FUNCS_TYPE):    // Function Token
            {
                funcToken* funToken = new funcToken(token.returnToken()); // Creates a new object of funcToken type with the function token
                while(!S.Empty() && S.Peek()->returnType() != OPEN_PARAN_TYPE && funToken->Precedence() >= static_cast<operatorToken*>(S.Peek())->Precedence()) // This while loop operates the same way as the operators
                {
                    Q.Push(S.Pop()); // Pops the token from the top of the stack and puts it into the queue as long as the precedence is lower or equal to the current function token, the stacks isnt empty, or if the top of the stack isnt a parantheses
                }
                S.Push(funToken); // Pushes the current function token into the stack
                break;
            }
            case(NEGATIVE_TYPE):    // Negative Token (Variable)
            {
                variableToken* negative = new variableToken("n"); // Creates a new object of variableToken type with the negative token "n"
                Q.Push(negative); // Pushes the variableToken into the queue
                break;
            }
            default:
            {
                throw token; // Throws an error if the token is of one of these desired types
            }
            }
        }
        // At this point were are done tokenizing and the algorithm is over
        while(!S.Empty())
        {
            Q.Push(S.Pop()); // Pops the rest of the items from the stack and puts it into the queu
        }
        return Q; // Returns the newly created queue
    }
    catch(MyString error) // Catches error of type string, displays the error (unwanted characters), and exits the program
    {
        cout << "Error: Plesase remove the [" << error << "]'s from the math expression" << endl;
        exit(0);
    }
    catch(int error) // Catches error of type int, displays the error, and exits the program
    {
        switch(error) // Switch statement for the different types of errors
        {
        case(UNIDENTIFIED_CHAR): // Unidentified characters in the math expression
        {
            cout << "Error " << error << ": Please remove the unidentified characters from the math expression" << endl;
            exit(0);
            break;
        }
        case(EMPTY): // Empty math expression
        {
            cout << "Error " << error << ": The math expression is empty." << endl;
            exit(0);
            break;
        }
        }
    }
    catch(char error) // Catches error of type char, displays the error (invalid function), and exits the program
    {
        cout << "Error " << error << ": invalid function found." << endl;
        exit(0);
    }
}
// Takes a postfix Queue of Token pointers (objects of parent) and evaluates the Queue, if a variable is present, the expression is evaluated with value evaluateAt
double EvalPostfix(Queue<Token*> Q,double evaluateAt)
{
    Stack<double> S; // Creates a Stack of doubles that will be used to evaluate the numbers
    while(!Q.Empty()) // While the Queue is not empty we pop from it and evaluate
    {
        switch(Q.Peek()->returnType()) // Switch statement that checks the type of token it is and evaluates accordingly
        {
        case(VARIABLE_TYPE):    // Variable Type
        {
            if(static_cast<variableToken*> (Q.Peek())->isNegative() == false) // If the token is a variable x
            {
                Q.Pop(); // Pops the token from the stack
                S.Push(evaluateAt); // Push the value of the variable (evaluateAt) into the stack to be evaluated
                break;
            }
            else // If the token is the negative token
            {
                Q.Pop(); // Pops the token from the stack
                S.Push(-1.0); // Push the value of negative (-1) into the stack to be evaluated
                break;
            }
            break;
        }
        case(NUMBER_TYPE):    // Integer Type
        {
            S.Push(static_cast<numberToken*>(Q.Pop())->returnNumber()); // Pops the number into the stack to be evaluated
            break;
        }
        case(OPERATOR_TYPE):    // Operator Type
        {
            int type = static_cast<operatorToken*>(Q.Pop())->operatorType(); // Checks what type of operator it is to evaluate
            double temp1, temp2;
            if(!S.Empty()) // If the Stack is not empty it pops the number to be evaluated
            {
                temp1 = S.Pop();
            }
            else // Throws an error if there is no number found to be evaluated
            {
                throw EVAL_STACK_ERROR;
            }
            if(!S.Empty()) // If the Stack is not empty it pops the second number to be evaluated
            {
                temp2 = S.Pop();
            }
            else // Throws an error if there is no second number found to be evaluated
            {
                throw OPERATOR_ERROR;
            }
            switch(type) // Switch statement of operator types, evaluates the operator with the two top numbers in the stack and pushes that number into the stack again to be re evaluated
            {
            case(EXPONENT_TYPE): // Exponent Operator
            {
                S.Push(pow(temp2,temp1));
                break;
            }
            case(MULTIPLY_TYPE): // Multiply Operator
            {
                S.Push(temp2 * temp1);
                break;
            }
            case(DIVIDE_TYPE): // Divide Operator
            {
                S.Push(temp2 / temp1);
                break;
            }
            case(ADD_TYPE): // Add Operator
            {
                S.Push(temp2 + temp1);
                break;
            }
            case(SUBTRACT_TYPE): // Subtract Operator
            {
                S.Push(temp2 - temp1);
                break;
            }
            }
            break;
        }
        case(FUNCS_TYPE):    // Function Type
        {
            double temp1 = S.Pop(); // Pops that top of the stack to be evaluated with the proper function
            int type =static_cast<funcToken*> (Q.Pop())->funcType(); // Checks what type of function it is to evaluate
            switch(type) // Switch statement of function types, evaluates the function with the top number in the stack and pushes that number into the stack again to be re evaluated
            {
            case(SINE_TYPE): // Sine Type
            {
                S.Push(sin(temp1));
                break;
            }
            case(COSINE_TYPE): // Cosine Type
            {
                S.Push(cos(temp1));
                break;
            }
            case(TANGENT_TYPE): // Tangent Type
            {
                S.Push(tan(temp1));
                break;
            }
            case(LOG_TYPE): // Logarithm Type
            {
                S.Push(log(temp1));
                break;
            }
            case(E_TYPE): // E Type
            {
                S.Push(pow(e,temp1));
                break;
            }
            case(ABS_TYPE): // Absolute Value Type
            {
                S.Push(abs(temp1));
                break;
            }
            case(SQRT_TYPE): // Square Root Type
            {
                S.Push(sqrt(temp1));
                break;
            }
            }
            break;
        }
        default: // If the incorrect token was found in the queue, an error is thrown
        {
            throw PARAN_ERROR;
        }
        }
    }
    return S.Pop(); //Pops the answer to the expression
}
