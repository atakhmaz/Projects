#include "main_window.h"
#include <QApplication>

int main (int nargs, char **args)
{
	if (nargs < 2 || nargs > 2){
		std::cout << "Usage: Scrabble <config-filename>\n";
		return 1;
	}
	QApplication app(nargs, args);
	main_window w(args[1]);
	w.show();
	return app.exec();
}