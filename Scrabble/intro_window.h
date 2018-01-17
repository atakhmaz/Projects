#ifndef INTRO_WINDOW_H
#define INTRO_WINDOW_H

#include <QWidget>
#include <QVBoxLayout>
#include <QLabel>
#include <QPushButton>
#include <QSpinBox>
#include <QFormLayout>
#include "Game.h"

class intro_window : public QWidget
{
	Q_OBJECT
public:
	intro_window(QWidget *parent = 0, Game* game = 0);
	~intro_window();

private slots:
	void continueClicked();

private:
	Game* game;
	QVBoxLayout* introLayout;
	QLabel* introMessage;
	QSpinBox* numberOfPlayersInput;
	QFormLayout* numberOfPlayersInputLayout;
	QHBoxLayout* buttonLayout;
	QPushButton* continueButton, * quitButton;
};
#endif //INTRO_WINDOW_H