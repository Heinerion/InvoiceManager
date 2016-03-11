package de.heinerion.betriebe.classes.gui;

public final class PositionCoordinates {
  private int posX = 0;
  private int posY = 0;
  private int width = 0;
  private int height = 0;
  private int priority = 0;

  public int getPosX() {
    return posX;
  }

  public int getPosY() {
    return posY;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getPriority() {
    return priority;
  }

  public void setPosX(int xPosition) {
    this.posX = xPosition;
  }

  public void setPosY(int yPosition) {
    this.posY = yPosition;
  }

  public void setWidth(int aWidth) {
    this.width = aWidth;
  }

  public void setHeight(int aHeight) {
    this.height = aHeight;
  }

  public void setPriority(int aPriority) {
    this.priority = aPriority;
  }
}