#ifndef MAIN_WINDOW_H
#define MAIN_WINDOW_H

#include "intro_window.h"
#include "playerNames_window.h"
#include "game_window.h"

class main_window : public QWidget
{
	Q_OBJECT
public:
	main_window(char* config_file_name, QWidget *parent = 0);
	~main_window();
	void exitGame();

private slots:
	void continueClicked();
	void quitIntro();
	void doneClicked();
	void updateGame();
	
private:
	intro_window* introWindow;
	playerNames_window* playerNamesWindow;
	game_window* gameWindow;
	Game* game;
};
#endif //MAIN_WINDOW_H