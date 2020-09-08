/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmne68.core;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

/**
 *
 * @author kmne6
 */
public class GamePanel extends JPanel implements Runnable, KeyListener {
  
  public static final int WIDTH   = 1500;
  public static final int HEIGHT  = 1000;
  
  private Thread thread;
  private boolean isRunning;
  
  private BufferedImage tileImage;
  private Graphics2D graphicContext;
  
  private int FPS = 30;
  private int targetTime = 1000 / FPS;
  
  private TileMap tileMap;
  private Player player;
  
  public GamePanel() {
    
    super();
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setFocusable(true);
    requestFocus();
  }
  
  public void addNotify() {
    super.addNotify();
    
    if(thread == null) {
      thread = new Thread(this);
      thread.start();
    }
    addKeyListener(this);
  }
  
  
  public void run() {
    
    init();
    
    long startTime;
    long urdTime;   // update render draw?
    long waitTime;
    
    while(isRunning) {
      
      startTime = System.nanoTime();
      
      update();
      render();
      draw();
      
      urdTime = (System.nanoTime() - startTime) / 1000000;
      waitTime = targetTime - urdTime;
      
      try {
        Thread.sleep(waitTime);
      }
      catch(Exception e) {
        
      }
    }    
  }
  
  
  private void init() {
    
    isRunning = true;
    
    tileImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    graphicContext = (Graphics2D) tileImage.getGraphics();
    
    //tileMap = new TileMap("C:\\Users\\kmne6\\Documents\\NetBeansProjects\\Game\\src\\com\\kmne68\\core\\map.txt", 32);
    //tileMap = new TileMap("C:\\Users\\kmne6\\Documents\\NetBeansProjects\\Game\\src\\com\\kmne68\\core\\resources\\testmap.txt", 32);
    tileMap = new TileMap("C:\\Users\\kmne6\\Documents\\NetBeansProjects\\Game\\src\\com\\kmne68\\core\\resources\\testmap2.txt", 32);
    tileMap.loadTiles("C:\\Users\\kmne6\\Documents\\NetBeansProjects\\Game\\src\\com\\kmne68\\core\\resources\\tileset.gif");
    
    player = new Player(tileMap);
    player.setX(50);
    player.setY(50);
    
  }
  
  
  /////////////////////////////////////////////////////////////////////////////
  // inherited methods
  
  private void update() {
    
    tileMap.update();
    player.update();
    
  }
  
  private void render() {
    // clear screen by drawing a black rectangle
    graphicContext.setColor(Color.BLACK);
    graphicContext.fillRect(0, 0, WIDTH, HEIGHT);
    
    tileMap.draw(graphicContext);
    player.draw(graphicContext);
    
  }
  
  private void draw() {
    
    Graphics g2 = getGraphics();
    g2.drawImage(tileImage, 0, 0, null);
    g2.dispose();
  }
  
  
  public void keyTyped(KeyEvent key) {
    
  }
  
  public void keyPressed(KeyEvent key) {
    
    int code = key.getKeyCode();
    
    if(code == KeyEvent.VK_LEFT) {
      System.out.println("left from gp");
      player.isMovingLeft(true);
    }
    if(code == KeyEvent.VK_RIGHT) {
      System.out.println("right from gp");
      player.isMovingRight(true);
    }
    if(code == KeyEvent.VK_W)
      player.setIsJumping(true);
  }
  
  public void keyReleased(KeyEvent key) {
        
    int code = key.getKeyCode();
    
    if(code == KeyEvent.VK_LEFT) {
      player.isMovingLeft(false);
    }
    if(code == KeyEvent.VK_RIGHT) {
      player.isMovingRight(false);
    }
  }
  
}
