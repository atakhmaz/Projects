#include "score_window.h"

score_window::score_window(Game* game): QWidget(0)
{
	setWindowTitle("Score Screen");

	scoreLayout = new QVBoxLayout();
	titleMessage = new QLabel("Game Over - Final Report");
	scoreLayout->addWidget(titleMessage);
	titleMessage->setAlignment(Qt::AlignHCenter);


	std::string scoreAdjustments = game->updateScores();
	scoreAdjust = new QLabel(QString::fromStdString(scoreAdjustments));
	if(!scoreAdjustments.empty()){
		scoreLayout->addWidget(scoreAdjust);
	}
	scores = new QVBoxLayout();
	scoreLayout->addLayout(scores);
	currentScoresLabel = new QLabel("Current Scores");
	currentScoresLabel->setAlignment(Qt::AlignHCenter);
	scores->addWidget(currentScoresLabel);
	for(int i = 0; i < game->numberOfPlayers(); ++i){
		std::string score = game->playerName(i) + " " + std::to_string(game->playerScore(i));
		nameLabel = new QLabel(QString::fromStdString(score));
		nameLabel->setAlignment(Qt::AlignHCenter);
		scores->addWidget(nameLabel);
		scoreLabels.push_back(nameLabel);
	}

	winners = new QVBoxLayout();
	scoreLayout->addLayout(winners);
	winnersTitle = new QLabel("Winners");
	scores->addWidget(winnersTitle);
	std::vector<Player*> winningPlayers;
	game->findWinners(winningPlayers);

	for(unsigned int i = 0; i < winningPlayers.size(); ++i){
		nameLabel = new QLabel(QString::fromStdString(winningPlayers[i]->name));
		scores->addWidget(nameLabel);
		winnerLabels.push_back(nameLabel);
	}

	doneButton = new QPushButton("Done");
	connect(doneButton, SIGNAL(clicked()), this, SLOT(exitGame()));
	scoreLayout->addWidget(doneButton);

	setLayout(scoreLayout);
	setVisible(true);
}
score_window::~score_window(){
	delete doneButton;
	for(unsigned int i = 0; i < winnerLabels.size(); ++i)
		delete winnerLabels[i];
	delete winnersTitle;
	delete winners;
	for(unsigned int i = 0; i < scoreLabels.size(); ++i)
		delete scoreLabels[i];
	delete currentScoresLabel;
	delete scores;
	delete scoreAdjust;
	delete titleMessage;
	delete scoreLayout;
}

void score_window::exitGame(){
	close();
}