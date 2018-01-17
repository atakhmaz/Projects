#include "game_window.h"
#include <stdexcept>

#include <iostream>

using namespace std;

game_window::game_window(QWidget *parent, Game* game): QWidget(parent)
{
	this->parent = parent;
	this->game = game;
	row = -1;
	col = -1;
	boardRows = game->boardRows();
	boardCols = game->boardCols();
	directions[0] = '0';
	directions[1] = '-';
	directions[2] = '|';
	currentDirection = 0;
	scoreWindow = NULL;
	errorWindow = NULL;
	turnWindow = NULL;
	blankTileWindow = NULL;

	gameLayout = new QHBoxLayout();
	boardLayout = new QGridLayout();
	boardLayout->setSpacing(1);
	createBoard();
	gameLayout->addLayout(boardLayout);
	rightSideBoard = new QVBoxLayout();
	gameLayout->addLayout(rightSideBoard);
	std::string playerMessage = game->currentPlayerName() + std::string(", it is your turn.");
	currentPlayer = new QLabel(QString::fromStdString(playerMessage));
	currentPlayer->setAlignment(Qt::AlignHCenter);
	rightSideBoard->addWidget(currentPlayer);
	scores = new QVBoxLayout();
	rightSideBoard->addLayout(scores);
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
	bottomRightLayout = new QVBoxLayout();
	rightSideBoard->addLayout(bottomRightLayout);
	tiles = new QHBoxLayout();
	bottomRightLayout->addLayout(tiles);
	for(int i = 0; i < game->currentPlayerHandSize(); ++i){
		tileButton = new QPushButton(QString::fromStdString(game->currentPlayersTile(i)));
		tileButton->setStyleSheet("QPushButton{ background-color: beige; border-style: outset; border-width: 3px; border-color: brown;}");
		tileButton->setMinimumWidth(40);
		tileButton->setMaximumWidth(40);
		tileButton->setMinimumHeight(40);		
		tileButton->setMaximumHeight(40);
		tileButton->setEnabled(false);
		tiles->addWidget(tileButton);
		tileButtons.push_back(tileButton);
	}
	playerInput = new QLineEdit();
	bottomRightLayout->addWidget(playerInput);
	inputButtons = new QHBoxLayout();
	bottomRightLayout->addLayout(inputButtons);
	placeButton = new QPushButton("P&lace");
	placeButton->setMinimumWidth(50);
	placeButton->setMaximumWidth(50);
	exchangeButton = new QPushButton("&Exchange");
	exchangeButton->setMinimumWidth(80);
	exchangeButton->setMaximumWidth(80);
	passButton = new QPushButton("&Pass");
	passButton->setMinimumWidth(50);
	passButton->setMaximumWidth(50);
	connect(passButton, SIGNAL(clicked()), this, SLOT(passClicked()));
	connect(exchangeButton, SIGNAL(clicked()), this, SLOT(exchangeClicked()));
	connect(placeButton, SIGNAL(clicked()), this, SLOT(placeClicked()));
	connect(passButton, SIGNAL(clicked()), parent, SLOT(updateGame()));
	connect(exchangeButton, SIGNAL(clicked()), parent, SLOT(updateGame()));
	connect(placeButton, SIGNAL(clicked()), parent, SLOT(updateGame()));
	inputButtons->addWidget(placeButton);
	inputButtons->addWidget(exchangeButton);
	inputButtons->addWidget(passButton);
	setLayout(gameLayout);
	setVisible(true);

	bool hasMaxScoreAI = false;
	bool hasMaxLetterAI = false;
	for(int i = 0; i < game->numberOfPlayers(); ++i){
		if(game->playerName(i).size() >= 4 && game->playerName(i).substr(0,4) == "CPUS"){
			hasMaxScoreAI = true;
			break;
		}
	}
		for(int i = 0; i < game->numberOfPlayers(); ++i){
		if(game->playerName(i).size() >= 4 && game->playerName(i).substr(0,4) == "CPUL"){
			hasMaxLetterAI = true;
			break;
		}
	}
		
	if(hasMaxScoreAI){
		maxScoreAI = new MaxScoreAI;
		maxScoreAI->initialize(game->getDictionary());
	}
	else
		maxScoreAI = NULL;
	if(hasMaxLetterAI){
		maxLetterAI = new MaxLettersAI;
		maxLetterAI->initialize(game->getDictionary());
	}
	else
		maxLetterAI = NULL;

	if(game->currentPlayerName().size() >= 4){
		if(game->currentPlayerName().substr(0,4) == "CPUS")
			maxScoreAITurn();
		else if(game->currentPlayerName().substr(0,4) == "CPUL")
			maxLetterAITurn();
	}
}

game_window::~game_window(){
	delete passButton;
	delete exchangeButton;
	delete placeButton;
	delete inputButtons;
	delete playerInput;
	for(unsigned int i = 0;  i < tileButtons.size(); ++i)
		delete tileButtons[i];
	delete tiles;
	delete bottomRightLayout;
	for(unsigned int i = 0;  i < scoreLabels.size(); ++i)
		delete scoreLabels[i];
	delete currentScoresLabel;
	delete scores;
	delete currentPlayer;
	delete rightSideBoard;
	for(unsigned int i = 0;  i < gridButtons.size(); ++i)
		delete gridButtons[i];
	delete boardLayout;
	delete gameLayout;
	if(scoreWindow)
		delete scoreWindow;
	if(errorWindow)
		delete errorWindow;
	if(turnWindow)
		delete turnWindow;
	if(blankTileWindow)
		delete blankTileWindow;
	if(maxScoreAI)
		delete maxScoreAI;
	if(maxLetterAI)
		delete maxLetterAI;
}

void game_window::createBoard(){
	for(int i = 0; i < boardRows; ++i){
		for(int j = 0; j < boardCols; ++j){
			std::string tileContent = game->tileAt(i,j);
			individualTile = new arrowButton(QString::fromStdString(tileContent));
			individualTile->setMinimumWidth(40);
			individualTile->setMaximumWidth(40);
			individualTile->setMinimumHeight(40);
			individualTile->setMaximumHeight(40);
			connect(individualTile, SIGNAL(clicked()), this, SLOT(boardClicked()));
			if(tileContent == "3W"){
				individualTile->setStyleSheet("QPushButton { background-color: red }");
				individualTile->setPalette(QPalette(QPalette::ButtonText,Qt::black));
			}
			else if(tileContent == "2W")
				individualTile->setStyleSheet("QPushButton { background-color: pink }");
			else if(tileContent == "3L"){
				individualTile->setStyleSheet("QPushButton { background-color: blue }");
				individualTile->setPalette(QPalette(QPalette::ButtonText,Qt::black));
			}
			else if(tileContent == "2L")
				individualTile->setStyleSheet("QPushButton { background-color: lightblue }");
			else if(tileContent == "")
				individualTile->setStyleSheet("QPushButton { background-color: green }");
			else{
				individualTile->setStyleSheet("QPushButton { background-color: yellow }");
				individualTile->setPalette(QPalette(QPalette::ButtonText,Qt::white));
				if(game->isTileUsed(i,j))
					individualTile->setEnabled(false);
			}
			boardLayout->addWidget(individualTile, i, j);
			gridButtons.push_back(individualTile);
		}
	}
}

void game_window::passClicked(){
	turnWindow = new turn_window(this, "You passed your move.");
	game->pass();
	if(row != -1 || col != -1)
		gridButtons[(row*boardRows)+col]->setDirection('0');
	currentDirection = 0;
	playerInput->clear();
}

void game_window::placeClicked(){
	std::string word = playerInput->text().toStdString(),wordFormed, message, drawnTiles, wordToUse = word;
	int score;

	size_t pos = wordToUse.find('?');
	while(pos != std::string::npos){
		blankTileWindow = new blankTile_window();
		blankTileWindow->exec();
		wordToUse[pos] = blankTileWindow->getLetterUsed();
		delete blankTileWindow;
		blankTileWindow = NULL;
		pos = wordToUse.find('?', pos+1);
	}

	for (unsigned int i = 0; i < word.size(); ++i){
    	word[i]= toupper(word[i]);
    	wordToUse[i]= toupper(wordToUse[i]);
	}
	try{
		score = game->currentPlayerScore();
		wordFormed = game->performPlay(directions[currentDirection], word, wordToUse, row, col, drawnTiles);
		message = "You placed tiles: " + word + "\nWord formed:\n " + wordFormed + "\nScore this round: "
		 + std::to_string(score) + "\nYour new score: " + std::to_string(game->currentPlayerScore()) 
		 + "\nNewly drawn tiles: " + drawnTiles;
		turnWindow = new turn_window(this, message);
		game->nextPlayer();
		int size = word.size();
		for(int i = 0; i < size; ++i){
			switch(currentDirection){
				case 1:
					if(gridButtons[(row*boardRows)+col + i]->text() != "" &&
						gridButtons[(row*boardRows)+col + i]->text() != "3W" &&
						gridButtons[(row*boardRows)+col + i]->text() != "2W" &&
						gridButtons[(row*boardRows)+col + i]->text() != "3L" &&
						gridButtons[(row*boardRows)+col + i]->text() != "2L" &&
						gridButtons[(row*boardRows)+col + i]->text() != "S" )
						++size;
					else
						gridButtons[(row*boardRows)+col + i]->setEnabled(false);
				break;
				case 2:
					if(gridButtons[((row+i)*(boardRows))+col]->text() != "" &&
						gridButtons[((row+i)*(boardRows))+col]->text() != "3W" &&
						gridButtons[((row+i)*(boardRows))+col]->text() != "2W" &&
						gridButtons[((row+i)*(boardRows))+col]->text() != "3L" &&
						gridButtons[((row+i)*(boardRows))+col]->text() != "2L" &&
						gridButtons[((row+i)*(boardRows))+col]->text() != "S")
						++size;
					else
						gridButtons[((row+i)*(boardRows))+col]->setEnabled(false);
				break;
			}
		}
	}
	catch(std::invalid_argument &e){
		errorWindow = new error_window(this);
		errorWindow->setErrorMessage(QString::fromStdString(e.what()));
	}
	if(row != -1 || col != -1)
		gridButtons[(row*boardRows)+col]->setDirection('0');
	currentDirection = 0;
	playerInput->clear();
}

void game_window::exchangeClicked(){
	std::string message, tiles;

	std::string word = playerInput->text().toStdString();
	for (unsigned int i = 0; i < word.size(); ++i){
    	word[i]= toupper(word[i]);
	}

	try{
		tiles = game->exchangeHand(word);
		message = "You exchanged tiles " + word + ".\nReplacement tiles: " + tiles + ".";
		turnWindow = new turn_window(this, message);
		game->nextPlayer();
	}
	catch(std::invalid_argument &e){
		errorWindow = new error_window(this);
		errorWindow->setErrorMessage(QString::fromStdString(e.what()));
	}
	if(row != -1 || col != -1)
		gridButtons[(row*boardRows)+col]->setDirection('0');
	currentDirection = 0;
	playerInput->clear();
}

void game_window::boardClicked(){
	currentDirection = (currentDirection+1)%3;

	QWidget *buttonWidget = qobject_cast<QWidget*>(sender());
	int indexOfButton = boardLayout->indexOf(buttonWidget);
	int tempRow = row;
	int tempCol = col;
	boardLayout->getItemPosition(indexOfButton, &row, &col, &rowspan, &colspan);
	if(row == tempRow && col == tempCol){
		gridButtons[(row*boardRows)+col]->setDirection(directions[currentDirection]);
	}
	else{
		if(tempRow != -1 || tempCol != -1)
			gridButtons[(tempRow*boardRows)+tempCol]->setDirection('0');
		currentDirection = 1;
		gridButtons[(row*boardRows)+col]->setDirection(directions[currentDirection]);
	}
}

void game_window::okClicked(){
	errorWindow->close();
	delete errorWindow;
	errorWindow = NULL;
}

void game_window::confirmClicked(){
	turnWindow->close();
	delete turnWindow;
	turnWindow = NULL;
	checkGameOver();
	if(game->currentPlayerName().size() >= 4){
		if(game->currentPlayerName().substr(0,4) == "CPUS")
			maxScoreAITurn();
		else if( game->currentPlayerName().substr(0,4) == "CPUL")
			maxLetterAITurn();
	}
}

void game_window::repaint(){
	std::string playerMessage = game->currentPlayerName() + std::string(", it is your turn.");
	currentPlayer->setText(QString::fromStdString(playerMessage));

	for(int i = 0; i < game->numberOfPlayers(); ++i){
		std::string score = game->playerName(i) + " " + std::to_string(game->playerScore(i));
		scoreLabels[i]->setText(QString::fromStdString(score));
	}

	for(int i = 0; i < game->currentPlayerHandSize(); ++i){
		tileButtons[i]->setText(QString::fromStdString(game->currentPlayersTile(i)));
	}

	for(int i = 0; i < boardRows; ++i){
		for(int j = 0; j < boardCols; ++j){
			std::string tileContent = game->tileAt(i,j);
			gridButtons[(i*boardRows)+j]->setText(QString::fromStdString(tileContent));

			if(tileContent == "3W"){
				gridButtons[(i*boardRows)+j]->setStyleSheet("QPushButton { background-color: red }");
				gridButtons[(i*boardRows)+j]->setPalette(QPalette(QPalette::ButtonText,Qt::black));
			}
			else if(tileContent == "2W")
				gridButtons[(i*boardRows)+j]->setStyleSheet("QPushButton { background-color: pink }");
			else if(tileContent == "3L"){
				gridButtons[(i*boardRows)+j]->setStyleSheet("QPushButton { background-color: blue }");
				gridButtons[(i*boardRows)+j]->setPalette(QPalette(QPalette::ButtonText,Qt::black));
			}
			else if(tileContent == "2L")
				gridButtons[(i*boardRows)+j]->setStyleSheet("QPushButton { background-color: lightblue }");
			else if(tileContent == "")
				gridButtons[(i*boardRows)+j]->setStyleSheet("QPushButton { background-color: green }");
			else{
				gridButtons[(i*boardRows)+j]->setStyleSheet("QPushButton { background-color: yellow }");
				individualTile->setPalette(QPalette(QPalette::ButtonText,Qt::white));
			}
		}
	}
}

void game_window::checkGameOver(){
	if((game->checkAllHands() || game->numberofPasses() >= game->numberOfPlayers())){
		close();
		parent->close();
		scoreWindow = new score_window(game);
	}
}

void game_window::maxScoreAITurn(){
	char direction = '*';
	int row = -1, col = -1;
	string word, wordToUse;
	maxScoreAI->getMove(game->getBoard(), game->getPlayer(), direction, word, wordToUse, row, col);
	playAITurn(direction, wordToUse, word, row, col);
	repaint();

}

void game_window::maxLetterAITurn(){
	char direction = '*';
	int row = -1, col = -1;
	string word, wordToUse;
	maxLetterAI->getMove(game->getBoard(), game->getPlayer(), direction, word, wordToUse, row, col);
	playAITurn(direction, wordToUse, word, row, col);
	repaint();
}

void game_window::playAITurn(char direction, string word, string wordToUse, int row, int col){
	string drawnTiles;
	if(!word.empty()){
		game->performPlay(direction, word, wordToUse, row, col, drawnTiles);
	}
	else
		try{
			game->exchangeHand(game->getPlayer()->getHand());
		}
		catch(invalid_argument &e){
			game->pass();
			checkGameOver();
		}
	game->nextPlayer();
	int size = word.size();
	for(int i = 0; i < size; ++i){
		switch(direction){
			case '-':
				if(gridButtons[(row*boardRows)+col + i]->text() != "" &&
					gridButtons[(row*boardRows)+col + i]->text() != "3W" &&
					gridButtons[(row*boardRows)+col + i]->text() != "2W" &&
					gridButtons[(row*boardRows)+col + i]->text() != "3L" &&
					gridButtons[(row*boardRows)+col + i]->text() != "2L" )
					++size;
				else
					gridButtons[(row*boardRows)+col + i]->setEnabled(false);
			break;
			case '|':
				if(gridButtons[((row+i)*(boardRows))+col]->text() != "" &&
					gridButtons[((row+i)*(boardRows))+col]->text() != "3W" &&
					gridButtons[((row+i)*(boardRows))+col]->text() != "2W" &&
					gridButtons[((row+i)*(boardRows))+col]->text() != "3L" &&
					gridButtons[((row+i)*(boardRows))+col]->text() != "2L" )
					++size;
				else
					gridButtons[((row+i)*(boardRows))+col]->setEnabled(false);
			break;
		}
	}
	checkGameOver();
	if(game->currentPlayerName().size() >= 4 && !(game->checkAllHands() || game->numberofPasses() >= game->numberOfPlayers())){
		if(game->currentPlayerName().substr(0,4) == "CPUS")
			maxScoreAITurn();
		else if( game->currentPlayerName().substr(0,4) == "CPUL")
			maxLetterAITurn();
	}
}