#ifndef GAME_WINDOW_H
#define GAME_WINDOW_H

#include <QWidget>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QLabel>
#include <QLineEdit>
#include <QPushButton>
#include <QGridLayout>
#include <QString>
#include <QPainter>
#include "Game.h"
#include "arrowButton.h"
#include "error_window.h"
#include "turn_window.h"
#include "score_window.h"
#include "blankTile_window.h"
#include "MaxScoreAI.h"
#include "MaxLettersAI.h"

class game_window : public QWidget
{
	Q_OBJECT
public:
	game_window(QWidget *parent = 0, Game* game = 0);
	~game_window();
	void repaint();

private slots:
	void passClicked();
	void placeClicked();
	void exchangeClicked();
	void boardClicked();
	void okClicked();
	void confirmClicked();
	
private:
	QWidget* parent;
	Game* game;
	int row,col, rowspan,colspan;
	int boardRows, boardCols;
	char directions[3];
	int currentDirection;
	char blankTileReplacement;
	QHBoxLayout* gameLayout;
	QGridLayout* boardLayout;
	QVBoxLayout* rightSideBoard;
	QLabel* currentPlayer;
	QVBoxLayout* scores;
	QLabel* currentScoresLabel;
	QLabel* nameLabel;
	QVBoxLayout* bottomRightLayout;
	QHBoxLayout* tiles;
	QPushButton* tileButton;
	QLineEdit* playerInput;
	QHBoxLayout* inputButtons;
	QPushButton* placeButton,* exchangeButton,* passButton;

	arrowButton* individualTile;

	std::vector<arrowButton*> gridButtons;
	std::vector<QPushButton*> tileButtons;
	std::vector<QLabel*> scoreLabels;

	QPainter* rightArrow;
	QPainter* downArrow;

	error_window* errorWindow;
	turn_window* turnWindow;
	score_window* scoreWindow;
	blankTile_window* blankTileWindow;

	MaxScoreAI* maxScoreAI;
	MaxLettersAI* maxLetterAI;

	void createBoard();
	void checkGameOver();
	void maxScoreAITurn();
	void maxLetterAITurn();
	void playAITurn(char direction, std::string word, std::string wordToUse, int row, int col);
};
#endif //GAME_WINDOW_H