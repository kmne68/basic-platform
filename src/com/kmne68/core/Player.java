/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmne68.core;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author kmne6
 */
public class Player {
  
  private double x;
  private double y;
  private double dX;
  private double dY;
  
  private int width;
  private int height;
  
  private boolean left;
  private boolean right;
  private boolean jumping;
  private boolean falling;
  
  private double moveSpeed;
  private double maxSpeed;
  private double maxFallingSpeed;
  private double stopSpeed;
  private double jumpStart;
  private double gravity;
  
  private TileMap tileMap;
  
  private boolean topLeft;
  private boolean topRight;
  private boolean bottomLeft;
  private boolean bottomRight;
  
  
  public Player(TileMap tileMap) {
    
    this.tileMap = tileMap;
    
    width = 20;
    height = 20;
    
    moveSpeed = 0.6;
    maxSpeed = 4.2;
    maxFallingSpeed = 12;
    stopSpeed = 0.30;
    jumpStart = -11.0;
    gravity = 0.64;
    
  }
  
  public void setX(int x ) {
    this.x = x;
  }
  
  public void setY(int y) {
    this.y = y;
  }
  
  public void setLeft(boolean b){
    
    this.left = b;
    System.out.println("left = " + left);
  }
  
  public void setRight(boolean b) {
    
    this.right = b;
    System.out.println("right = " + right);
  }
  
  public void setJumping(boolean b) {
    if(!falling)
      this.jumping = true;
  }
  
  
  private void calculateCorners(double x, double y) {
    
    int leftTile = tileMap.getColumnTile( (int) (x - width / 2 ) );
    int rightTile = tileMap.getColumnTile( (int) (x + width / 2 ) - 1 );
    int topTile = tileMap.getRowTile( (int) ( y - height / 2 ) );
    int bottomTile = tileMap.getRowTile( (int) ( y + height / 2 ) - 1 );
    
    /*
    * The following code was used to access tiles but was replaced by the
    * isBlocked() method
    */
//    topLeft = tileMap.getTile(topTile, leftTile) == 0;
//    topRight = tileMap.getTile(topTile, rightTile) == 0;
//    bottomLeft = tileMap.getTile(bottomTile, leftTile) == 0;
//    bottomRight = tileMap.getTile(bottomTile, rightTile) == 0;
    
    topLeft = tileMap.isBlocked(topTile, leftTile);
    topRight = tileMap.isBlocked(topTile, rightTile);
    bottomLeft = tileMap.isBlocked(bottomTile, leftTile);
    bottomRight = tileMap.isBlocked(bottomTile, rightTile);
    
  }
  
  /////////////////////////////////////////////////////////////////////////////
  
  public void update() {
    
    // determine next position
    if(left) {
      dX -= moveSpeed;
      if(dX < -maxSpeed) {
        dX = -maxSpeed;
      }
    }
    else if(right) {
      dX += moveSpeed;
      if(dX > maxSpeed) {
        dX = maxSpeed;
      }
    }
    else {
      if(dX > 0) {
        dX -= stopSpeed;
        if(dX < 0) {
          dX = 0;
        }
      }
      else if(dX < 0) {
        dX += stopSpeed;
        if(dX > 0) {
          dX = 0;
        }
      }
    }
    
    if(jumping) {
      dY = jumpStart;
      falling = true;
      jumping = false;
    }
    
    if(falling) {
      dY += gravity;
      if(dY > maxFallingSpeed) {
        dY = maxFallingSpeed;
      }
    }
    else {
      dY = 0;
    }
    
    // check collisions
    int currentColumn = tileMap.getColumnTile( (int) x);
    int currentRow = tileMap.getRowTile( (int) y);
    
    double toX = x + dX;
    double toY = y + dY;
    
    double tempX = x;
    double tempY = y;
    
    calculateCorners(x, toY);
    
    if(dY < 0) {
      if(topLeft || topRight) {
        dY = 0;
        tempY = currentRow * tileMap.getTileSize() + height / 2;
      }
      else {
        tempY += dY;
      }
    }
    
    if(dY > 0) {
      if(bottomLeft || bottomRight) {
        dY = 0;
        falling = false;
        tempY = (currentRow + 1) * tileMap.getTileSize() - height / 2;
      }
      else {
        tempY += dY;
      }
    }
    
    calculateCorners(toX, y);
    
    if(dX < 0) {
      if(topLeft || bottomLeft) {
        dX = 0;
        tempX = currentColumn * tileMap.getTileSize() + width / 2;
      }
      else {
        tempX += dX;
      }
    }
    
    if(dX > 0) {
      if(topRight || bottomRight) {
        dX = 0;
        tempX = (currentColumn + 1) * tileMap.getTileSize() - width / 2;
      }
      else {
        tempX += dX;
      }
    }
    
    if(!falling) {
      calculateCorners(x, y + 1);
      if(!bottomLeft && !bottomRight) {
        falling = true;
      }
    }
    
    x = tempX;
    y = tempY;
  }
  
  
  public void draw(Graphics2D g) {
    
    int tileMapX = tileMap.getX();
    int tileMapY = tileMap.getY();
    
    g.setColor(Color.RED);
    g.fillRect(
      (int) (tileMapX + x - width / 2 ),
      (int) (tileMapY + y - height / 2 ),
      width,
      height 
    );
    
  }
  
}
