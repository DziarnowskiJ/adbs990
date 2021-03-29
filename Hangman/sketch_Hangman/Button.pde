class Button {
  boolean above, show;
  PVector center;
  float initRadius, radius, initFontSize, fontSize;
  String message;
  color colour = #333333;

  Button(float x, float y, String text, color c) {
    center = new PVector(x, y);
    above = false;
    message = text;
    colour = c;
    show = false;

    initRadius = radius = 50;
    initFontSize = fontSize = radius*0.4;

    fontSize = radius*0.4;
  }

  void above() {
    if (dist(mouseX, mouseY, center.x, center.y) < radius) {    //mouse above button
      above = true;
      radius = initRadius*1.1;
      fontSize = initFontSize*1.1;
    } else if (dist(mouseX, mouseY, center.x, center.y) > radius) {    //mouse somewhere else
      above = false;
      radius = initRadius;
      fontSize = initFontSize;
    }
  }

  void display() {
    if (show) {
      strokeWeight(3);
      fill(colour);
      circle(center.x, center.y, radius*2);

      textAlign(CENTER, CENTER);
      fill(0);
      textSize(fontSize);
      text(message, center.x, center.y);
    }
  }
}
