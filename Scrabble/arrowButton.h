#ifndef ARROWBUTTON_H
#define ARROWBUTTON_H
#include <QPushButton>
#include <QString>

class arrowButton : public QPushButton
{
  Q_OBJECT

 public:
  arrowButton (QWidget *parent = NULL);

  arrowButton (QString content);

  QSize sizeHint () const { return QSize (30, 30); }

  QSize minimumSizeHint () const { return QSize (30, 30); }

  char getDirection () { return direction; }
  void setDirection (char direction) { this->direction = direction; repaint (); }

 protected:
  void paintEvent (QPaintEvent *event);

 private:
  char direction;
};

#endif
