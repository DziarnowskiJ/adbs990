//GIVING AN ANSWER
void keyPressed() {
  if (key != CODED && key != ' ') {    //preventing to put spaces and save coded keys as word (eg. Ctrl, Alt, Shift)
  
    if (gameStatus == 2) {    //set word for multiplayer    
      if (word.length() <= 19) {    //prevents the word from getting to long
        ding.play(); 
        word = word + str(key).toUpperCase();    //set up a word
        
        if (key == BACKSPACE && word.length() >= 2) {    //removing letters 
          word = word.substring(0, word.length()-2);
        }

        if ((key == ENTER || key == RETURN) && word.length() >= 2) {    //submitting a word (ENTER for PC, RETURN for Mac)
          word = word.substring(0, word.length()-1);

          click.play();

          gameStatus = 3;
        }
      }
      if (word.length() >= 20) {    //when word gets to long 
        wrong.play();
        word = word.substring(0, 19);
      }
    }

    if (gameStatus == 1 || gameStatus ==3) {    //game is played (singleplayer or multiplayer)
      if (key != ENTER) {
        answer = str(key).toUpperCase();
        if (word.indexOf(answer) >= 0) {    //correct letter
          if (rightLetters.indexOf(answer)  == -1) {    //check if the right letter was used before
            rightLetters = rightLetters + answer;

            right.play();
          }
        } else if (word.indexOf(answer) == -1) {    //wrong letter
          if (wrongLetters.indexOf(answer) == -1) {    //check if the wrong letter was used before
            wrongLetters = wrongLetters + " " + answer;
            wrongCounter++;

            wrong.play();
          }
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
