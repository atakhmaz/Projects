#include "calcwindow.h"

// Sets up a window with the CONSTANT WINDOW_SIZE, and sets up the vertex array to be of type sf::Points with NUMBER_OF_POINTS number of points
CalcWindow::CalcWindow():window(VideoMode(WINDOW_SIZE, WINDOW_SIZE), "Graphing Calculator"), pointz(Points, NUMBER_OF_POINTS)
{
    // Itroduction to the program and user controls
    system("cls");
    cout << "Welcome to the Graphing Calculator Program" << endl;
    cout << "Controls: \n Esc = Close Calculator \n + = Zoom In \n - = Zoom Out \n * = Reset Window \n Left Click = Zoom in at Mouse Position \n" << endl;
    cout << "Available Functions: \n s() - Sine \n c() - Cosine \n "
            "t() - Tangent \n l() - Log \n e() - e \n a() - Absolute Value \n "
            "q() - Square Root \n\n";
}
void CalcWindow::Plot(MyString Expression, double low, double high, double increment)
{
    // Checks if the system has enough memory and won't crash, exits if it doesn't
    if(((abs(low)+abs(high))/increment) > NUMBER_OF_POINTS)
    {
        cout << "Error: Your increment was too small for the memory to handle." << endl;
        exit(0);
    }
    // Check if lower boundary is within the scope of the window
    if(abs(low) > WINDOW_ORIGIN)
    {
        cout << "Error: Your lower bound was less than the window size." << endl;
        exit(0);
    }
    // Check if higher boundary is within the scope of the window
    if(abs(high) > WINDOW_ORIGIN)
    {
        cout << "Error: Your higher bound was greater than the window size" << endl;
        exit(0);
    }
    //  Tells the user what is being graphed, and what boundary and increment
    cout << "Now Graphing: " << Expression << endl << "From: " << low << " to " << high << endl << "With Increment: " << increment << endl;
    try // Trys to execute this code and if something is wrong, it throws the appropriate error
    {
        Queue<Token*> Q = EvalInfix(Expression); // Evaluation of expression in postfix
        int j = 0;
        double x,y;
        // For loop to evaluate ALL points on the graph
        for(double i = low; i <= high; i=i+increment)
        {
            x = i+WINDOW_ORIGIN; // X coordinate
            y = (EvalPostfix(Q,i)*-1)+WINDOW_ORIGIN; // Y coordinate
            pointz[j].position = Vector2f(x, y); // Pushes the Coordaintes into a Vertex Array of points
            j++;
        }
    }
    catch (Token error) // Catches error of type token, displays the error (invalid token type), and exits the program, this error will most likely never be called because it is handled at a lower level
    {
        cout << "Error: " << error << " could not be evaluated." <<endl;
        exit(0);
    }
    catch (int error) // Catches error of type int, displays the error, and exits the program
    {
        switch(error) // A switch statement for the various type of errors
        {
        case(PARAN_ERROR): // Extra parantheses in the math expression
        {
            cout << "Error " << error << ": you have extra (." <<endl;
            exit(0);
            break;
        }
        case(EVAL_STACK_ERROR): // Missing operator
        {
            cout << "Error " << error << ": Not Enough Numbers in Stack." <<endl;
            exit(0);
            break;
        }
        case(OPERATOR_ERROR): // Extra operators
        {
            cout << "Error " << error << ": Too many operators." <<endl;
            exit(0);
            break;
        }
        }
    }
}
// Display the graph and evaluate user controls
void CalcWindow::Display()
{
    View view; // View to let user zoom
    view.setCenter(Vector2f(WINDOW_ORIGIN,WINDOW_ORIGIN)); // Sets the center of the view to the center of the window
    window.setKeyRepeatEnabled(false);
    // X Axis
    Vertex linex[] =
    {
        Vertex(Vector2f(0, WINDOW_ORIGIN)),
        Vertex(Vector2f(WINDOW_SIZE, WINDOW_ORIGIN))
    };
    // Y Axis
    Vertex liney[] =
    {
        Vertex(Vector2f(WINDOW_ORIGIN, 0)),
        Vertex(Vector2f(WINDOW_ORIGIN, WINDOW_SIZE))
    };
    // While loop to keep window open
    while (window.isOpen())
    {
        Event event;
        while (window.pollEvent(event))
        {
            // Event where the X on the window is pressed
            if (event.type == Event::Closed)
                window.close();
            // Event where + is pressed, it zooms in
            if(Keyboard::isKeyPressed(Keyboard::Add))
            {
                view.zoom(ZOOM_IN);
                window.setView(view);
            }
            // Event where - is pressed, it zooms out
            if(Keyboard::isKeyPressed(Keyboard::Subtract))
            {
                view.zoom(ZOOM_OUT);
                window.setView(view);
            }
            // Event where left mouse button is pressed on the window it zooms into that position
            if(sf::Mouse::isButtonPressed(Mouse::Left))
            {
                view.setCenter(Mouse::getPosition(window).x ,Mouse::getPosition(window).y); // Gets mouse position
                view.zoom(ZOOM_IN);
                window.setView(view);
            }
            // Resets the window if * is pressed
            if(Keyboard::isKeyPressed(Keyboard::Multiply))
            {
                view.reset(FloatRect(0,0,WINDOW_SIZE,WINDOW_SIZE));
                view.setCenter(Vector2f(WINDOW_ORIGIN,WINDOW_ORIGIN));
                window.setView(view);
            }
            // Closes the window if Esc is pressed
            if(Keyboard::isKeyPressed(Keyboard::Escape))
            {
                window.close();
            }
        }
        // Clears the screen each loop
        window.clear(Color::Black);
        // Draws the X Axis
        window.draw(linex,2,Lines);
        // Draws the Y Axis
        window.draw(liney,2,Lines);
        // Draws all the points that were calculated the the Plot function
        window.draw(pointz);
        // Displays the window
        window.display();
    }
}
