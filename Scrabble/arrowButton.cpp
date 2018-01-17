#include <QPainter>
#include <QPushButton>
#include <QRect>
#include "arrowButton.h"

arrowButton::arrowButton (QWidget *parent) : QPushButton (parent)
{
	direction = '0';
}

arrowButton::arrowButton (QString content) : QPushButton (0)
{
	direction = '0';
	setText(content);
}

void arrowButton::paintEvent (QPaintEvent *event)
{
	QPushButton::paintEvent (event);
	QPainter p (this);

	static const QPointF rightArrow[7] = {
			QPointF(1.0, 10.0),
			QPointF(20.0, 10.0),
			QPointF(20.0, 1.0),
			QPointF(39.0, 20.0),
			QPointF(20.0, 39.0),
			QPointF(20.0, 30.0),
			QPointF(1.0, 30.0)
		};
	static const QPointF upArrow[7] = {
			QPointF(10.0, 1.0),
			QPointF(10.0, 20.0),
			QPointF(1.0, 20.0),
			QPointF(20.0, 39.0),
			QPointF(39.0, 20.0),
			QPointF(30.0, 20.0),
			QPointF(30.0, 1.0)
		};
	switch (direction) {
	case '-' :
		p.drawPolygon(rightArrow, 7);
		break;
	case '|' : 
		p.drawPolygon(upArrow, 7);
		break;
	}

}
