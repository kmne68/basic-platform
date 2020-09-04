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
public class Animation {
  
  private BufferedImage[] frames;
  private int currentFrame;
  
  private long startTime;
  private long delay;
  
  
  public Animation() {
    
  }
  
  public void setFrames(BufferedImage[] images) {
    
    frames = images;
    if(currentFrame >= frames.length)
      currentFrame = 0;
  }
  
  
  public void setDelay(long delay) {
    this.delay = delay;
  }
  
  
  public void update() {
    
    // if no animation at all
    if(delay == -1)
      return;
    
    // time since last frame
    long elapsed = (System.nanoTime() - startTime) / 1000000;
    
    if(elapsed > delay) {
      currentFrame++;
      startTime = System.nanoTime();
    }
    
    // avoid going out of bounds as we work across the frames image
    if(currentFrame == frames.length) {
      currentFrame = 0;
    }
    
  }
  
  
  public BufferedImage getImage() {
    return frames[currentFrame];
  }
  
}
