#include "blankTile_window.h"

blankTile_window::blankTile_window(QDialog *parent): QDialog(parent)
{
	setWindowTitle("Blank Tile");

	blankTileLayout = new QVBoxLayout();
	messageLabel = new QLabel("Blank Tile replacement tile");
	blankTileLayout->addWidget(messageLabel);

	options = new QComboBox();
	QString letters;
	for(int i = 0; i < 26; ++i){
		letters = (char)i+65;
		options->addItem(letters);
	}

	blankTileLayout->addWidget(options);

	confirmButton = new QPushButton("&Confirm");
	connect(confirmButton, SIGNAL(clicked()), this, SLOT(blankTileConfirmed()));
	blankTileLayout->addWidget(confirmButton);
	
	setLayout(blankTileLayout);
	setVisible(true);
}

blankTile_window::~blankTile_window(){
	delete confirmButton;
	delete options;
	delete messageLabel;
	delete blankTileLayout;
}

char blankTile_window::getLetterUsed(){
	return letter;
}

void blankTile_window::blankTileConfirmed(){
	letter = options->currentText().toStdString()[0];
	close();
}