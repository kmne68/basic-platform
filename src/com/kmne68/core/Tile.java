/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmne68.core;

import java.awt.image.BufferedImage;

/**
 *
 * @author kmne6
 */
public class Tile {
  
  private BufferedImage image;
  private boolean isBlocked;
  
  public Tile(BufferedImage image, boolean isBlocked) {
    
    this.image = image;
    this.isBlocked = isBlocked;
    
  }
  
  
  public BufferedImage getImage() {
    return image;
  }
  
  
  public boolean isBlocked() {
    return isBlocked;
  }
  
}
