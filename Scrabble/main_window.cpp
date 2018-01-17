#include "main_window.h"

main_window::main_window(char* config_file_name, QWidget *parent): QWidget(parent)
{
	game = new Game(config_file_name);
	introWindow = new intro_window(this, game);
	setWindowTitle("Scrabble");
	gameWindow = NULL;
}

main_window::~main_window(){
	if(gameWindow)
		delete gameWindow;
	delete game;
}

void main_window::continueClicked(){
	introWindow->close();
	delete introWindow;
	playerNamesWindow = new playerNames_window(this, game);
	resize(playerNamesWindow->sizeHint());
}
void main_window::quitIntro(){
	introWindow->close();
	delete introWindow;
	close();
}

void main_window::doneClicked(){
	playerNamesWindow->close();
	delete playerNamesWindow;
	gameWindow = new game_window(this, game);
	resize(gameWindow->sizeHint());
}

void main_window::updateGame(){
	gameWindow->repaint();
}