#ifndef ERROR_WINDOW_H
#define ERROR_WINDOW_H

#include <QWidget>
#include <QVBoxLayout>
#include <QLabel>
#include <QString>
#include <QPushButton>

class error_window : public QWidget
{
	Q_OBJECT
public:
	error_window(QWidget *parent = 0);
	~error_window();
	void setErrorMessage(QString error);

private:
	QVBoxLayout* errorLayout;
	QLabel* errorMessage;
	QPushButton* okButton;
};

#endif // ERROR_WINDOW_H