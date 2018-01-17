#include "turn_window.h"

turn_window::turn_window(QWidget *parent, std::string message): QWidget(0)
{
	this->parent = parent;
	setWindowTitle("Turn Complete");
	turnLayout = new QVBoxLayout();
	messageLabel = new QLabel(QString::fromStdString(message));
	turnLayout->addWidget(messageLabel);
	confirmButton = new QPushButton("&Confirm");
	connect(confirmButton, SIGNAL(clicked()), parent, SLOT(confirmClicked()));
	turnLayout->addWidget(confirmButton);
	setLayout(turnLayout);
	setVisible(true);
}
turn_window::~turn_window(){
	delete confirmButton;
	delete messageLabel;
	delete turnLayout;
}