#include "playerNames_window.h"

playerNames_window::playerNames_window(QWidget *parent, Game* game): QWidget(parent)
{
	this->game = game;
	windowLayout = new QVBoxLayout();
	windowMessage = new QLabel();
	windowMessage->setText(QString::fromStdString("Please enter the name of the players"));
	windowLayout->addWidget(windowMessage);
	playerNameInput = new QFormLayout();
	windowLayout->addLayout(playerNameInput);
	for(int i = 0; i < game->numberOfPlayers(); ++i){
		nameField = new QLineEdit();
		names.push_back(nameField);
		playerNameInput->addRow(QString::fromStdString("Player &" + std::to_string(i+1)), names[i]);
	}

	doneButton = new QPushButton("&Done");
	windowLayout->addWidget(doneButton);
	connect(doneButton, SIGNAL(clicked()), this, SLOT(doneClicked()));
	connect(doneButton, SIGNAL(clicked()), parent, SLOT(doneClicked()));
	setLayout(windowLayout);
	setVisible(true);
}

playerNames_window::~playerNames_window(){
	delete doneButton;
	for(unsigned int i = 0; i < names.size(); ++i){
		delete names[i];
	}
	delete playerNameInput;
	delete windowMessage;
	delete windowLayout;
}

void playerNames_window::doneClicked(){
	for(int i = 0; i < game->numberOfPlayers(); ++i){
		game->addPlayer(names[i]->text().toStdString());
	}
}