/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmne68.core;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author kmne6
 */
public class TileMap {
  
  private int x;
  private int y;
  
  private int tileSize;
  private int[][] map;
  private int mapWidth;
  private int mapHeight;
  
  private BufferedImage tileset;
  private Tile[][] tiles;
  
  private int minX;
  private int minY;
  private int maxX = 0;
  private int maxY = 0;
  
  
  public TileMap(String tileMap, int tileSize) {
    
    this.tileSize = tileSize;
    
    try {
      BufferedReader br = new BufferedReader(new FileReader(tileMap));
      
      mapWidth = Integer.parseInt(br.readLine());
      mapHeight = Integer.parseInt(br.readLine());
      map = new int[mapHeight][mapWidth];
      
      minX = GamePanel.WIDTH - mapWidth * tileSize;
      minY = GamePanel.HEIGHT - mapHeight * tileSize;
      
      
      String delimiters = "\\s+";   // delimiter is any whitespace
      for(int row = 0; row < mapHeight; row++) {
        String line = br.readLine();
        String[] tokens = line.split(delimiters);
        
        for(int column = 0; column < mapWidth; column++) {
          map[row][column] = Integer.parseInt(tokens[column]);
        }      
      }
    }
    catch(Exception e) {
      System.out.println("Exception caught: " + e);
      e.printStackTrace();
    }
  }
  
  
  public void loadTiles(String s) {
    
    try {
      tileset = ImageIO.read(new File(s));
      int numTilesAcross = (tileset.getWidth() + 1) / (tileSize + 1); // +1 accounts for one pixel border between tiles
      tiles = new Tile[2][numTilesAcross];
      
      BufferedImage subImage;
      for(int column = 0; column < numTilesAcross; column++) {
        subImage = tileset.getSubimage(
                                       column * tileSize + column,
                                       0,
                                       tileSize,
                                       tileSize
        );
        
        tiles[0][column] = new Tile(subImage, false); // false = tile isn't blocked i.e. walkable
        subImage = tileset.getSubimage(
                                       column * tileSize + column, 
                                       tileSize + 1,
                                       tileSize,
                                       tileSize);
        
        tiles[1][column] = new Tile(subImage, true); // true = a blocked tile
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  
  public int getX() { return x; }
  public int getY() { return y; }
  
  public int getColumnTile(int x) {
    return x / tileSize;
  }
  
  public int getRowTile(int y) {
    return y / tileSize;
  }
  
  public int getTile(int row, int column) {
    return map[row][column];
  }
  
  public int getTileSize() {
    return tileSize;
  }
  
  public boolean isBlocked(int row, int column) {
    int rc = map[row][column];
    int r = rc / tiles[0].length;
    int c = rc % tiles[0].length;
    return tiles[r][c].isBlocked();
  }
  
  public void setX(int x) {
    this.x = x;
    
    if(x < minX) {
      x = minX;
    }
    if(x > maxX) {
      x = maxX;
    }
  }
  
  public void setY(int y) {
    this.y = y;
    
    if(y < minY) {
      y = minY;
    }
    if(y > maxY) {
      y = maxY;
    }
    
  }
  
  /////////////////////////////////////////////////////////////////////////////
  
  
  public void update() {
    
  }
  
  
  public void draw(Graphics2D g) {
    
    for(int row = 0; row < mapHeight; row++) {
      for(int column = 0; column < mapWidth; column++) {
        
        int rc = map[row][column];
        
        // determine the map row (r) and column (c)
        int r = rc / tiles[0].length;
        int c = rc % tiles[0].length;
        
        // select and draw teh appropriate image from the tileset
        g.drawImage(
                    tiles[r][c].getImage(),
                    x + column * tileSize,
                    y + row * tileSize,
                    null
                    );
        
        /*
        * The following code is for drawing colored blocks and is was replaced
        * with subimages
        */
//        if(rc == 0) {
//          g.setColor(Color.BLACK);
//        }
//        if(rc == 1) {
//          g.setColor(Color.WHITE);
//        }
//        
//        g.fillRect(x + column * tileSize, y + row * tileSize, tileSize, tileSize);
      }
    }
  }
  
}
