#ifndef SCORE_WINDOW_H
#define SCORE_WINDOW_H

#include <QWidget>
#include <QVBoxLayout>
#include <QLabel>
#include <QString>
#include <QPushButton>
#include "Game.h"

class score_window : public QWidget
{
	Q_OBJECT
public:
	score_window(Game* game = 0);
	~score_window();

public slots:
	void exitGame();

private:
	QVBoxLayout* scoreLayout;
	QLabel* titleMessage;
	QLabel* scoreAdjust;
	QPushButton* doneButton;
	QVBoxLayout* scores;
	QLabel* currentScoresLabel;
	QLabel* nameLabel;
	std::vector<QLabel*> scoreLabels;
	QVBoxLayout* winners;
	QLabel* winnersTitle;
	std::vector<QLabel*> winnerLabels;
};


#endif // SCORE_WINDOW_H