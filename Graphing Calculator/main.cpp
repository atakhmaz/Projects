// Andre Takhmazyan
// Graphing Calculator

#include "calcwindow.h"

//s() - Sine
//c() - Cosine
//t() - Tangent
//l() - Log
//e() - e
//a() - Absolute Value
//q() - Square Root

int main()
{
    // Graph of -x^2
    CalcWindow A;
    A.Plot("n*(x^2)", -300, 300, .001);
    A.Display();

    // Graph of xsin(x)
    CalcWindow B;
    B.Plot("x*s(x)", -300, 300, .001);
    B.Display();

    // Graph of sin(tan(x))
    CalcWindow C;
    C.Plot("s(t(x))", -300, 300, .001);
    C.Display();

    // Graph of sin(1/x)
    CalcWindow D;
    D.Plot("s(1/x)", -300, 300, .001);
    D.Display();

    // Graph of 1/sin(x)
    CalcWindow E;
    E.Plot("1/s(x)", -300, 300, .001);
    E.Display();

    // Graph of 1/tan(x)
    CalcWindow F;
    F.Plot("1/t(x)", -300, 300, .001);
    F.Display();

    // Graph of log(x)
    CalcWindow G;
    G.Plot("l(x)", -300, 300, .001);
    G.Display();

    // Graph of square root of x
    CalcWindow I;
    I.Plot("q(x)", -300, 300, .001);
    I.Display();

    // Graph of 1/x
    CalcWindow H;
    H.Plot("1/x", -300, 300, .001);
    H.Display();

    // Graph of tan(e^x)
    CalcWindow J;
    J.Plot("t(e(x))", -300, 300, .001);
    J.Display();

    // Graph of Abs(x)
    CalcWindow K;
    K.Plot("a(x)", -300, 300, .001);
    K.Display();

    return 0;
}
