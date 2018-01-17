#ifndef BLANKTILE_WINDOW_H
#define BLANKTILE_WINDOW_H

#include <QVBoxLayout>
#include <QLabel>
#include <QPushButton>
#include <QComboBox>
#include <QDialog>

class blankTile_window : public QDialog
{
	Q_OBJECT
public:
	blankTile_window(QDialog *parent = 0);
	~blankTile_window();
	char getLetterUsed();

public slots:
	void blankTileConfirmed();

private:
	QVBoxLayout* blankTileLayout;
	QLabel* messageLabel;
	QPushButton* confirmButton;
	QComboBox* options;
	char letter;
};


#endif // BLANKTILE_WINDOW_H