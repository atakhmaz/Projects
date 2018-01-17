#ifndef PLAYERNAMES_WINDOW_H
#define PLAYERNAMES_WINDOW_H

#include <QWidget>
#include <QVBoxLayout>
#include <QLabel>
#include <QLineEdit>
#include <QPushButton>
#include <QFormLayout>
#include <vector>
#include "Game.h"

class playerNames_window : public QWidget
{
	Q_OBJECT
public:
	playerNames_window(QWidget *parent = 0, Game* game = 0);
	~playerNames_window();

private slots:
	void doneClicked();

private:
	Game* game;
	QVBoxLayout* windowLayout;
	QLabel* windowMessage;
	QFormLayout* playerNameInput;
	QPushButton* doneButton;
	QLineEdit* nameField;
	std::vector<QLineEdit*> names;
};
#endif //PLAYERNAMES_WINDOW_H