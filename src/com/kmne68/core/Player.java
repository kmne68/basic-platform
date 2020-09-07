/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmne68.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

/**
 *
 * @author kmne6
 */
public class Player {
  
  private double currentX;
  private double currentY;
  private double destinationX;
  private double destinationY;
  
  private int playerWidth;
  private int playerHeight;
  
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
  
  private Animation animation;
  private BufferedImage[] idleSprites;
  private BufferedImage[] walkingSprites;
  private BufferedImage[] jumpingSprites;
  private BufferedImage[] fallingSprites;
  
  private boolean facingLeft;
  
  
  public Player(TileMap tileMap) {
    
    this.tileMap = tileMap;
    
    playerWidth = 22;
    playerHeight = 22;
    
    moveSpeed = 0.6;
    maxSpeed = 4.2;
    maxFallingSpeed = 12;
    stopSpeed = 0.30;
    jumpStart = -11.0;
    gravity = 0.64;
    
    
    try {
      idleSprites = new BufferedImage[1];
      walkingSprites = new BufferedImage[6];
      jumpingSprites = new BufferedImage[1];
      fallingSprites = new BufferedImage[1];
      
      idleSprites[0] = ImageIO.read(new File("C:\\Users\\kmne6\\Documents\\NetBeansProjects\\Game\\src\\com\\kmne68\\core\\resources\\kirbyidle.gif"));
      jumpingSprites[0] = ImageIO.read(new File("C:\\Users\\kmne6\\Documents\\NetBeansProjects\\Game\\src\\com\\kmne68\\core\\resources\\kirbyjump.gif"));
      fallingSprites[0] = ImageIO.read(new File("C:\\Users\\kmne6\\Documents\\NetBeansProjects\\Game\\src\\com\\kmne68\\core\\resources\\kirbyfall.gif"));
      
      BufferedImage image = ImageIO.read(new File("C:\\Users\\kmne6\\Documents\\NetBeansProjects\\Game\\src\\com\\kmne68\\core\\resources\\kirbywalk.gif"));
      for(int i = 0; i < walkingSprites.length; i++) {
        walkingSprites[i] = image.getSubimage(i * playerWidth + i,
                                              0,
                                              playerWidth,
                                              playerHeight);
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    
    animation = new Animation();
    facingLeft = false;
    
  }
  
  public void setX(int x ) {
    this.currentX = x;
  }
  
  public void setY(int y) {
    this.currentY = y;
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
    
    int leftTile = tileMap.getColumnTile((int) (x - playerWidth / 2 ) );
    int rightTile = tileMap.getColumnTile((int) (x + playerWidth / 2 ) - 1 );
    int topTile = tileMap.getRowTile((int) ( y - playerHeight / 2 ) );
    int bottomTile = tileMap.getRowTile((int) ( y + playerHeight / 2 ) - 1 );
    
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
      destinationX -= moveSpeed;
      if(destinationX < -maxSpeed) {
        destinationX = -maxSpeed;
      }
    }
    else if(right) {
      destinationX += moveSpeed;
      if(destinationX > maxSpeed) {
        destinationX = maxSpeed;
      }
    }
    else {
      if(destinationX > 0) {
        destinationX -= stopSpeed;
        if(destinationX < 0) {
          destinationX = 0;
        }
      }
      else if(destinationX < 0) {
        destinationX += stopSpeed;
        if(destinationX > 0) {
          destinationX = 0;
        }
      }
    }
    
    if(jumping) {
      destinationY = jumpStart;
      falling = true;
      jumping = false;
    }
    
    if(falling) {
      destinationY += gravity;
      if(destinationY > maxFallingSpeed) {
        destinationY = maxFallingSpeed;
      }
    }
    else {
      destinationY = 0;
    }
    
    // check collisions
    int currentColumn = tileMap.getColumnTile((int) currentX);
    int currentRow = tileMap.getRowTile((int) currentY);
    
    double toX = currentX + destinationX;
    double toY = currentY + destinationY;
    
    double tempX = currentX;
    double tempY = currentY;
    
    calculateCorners(currentX, toY);
    
    if(destinationY < 0) {
      if(topLeft || topRight) {
        destinationY = 0;
        tempY = currentRow * tileMap.getTileSize() + playerHeight / 2;
      }
      else {
        tempY += destinationY;
      }
    }
    
    if(destinationY > 0) {
      if(bottomLeft || bottomRight) {
        destinationY = 0;
        falling = false;
        tempY = (currentRow + 1) * tileMap.getTileSize() - playerHeight / 2;
      }
      else {
        tempY += destinationY;
      }
    }
    
    calculateCorners(toX, currentY);
    
    if(destinationX < 0) {
      if(topLeft || bottomLeft) {
        destinationX = 0;
        tempX = currentColumn * tileMap.getTileSize() + playerWidth / 2;
      }
      else {
        tempX += destinationX;
      }
    }
    
    if(destinationX > 0) {
      if(topRight || bottomRight) {
        destinationX = 0;
        tempX = (currentColumn + 1) * tileMap.getTileSize() - playerWidth / 2;
      }
      else {
        tempX += destinationX;
      }
    }
    
    if(!falling) {
      calculateCorners(currentX, currentY + 1);
      if(!bottomLeft && !bottomRight) {
        falling = true;
      }
    }
    
    currentX = tempX;
    currentY = tempY;
    
    // move the map
    tileMap.setX((int) (GamePanel.WIDTH / 2 - currentX ));
    tileMap.setY((int) (GamePanel.HEIGHT / 2 - currentY ));
    
    // sprite animation
    if(left || right) {
      animation.setFrames(walkingSprites);
      animation.setDelay(100);
    }
    else {
      animation.setFrames(idleSprites);
      animation.setDelay(-1);
    }
    
    if(destinationY < 0) {
      animation.setFrames(jumpingSprites);
      animation.setDelay(-1);
    }
    if(destinationY > 0) {
      animation.setFrames(fallingSprites);
      animation.setDelay(-1);
    }
    
    animation.update();
    
    if(destinationX < 0) {
      facingLeft = true;
    }
    if(destinationX > 0) {
      facingLeft = false;
    }
    
  }
  
  
  public void draw(Graphics2D g) {
    
    int tileMapX = tileMap.getX();
    int tileMapY = tileMap.getY();
    
    if(facingLeft) {
      g.drawImage(animation.getImage(),
                  (int) (tileMapX + currentX - playerWidth / 2),
                  (int) (tileMapY + currentY - playerHeight / 2),
                  null
                  );
    }
    else {
      g.drawImage(animation.getImage(),
                  (int) (tileMapX + currentX - playerWidth / 2 + playerWidth ),
                  (int) (tileMapY + currentY - playerHeight / 2 ),
                  -playerWidth,
                  playerHeight,
                  null
                  );
    }
      
      
      
    /*
    * The code below was used in set up. Retained for testing feature
    * additions later
    */
//    g.setColor(Color.RED);
//    g.fillRect(
//      (int) (tileMapX + x - width / 2 ),
//      (int) (tileMapY + y - height / 2 ),
//      width,
//      height 
//    );
    
  }
  
}
