Requires Qt libraries
do qmake -project
add the following line to the .pro 
	QMAKE_CXXFLAGS += -std=c++11
then do qmake
then make
run by using ./Scrabble config.txt
the first window ask for the amount of players
the second window asks for the player names, if the first 4 letters of a 
	name are "CPUS" or "CPUL" it will be a computer player of the type
the third window is the game window
	click on the board to chose a starting position and direction
	to use blank tiles simply use them like any other tile, you will 
		prompted to give the letter you want to use to replace the blank
	the score is shown after the end game conditions are met or if everyone
		passes
	the score screen shows any adjustments made to players scores if any were
		made

This is the GUI version and has both AIs: CPUS and CPUL