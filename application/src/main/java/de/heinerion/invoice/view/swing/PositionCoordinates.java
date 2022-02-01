package de.heinerion.invoice.view.swing;

public class PositionCoordinates {
  private final int posX;
  private final int posY;
  private final int width;
  private final int height;

  private PositionCoordinates(Builder builder) {
    this.posX = builder.posX;
    this.posY = builder.posY;
    this.width = builder.width;
    this.height = builder.height;
  }

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

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private int posX = 0;
    private int posY = 0;
    private int width = 0;
    private int height = 0;

    public Builder withPosX(int x) {
      posX = x;
      return this;
    }

    public Builder withPosY(int y) {
      posY = y;
      return this;
    }

    public Builder withWidth(int w) {
      width = w;
      return this;
    }

    public Builder withHeight(int h) {
      height = h;
      return this;
    }

    public PositionCoordinates build() {
      return new PositionCoordinates(this);
    }
  }
}