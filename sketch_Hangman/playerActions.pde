//GIVING AN ANSWER
void keyPressed() {
  if ((key != DELETE && key != CODED) && key != ' ') {    //preventing to put spaces and coded keys as letters of word (eg. Ctrl, Alt, Shift)

    if (gameStatus == 2) {    //set word for multiplayer    
      ding.play(); 

      if (key >= '0' && key <= 'z') {  //allow only letters and numbers
        word = word + str(key).toUpperCase();    //set up a word
      }

      if (key == BACKSPACE) {    //removing letters
        if (word.length() >= 1) {
          word = word.substring(0, word.length()-1);
        } else if (word.length() <= 1) {
          word = "";
        }
      }

      if ((key == ENTER || key == RETURN) && word.length() >= 1) {    //submitting a word (ENTER for PC, RETURN for Mac)
        click.play();

        gameStatus = 3;
      }

      if (word.length() >= 20) {    //prevents the word from getting to long
        wrong.play();
        word = word.substring(0, 19);
      }
    }

    if (gameStatus == 1 || gameStatus ==3) {    //game is running (singleplayer or multiplayer)
      if (key >= '0' && key <= 'z') {

        answer = str(key).toUpperCase();

        if (wrongLetters.indexOf(answer) >= 0 || rightLetters.indexOf(answer) >= 0) {  //check if the letter was used before
          message = "This letter was already used!";
          wrong.play();
        } else if (word.indexOf(answer) >= 0) {    //correct letter
          rightLetters = rightLetters + answer;  
          message = "";
          right.play();
        } else if (word.indexOf(answer) == -1) {    //wrong letter
          wrongLetters = wrongLetters + " " + answer;
          wrongCounter++;
          message = "";
          wrong.play();
        }
      }
    }
  }
}



//CHOOSING A BUTTON
void mouseClicked() {
  if (singleButton.above == true && singleButton.show == true) {    //singleplayer button clicked
    click.play();

    singleButton.show = false;
    multiButton.show = false;

    gameStatus = 1;

    choseWord();
  }
  if (multiButton.above == true && multiButton.show == true) {    //multiplayer button clicked
    click.play();

    singleButton.show = false;
    multiButton.show = false;

    gameStatus = 2;

    word = "";
  }
  if (resetButton.above == true && resetButton.show == true) {    //reset button clicked
    click.play();

    setup();
  }
  if (exitButton.above == true && exitButton.show == true) {      //exit button clicked
    close.play();

    delay(100);
    exit();
  }
}
