#ifndef SQUARE_H_
#define SQUARE_H_

class Square {
public:
	Square(int LMult, int WMult, bool start)
	{
		_LMult = LMult;
		_WMult = WMult;
		_start = start;
		_score = 0;
		_letter = ' ';
		_isUsed = false;
	}

	void placeLetter(char letter, int score)
	{
		_isUsed = true;
		_letter = letter;
		_score = score;
		_LMult = 1;
		_WMult = 1;
		_start = false;
	}

	bool isUsed() const
	{ return _isUsed; }

	bool isStart() const
	{ return _start; }

	int getScore() const
	{ return _score; }

	char getLetter() const
	{ return _letter; }

	int getLMult() const
	{ return _LMult; }

	int getWMult() const
	{ return _WMult; }

protected:
	int _LMult, _WMult;
	bool _isUsed;
	bool _start;
	char _letter;
	int _score;
};

#endif //SQUARE_H_