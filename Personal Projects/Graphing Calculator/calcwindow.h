#ifndef CALCWINDOW_H
#define CALCWINDOW_H

#include "SFML/Graphics.hpp" //Includes the sfml library to graph the points
#include "evaluate.h" //Evalutaion of functions
#include <iostream>

using namespace sf;
using namespace std;

class CalcWindow
{
public:
    CalcWindow();
    // Construct the RenderWindow and VertexArray of points and form an introduction to the program
    void Plot(MyString Expression, double low, double high, double increment);
    // Takes a Math Expression and tokenizes it and places it into postfix form, then it calculates points into cartesian coordianes based on boundary and increment and puts them in a vertex array
    void Display();
    // Displays the xy plane and the set of vertex points as a graph, also lets user manipulate the graph (zoom/click)

private:
    RenderWindow window; // Window where the graph is displayed
    VertexArray pointz; // The vertex array of points used to graph the math expression (vector)
};

#endif // CALCWINDOW_H
