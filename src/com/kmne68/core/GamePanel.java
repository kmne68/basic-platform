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
  
  public static final int WIDTH   = 640;
  public static final int HEIGHT  = 480;
  
  private Thread thread;
  private boolean running;
  
  private BufferedImage image;
  private Graphics2D g;
  
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
    
    while(running) {
      
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
    
    running = true;
    
    image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    g = (Graphics2D) image.getGraphics();
    
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
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, WIDTH, HEIGHT);
    
    tileMap.draw(g);
    player.draw(g);
    
  }
  
  private void draw() {
    
    Graphics g2 = getGraphics();
    g2.drawImage(image, 0, 0, null);
    g2.dispose();
  }
  
  
  public void keyTyped(KeyEvent key) {
    
  }
  
  public void keyPressed(KeyEvent key) {
    
    int code = key.getKeyCode();
    
    if(code == KeyEvent.VK_LEFT) {
      System.out.println("left from gp");
      player.setLeft(true);
    }
    if(code == KeyEvent.VK_RIGHT) {
      System.out.println("right from gp");
      player.setRight(true);
    }
    if(code == KeyEvent.VK_W)
      player.setJumping(true);
  }
  
  public void keyReleased(KeyEvent key) {
        
    int code = key.getKeyCode();
    
    if(code == KeyEvent.VK_LEFT) {
      player.setLeft(false);
    }
    if(code == KeyEvent.VK_RIGHT) {
      player.setRight(false);
    }
  }
  
}
