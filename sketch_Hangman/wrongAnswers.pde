void writeWrongLetters() {
  textSize(16);
  textAlign(LEFT);
  fill(0);
  text(wrongLetters.toUpperCase(), 10, 100);
}

//DRAW HANGMAN PARTS
void drawHangmanParts() {    //draw hangman
  pushMatrix();
  strokeWeight(5);
  translate(10, height-10);
  scale(scaleHangman);
  if (wrongCounter >= 1) {    //base
    line(0, 0, 100, 0);
  } 
  if (wrongCounter >=2) {    //vert line
    line(50, 0, 50, -300);
  } 
  if (wrongCounter >=3) {    //crosspice
    line(50, -300, 130, -300);
  } 
  if (wrongCounter >=4) {    //head
    line(130, -300, 130, -260);
    fill(background);
    circle(130, -230, 60);
    fill(0);
  } 
  if (wrongCounter >=5) {    //body
    line(130, -200, 130, -100);
  } 
  if (wrongCounter >=6) {    //left arm
    line(130, -190, 90, -150);
  } 
  if (wrongCounter >=7) {    //right arm
    line(130, -190, 170, -150);
  } 
  if (wrongCounter >=8) {    //left leg
    line(130, -100, 100, -60);
  } 
  if (wrongCounter >=9) {    //right leg
    line(130, -100, 160, -60);
  }
  strokeWeight(1);
  popMatrix();
}
