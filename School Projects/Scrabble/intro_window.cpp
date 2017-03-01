#include "intro_window.h"

intro_window::intro_window(QWidget *parent, Game* game): QWidget(parent)
{
	this->game = game;
	introLayout = new QVBoxLayout();
	introLayout->addStretch();
	introMessage = new QLabel();
	introMessage->setText(QString::fromStdString("Welcome to Scrabble"));
	introLayout->addWidget(introMessage);
	numberOfPlayersInput = new QSpinBox();
	numberOfPlayersInput->setRange(1,8);
	numberOfPlayersInput->setValue(2);
	numberOfPlayersInputLayout = new QFormLayout();
	introLayout->addLayout(numberOfPlayersInputLayout);
	numberOfPlayersInputLayout->addRow(QString::fromStdString("&Number of Players:"), numberOfPlayersInput);
	continueButton = new QPushButton("&Continue");
	quitButton = new QPushButton("&Quit");
	buttonLayout = new QHBoxLayout();
	buttonLayout->addWidget(continueButton);
	buttonLayout->addWidget(quitButton);
	introLayout->addLayout(buttonLayout);
	connect(continueButton, SIGNAL(clicked()), this, SLOT(continueClicked()));
	connect(continueButton, SIGNAL(clicked()), parent, SLOT(continueClicked()));
	connect(quitButton, SIGNAL(clicked()), parent, SLOT(quitIntro()));
	setLayout(introLayout);
	setVisible(true);
}

intro_window::~intro_window(){
	delete quitButton;
	delete continueButton;
	delete buttonLayout;
	delete numberOfPlayersInputLayout;
	delete numberOfPlayersInput;
	delete introMessage;
	delete introLayout;
}

void intro_window::continueClicked(){
	game->updateNumberOfPlayers(numberOfPlayersInput->value());
}