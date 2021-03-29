void writeRightLetters() {
  rightCounter = 0;
  for (int i = 0; i < rightLetters.length(); i++) {
    checkLetterLocation(rightLetters.charAt(i));
  }
}

void checkLetterLocation(char letter) {
  for ( int i=0; i<= word.length()-1; i++) {
    if (word.charAt(i) == letter) {
      textSize(26);
      textAlign(CENTER);
      text(str(letter).toUpperCase(), 20 + i*30, 48);
      rightCounter++;
    }
  }
}
