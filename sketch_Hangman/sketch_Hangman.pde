import processing.sound.*;
SoundFile ding, click, right, wrong, lose, win, close;

String[] words;    //words from file, to singleplayer

String word;  //word to guess
String wrongLetters; 
String rightLetters;

int gameStatus;    // 0 = chose mode, 1 = single, 2 = multiSetup, 3= multiGame, 4 = end

int wordLength;
int wrongCounter;
int rightCounter;
int playSound;    //variable to play sound at the end only once (by defoult = 0, after end sound =1)

float xLine;  
String answer;

color background = #C4C4C4;
float scaleHangman;    //change size of hangman drawing
float xHangman;     //change x-coordinate of hangman drawing

Button resetButton, exitButton, singleButton, multiButton;

void setup() {
  scaleHangman = 1.3;
  xHangman = width/4;
  gameStatus = 0;
  playSound = 0;

  words = loadStrings("data/words.txt");

  size(600, 600); 
  background(background);
  fill(0);

  //Sound initialisation
  ding = new SoundFile(this, "sounds/ding.wav");
  click = new SoundFile(this, "sounds/click.wav");
  right = new SoundFile(this, "sounds/right.wav");
  wrong = new SoundFile(this, "sounds/wrong.wav");
  lose = new SoundFile(this, "sounds/lose.wav");
  win = new SoundFile(this, "sounds/win.wav");
  close = new SoundFile(this, "sounds/close.wav");

  //Button initialisation (single, multi, reset, exit)
  singleButton = new Button(width/3, height/2, "SINGLEPLAYER", color(255, 255, 0));
  singleButton.initFontSize = 13;
  singleButton.show = true;

  multiButton = new Button(width*2/3, height/2, "MULTIPLAYER", color(0, 255, 255));
  multiButton.initFontSize = 13;
  multiButton.show = true;

  resetButton = new Button(width/3, height/2, "RESET", color(0, 255, 0));
  exitButton = new Button(width*2/3, height/2, "EXIT", color(255, 0, 0));

  wrongLetters = "";    //reseting wrong counter and answers for new game
  wrongCounter = 0;

  rightLetters = "";    //reseting right counter and answers for new game
  rightCounter = 0;
}

void draw() {
  background(background);

  textAlign(CENTER);

  if (gameStatus == 0) {    //chosing singleplayer/multiplayer
    startScreen();

    singleButton.above();
    singleButton.display();
    multiButton.above();
    multiButton.display();
  }

  if (gameStatus == 1 || gameStatus == 3) {    //singleplayer or multiplayer
    gameFeatures(); 
    writeRightLetters();

    writeWrongLetters();
    drawHangmanParts();

    endGameInfo();
  }

  if (gameStatus == 2) {    //setting up word for multiplayer
    choseMultiWord();
  }

  if (gameStatus == 4) {    //game ended, chose action (reset / exit)
    gameFeatures(); 
    writeRightLetters();

    writeWrongLetters();
    drawHangmanParts();

    endGameInfo();

    resetButton.show = true;
    resetButton.above();
    resetButton.display(); 

    exitButton.show = true;
    exitButton.above();
    exitButton.display();

    scaleHangman = 0.7;
    xHangman = 10;
  }
}

void gameFeatures() {
  fill(0);
  textSize(15);
  textAlign(LEFT);
  text("Your word:", 10, 20);

  xLine = 0;
  wordLength = word.length();
  drawLetterLines();

  if (wrongCounter >= 1) {    //Initialisating wrong letters text
    textSize(12);
    text("Wrong letters:", 10, 80);
  }
}

void startScreen() {
  textSize(35);
  text("HANGMAN", width/2, 100);
  textSize(15);
  text("Chose the game mode you want to play:", width/2, height/2-100);

  textAlign(RIGHT);    //author
  textSize(12);
  text("Game by:", width-10, height-25);
  text("JAKUB DZIARNOWSKI", width-10, height-10);
}

void drawLetterLines() {    //drawing lines below letters to guess
  strokeWeight(1);
  for (int i = 0; i < word.length(); i++) {
    line(xLine + 10, 50, xLine+30, 50);
    xLine = xLine + 30;
  }
}

//CHECK THE GAME STATUS (WON OR LOST)
void endGameInfo() {
  if (rightCounter == word.length()) {    //game won
    if (playSound == 0) {
      win.play();
      playSound++;
    }
    textSize(40);
    textAlign(CENTER);
    fill(255, 0, 0);
    text("YOU WON", width/2, height/2 - 80);
    gameStatus = 4;
  }

  if (wrongCounter >= 9) {    //game lost
    if (playSound == 0) {
      lose.play();
      playSound++;
    }
    textSize(40);
    textAlign(CENTER);
    fill(255, 0, 0);
    text("GAME OVER", width/2, height/2 - 80);

    textAlign(RIGHT);
    textSize(15);
    fill(0);
    text("Your word was:", width-100, height-200);

    textSize(25);
    fill(0);
    text(word.toUpperCase(), width-100, height-170);
    gameStatus = 4;
  }
}

//SETTING WORDS
void choseWord() {
  word = words[int(random(words.length))].toUpperCase();
}

void choseMultiWord() {
  textAlign(CENTER);

  textSize(15);
  text("Write down your word and submit it by clicking ENTER", width/2, height/2-100);
  textSize(12);
  text("Your word:", width/2, height/2-25);

  textSize(30);
  text(word.toUpperCase(), width/2, height/2+10);
}
