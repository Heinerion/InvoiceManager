package de.heinerion.betriebe.gui.panels;

class PositionCoordinates {
  private int posX = 0;
  private int posY = 0;
  private int width = 0;
  private int height = 0;

  int getPosX() {
    return posX;
  }

  int getPosY() {
    return posY;
  }

  int getWidth() {
    return width;
  }

  int getHeight() {
    return height;
  }

  void setPosX(int xPosition) {
    this.posX = xPosition;
  }

  void setPosY(int yPosition) {
    this.posY = yPosition;
  }

  void setWidth(int aWidth) {
    this.width = aWidth;
  }

  void setHeight(int aHeight) {
    this.height = aHeight;
  }
}