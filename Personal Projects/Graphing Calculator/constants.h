#ifndef CONSTANTS
#define CONSTANTS

// Window Size
const int WINDOW_SIZE = 600;
const int WINDOW_ORIGIN = WINDOW_SIZE/2;

//Limits
const int NUMBER_OF_POINTS = 600000;

//Error Codes
const int PARAN_ERROR = 420;
const int EVAL_STACK_ERROR = 33;
const int OPERATOR_ERROR = 66;
const int UNIDENTIFIED_CHAR = 404;
const int EMPTY = 999;
const char FUNC_TYPE_ERROR = 'F';
const char OPERATOR_PRECEDENCE = 'O';

//Other
const double ZOOM_IN = 0.5;
const double ZOOM_OUT = 2.0;
const double e = 2.71828182845904523536;

//Token Types
const int VARIABLE_TYPE = 10;
const int NUMBER_TYPE = 20;
const int OPERATOR_TYPE = 30;
const int OPEN_PARAN_TYPE = 40;
const int CLOSE_PARAN_TYPE = 50;
const int FUNCS_TYPE = 60;
const int NEGATIVE_TYPE = 70;

//Function Types
const int SINE_TYPE = -1;
const int COSINE_TYPE = -2;
const int TANGENT_TYPE = -3;
const int LOG_TYPE = -4;
const int E_TYPE = -5;
const int ABS_TYPE = -6;
const int SQRT_TYPE = -7;

//Operator Types
const int EXPONENT_TYPE = 1;
const int MULTIPLY_TYPE = 2;
const int DIVIDE_TYPE = 3;
const int ADD_TYPE = 4;
const int SUBTRACT_TYPE = 5;

//Precedence
const int FUNCTION_PRECEDENCE = -11;
const int PARAN_PRECEDENCE = -10;
const int EXPONENT_PRECEDENCE = 1;
const int MULTIPLY_PRECEDENCE = 2;
const int DIVIDE_PRECEDENCE = 2;
const int ADD_PRECEDENCE = 4;
const int SUBTRACT_PRECEDENCE = 4;

#endif // CONSTANTS

