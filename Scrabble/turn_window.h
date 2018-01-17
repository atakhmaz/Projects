#ifndef TURN_WINDOW_H
#define TURN_WINDOW_H

#include <QWidget>
#include <QVBoxLayout>
#include <QLabel>
#include <QString>
#include <QPushButton>
#include <string>

class turn_window : public QWidget
{
	Q_OBJECT
public:
	turn_window(QWidget *parent, std::string message);
	~turn_window();

private:
	QWidget* parent;
	QVBoxLayout* turnLayout;
	QLabel* messageLabel;
	QPushButton* confirmButton;
};


#endif // TURN_WINDOW_H