# Dynamic Multiplayer Text Board

# Description

A hackable program that allows for the creation of several player "boards" in which are passed currently
a 2D integer array, geneset, and score to be displayed per board.
The programs intent is to allow for Python to call it as a method helper in displaying the GUI screens derived from the 2D board being
utilized inside each spawned thread of there respective Genetic Algorithm.

The global fields present inside PythonGUI allow for personal customization of display size and currently working on buffer spaces in
between each board. The simple equations in place allow for varying columns and sizes.

# Fields - Important Ones Listed

numberOfPlayers (Integer) - Allows for creation of n players onto the board to be updated and maintained.

boardWidth (Integer) - Designated size per board width.

boardHeight (Integer) - Designated size per board height.

expander (Integer) - Multiplies size of board.

drawCols (Integer) - Number of columns per row to draw until a new row is to be added for further additions.

# Constructors

@param int numberOfPlayers - Initializes numberOfPlayers field.

@param int boardWidth - Initializes number of columns/total width of board before multiplier is added.

@param int boardHeight - Initializes number of rows/total height of board before multiplier is added. 

PythonGUI - Constructs GUI screen with prescreen detail of the board.

@param int width - Used to know total width after multiplier is added.

@param int height - Used to know total height after multiplier is added.

@param other - Set as 0, not in use currently.

Board - Constructs base board system for specified board.

# Methods

@param int index - Used to retrieve specified board for further use.

getBoard

@param int[][] board - 2D integer array to be displayed.

@param int[] genes - Genes from the genetic algorithm to be displayed.

@param int score - Score to be displayed from genetic algorithm.

draw - Draws board, genes, and score for respective board position given constructors parameters

@param String message - Displays text string provided to GUI screen.

drawText - Displays given message to GUI screen by initializing message and displaying the text by repaint.

