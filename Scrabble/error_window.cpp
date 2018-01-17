#include "error_window.h"

error_window::error_window(QWidget *parent ): QWidget(0)
{
	setWindowTitle("Error");
	errorLayout = new QVBoxLayout();
	errorMessage = new QLabel();
	errorLayout->addWidget(errorMessage);
	okButton = new QPushButton("&OK");
	connect(okButton, SIGNAL(clicked()), parent, SLOT(okClicked()));
	errorLayout->addWidget(okButton);
	setLayout(errorLayout);
	setVisible(true);
}

error_window::~error_window(){
	delete okButton;
	delete errorMessage;
	delete errorLayout;
}

void error_window::setErrorMessage(QString error){
	errorMessage->setText(error);
}