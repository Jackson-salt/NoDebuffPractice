//package me.jacksondasheng.noDebuffPractice;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;

public class Main extends Thread implements KeyListener {
    public static ArrayList<Pot> pots = new ArrayList<Pot>();
    public static ArrayList<Splash> splashes = new ArrayList<Splash>();
    public static JFrame frame = new JFrame("NoDebuff Practice");
    public static JPanel panel;
    public static ArrayList<Integer> potXs = new ArrayList<Integer>(),
    potYs = new ArrayList<Integer>();
    public static Color player1Color = Color.GREEN,
    player2Color = Color.BLUE,
    potColor = Color.RED,
    pearlColor = Color.MAGENTA,
    statsColor = Color.GRAY,
    statsBackgroundColor = Color.LIGHT_GRAY;
    public static boolean gameOver;
    public static int maxHealth = 10,
    maxPots = 10,
    maxPearlCooldown = 15,
    mapWidth = 33,
    mapHeight = 40,
    frameWidth = mapWidth * 10,
    frameHeight = mapHeight * 10,
    player1Health = maxHealth,
    player1Pots = maxPots,
    player1PearlCooldown = 0,
    player1X = 1,
    player1Y = 1,
    player1FacingX = 1,
    player1FacingY = 0,
    player1PearlX = -1,
    player1PearlY = -1,
    player2Health = maxHealth,
    player2Pots = maxPots,
    player2PearlCooldown = 0,
    player2X = mapWidth - 2,
    player2Y = mapWidth - 2,
    player2FacingX = -1,
    player2FacingY = 0,
    player2PearlX = -1,
    player2PearlY = -1;

    public static void main(String[] args) {
        frame.setBounds(0, 0, frameWidth, frameHeight);
        frame.addKeyListener(new Main());
        refresh();
        frame.add(panel);
        frame.setVisible(true);
    }
    
    @Override
    public void keyPressed(KeyEvent event) {
        switch(event.getKeyCode()) {
            case 87: {
                movePlayer1(0, -1);
                break;
            }
            
            case 65: {
                movePlayer1(-1, 0);
                break;
            }
            
            case 83: {
                movePlayer1(0, 1);
                break;
            }
            
            case 68: {
                movePlayer1(1, 0);
                break;
            }
            
            case 81: {
                Main player1ThrowPearl = new Main();
                player1ThrowPearl.setName("player1ThrowPearl");
                player1ThrowPearl.start();
                break;
            }
            
            case 69: {
                Main player1ThrowPot = new Main();
                player1ThrowPot.setName("player1ThrowPot");
                player1ThrowPot.start();
                break;
            }
            
            case 38: {
                movePlayer2(0, -1);
                break;
            }
            
            case 37: {
                movePlayer2(-1, 0);
                break;
            }
            
            case 40: {
                movePlayer2(0, 1);
                break;
            }
            
            case 39: {
                movePlayer2(1, 0);
                break;
            }
            
            case 47: {
                Main player2ThrowPearl = new Main();
                player2ThrowPearl.setName("player2ThrowPearl");
                player2ThrowPearl.start();
                break;
            }
            
            case 16: {
                Main player2ThrowPot = new Main();
                player2ThrowPot.setName("player2ThrowPot");
                player2ThrowPot.start();
                break;
            }
        }
    }
    
    public void run() {
        switch(getName()) {
            case "player1ThrowPot": {
                if(player1Pots > 0) {
                    int potFacingX = player1FacingX,
                    potFacingY = player1FacingY,
                    potX = player1X,
                    potY = player1Y;
                    player1Pots--;
                    
                    for(int i = 0; i < 3; i++) {
                        potX += potFacingX;
                        potY += potFacingY;
                        
                        Pot pot = new Pot(potX, potY);
                        pots.add(pot);
                        refresh();
                        
                        try {
                            Thread.sleep(200);
                        } catch(InterruptedException exc) {}
                        
                        pots.remove(pot);
                        
                        if(outOfMap(potX, potY) || blockedByPlayer1(potX, potY) || blockedByPlayer2(potX, potY)) {
                            break;
                        }
                        
                        refresh();
                    }
                    
                    healPlayer1((int) (4 - Math.sqrt(Math.pow(potX - player1X, 2) + Math.pow(potY - player1Y, 2))));
                    healPlayer2((int) (4 - Math.sqrt(Math.pow(potX - player2X, 2) + Math.pow(potY - player2Y, 2))));
                    refresh();
                    splash(potX, potY);
                }
                
                break;
            }
            
            case "player2ThrowPot": {
                if(player2Pots > 0) {
                    int potFacingX = player2FacingX,
                    potFacingY = player2FacingY,
                    potX = player2X,
                    potY = player2Y;
                    player2Pots--;
                    
                    for(int i = 0; i < 3; i++) {
                        potX += potFacingX;
                        potY += potFacingY;
                        
                        Pot pot = new Pot(potX, potY);
                        pots.add(pot);
                        refresh();
                        
                        try {
                            Thread.sleep(200);
                        } catch(InterruptedException exc) {}
                        
                        pots.remove(pot);
                        
                        if(outOfMap(potX, potY) || blockedByPlayer1(potX, potY) || blockedByPlayer2(potX, potY)) {
                            break;
                        }
                        
                        refresh();
                    }
                    
                    healPlayer1((int) (4 - Math.sqrt(Math.pow(potX - player1X, 2) + Math.pow(potY - player1Y, 2))));
                    healPlayer2((int) (4 - Math.sqrt(Math.pow(potX - player2X, 2) + Math.pow(potY - player2Y, 2))));
                    refresh();
                    splash(potX, potY);
                }
                
                break;
            }
            
            case "player1ThrowPearl": {
                if(player1PearlCooldown == 0) {
                    Main newPlayer1PearlCooldown = new Main();
                    newPlayer1PearlCooldown.setName("newPlayer1PearlCooldown");
                    newPlayer1PearlCooldown.start();
                    
                    int pearlFacingX = player1FacingX,
                    pearlFacingY = player1FacingY;
                    player1PearlX = player1X;
                    player1PearlY = player1Y;
                    
                    for(int i = 0; i < 20; i++) {
                        int newX = player1PearlX + pearlFacingX,
                        newY = player1PearlY + pearlFacingY;
                        
                        if(outOfMap(newX, newY) || blockedByPlayer2(newX, newY)) {
                            break;
                        }
                        
                        refresh();
                        player1PearlX += pearlFacingX;
                        player1PearlY += pearlFacingY;
                        
                        try {
                            Thread.sleep(50);
                        } catch(InterruptedException exc) {}
                    }
                    
                    player1X = player1PearlX;
                    player1Y = player1PearlY;
                    player1PearlX = -1;
                    player1PearlY = -1;
                    refresh();
                }
                
                break;
            }
            
            case "player2ThrowPearl": {
                if(player2PearlCooldown == 0) {
                    Main newPlayer2PearlCooldown = new Main();
                    newPlayer2PearlCooldown.setName("newPlayer2PearlCooldown");
                    newPlayer2PearlCooldown.start();
                    
                    int pearlFacingX = player2FacingX,
                    pearlFacingY = player2FacingY;
                    player2PearlX = player2X;
                    player2PearlY = player2Y;
                    
                    for(int i = 0; i < 20; i++) {
                        int newX = player2PearlX + pearlFacingX,
                        newY = player2PearlY + pearlFacingY;
                        
                        if(outOfMap(newX, newY) || blockedByPlayer1(newX, newY)) {
                            break;
                        }
                        
                        refresh();
                        player2PearlX += pearlFacingX;
                        player2PearlY += pearlFacingY;
                        
                        try {
                            Thread.sleep(50);
                        } catch(InterruptedException exc) {}
                    }
                    
                    player2X = player2PearlX;
                    player2Y = player2PearlY;
                    player2PearlX = -1;
                    player2PearlY = -1;
                    refresh();
                }
                
                break;
            }
            
            case "newPlayer1PearlCooldown": {
                for(int i = maxPearlCooldown; i >= 0; i--) {
                    player1PearlCooldown = i;
                    refresh();
                    
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException exc) {}
                }
                
                break;
            }
            
            case "newPlayer2PearlCooldown": {
                for(int i = maxPearlCooldown; i >= 0; i--) {
                    player2PearlCooldown = i;
                    refresh();
                    
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException exc) {}
                }
            }
        }
    }
    
    public static void movePlayer1(int x, int y) {
        int newX = player1X + x,
        newY = player1Y + y;
        
        player1FacingX = x;
        player1FacingY = y;
        
        if(!outOfMap(newX, newY)) {
            if(blockedByPlayer2(newX, newY)) {
                player2Health--;
                
                if(!outOfMap(player2X + x, player2Y + y)) {
                    player1X += x;
                    player1Y += y;
                    player2X += x;
                    player2Y += y;
                }
            } else {
                player1X += x;
                player1Y += y;
            }
            
            refresh();
            
            if(player2Health == 0) {
                gameOver = true;
            }
        }
    }
    
    public static void movePlayer2(int x, int y) {
        int newX = player2X + x,
        newY = player2Y + y;
        
        player2FacingX = x;
        player2FacingY = y;
        
        if(!outOfMap(newX, newY)) {
            if(blockedByPlayer1(newX, newY)) {
                player1Health--;
                
                if(!outOfMap(player1X + x, player1Y + y)) {
                    player2X += x;
                    player2Y += y;
                    player1X += x;
                    player1Y += y;
                }
            } else {
                player2X += x;
                player2Y += y;
            }
            
            refresh();
            
            if(player1Health == 0) {
                gameOver = true;
            }
        }
    }

    public static void refresh() {
        panel = new JPanel() {
            @Override
            public void paint(Graphics graphics) {
                graphics.setColor(potColor);
                
                for(Splash splash : splashes) {
                    int x = splash.getX() * 10, y = splash.getY() * 10;
                    
                    switch(splash.getRange()) {
                        case 0: {
                            graphics.fillRect(x, y + 10, 10, 10);
                            graphics.fillRect(x + 10, y, 10, 10);
                            graphics.fillRect(x, y - 10, 10, 10);
                            graphics.fillRect(x - 10, y, 10, 10);
                            
                            break;
                        }
                        
                        case 1: {
                            graphics.fillRect(x, y + 20, 10, 10);
                            graphics.fillRect(x + 10, y + 10, 10, 10);
                            graphics.fillRect(x + 20, y, 10, 10);
                            graphics.fillRect(x + 10, y - 10, 10, 10);
                            graphics.fillRect(x, y - 20, 10, 10);
                            graphics.fillRect(x - 10, y - 10, 10, 10);
                            graphics.fillRect(x - 20, y, 10, 10);
                            graphics.fillRect(x - 10, y + 10, 10, 10);
                            
                            break;
                        }
                        
                        case 2: {
                            graphics.fillRect(x, y + 30, 10, 10);
                            graphics.fillRect(x + 10, y + 20, 10, 10);
                            graphics.fillRect(x + 20, y + 10, 10, 10);
                            graphics.fillRect(x + 30, y, 10, 10);
                            graphics.fillRect(x + 20, y - 10, 10, 10);
                            graphics.fillRect(x + 10, y - 20, 10, 10);
                            graphics.fillRect(x, y - 30, 10, 10);
                            graphics.fillRect(x - 10, y - 20, 10, 10);
                            graphics.fillRect(x - 20, y - 10, 10, 10);
                            graphics.fillRect(x - 30, y, 10, 10);
                            graphics.fillRect(x - 20, y + 10, 10, 10);
                            graphics.fillRect(x - 10, y + 20, 10, 10);
                        }
                    }
                }
                
                for(Pot pot : pots) {
                    graphics.fillRect(pot.getX() * 10, pot.getY() * 10, 10, 10);
                }
                
                graphics.setColor(player1Color);
                graphics.fillRect(player1X * 10, player1Y * 10, 10, 10);
                graphics.setColor(player2Color);
                graphics.fillRect(player2X * 10, player2Y * 10, 10, 10);
                
                graphics.setColor(pearlColor);
                graphics.fillRect(player1PearlX * 10, player1PearlY * 10, 10, 10);
                graphics.fillRect(player2PearlX * 10, player2PearlY * 10, 10, 10);

                graphics.setColor(statsColor);
                graphics.fillRect(0, frameWidth, frameWidth, frameHeight - frameWidth);

                graphics.setColor(statsBackgroundColor);
                graphics.fillRect(10, frameWidth + 10, maxHealth * 10, 10);
                graphics.fillRect(170, frameWidth + 10, maxHealth * 10, 10);
                graphics.fillRect(10, frameWidth + 30, maxPots * 10, 10);
                graphics.fillRect(170, frameWidth + 30, maxPots * 10, 10);
                graphics.fillRect(10, frameWidth + 50, maxPearlCooldown * 10, 10);
                graphics.fillRect(170, frameWidth + 50, maxPearlCooldown * 10, 10);

                graphics.setColor(player1Color);
                graphics.fillRect(10, frameWidth + 10, player1Health * 10, 10);
                graphics.setColor(player2Color);
                graphics.fillRect(170, frameWidth + 10, player2Health * 10, 10);
                graphics.setColor(potColor);
                graphics.fillRect(10, frameWidth + 30, player1Pots * 10, 10);
                graphics.fillRect(170, frameWidth + 30, player2Pots * 10, 10);
                graphics.setColor(pearlColor);
                graphics.fillRect(10, frameWidth + 50, player1PearlCooldown * 10, 10);
                graphics.fillRect(170, frameWidth + 50, player2PearlCooldown * 10, 10);
            }
        };
        
        if(!gameOver) {
            frame.repaint();
        }
    }
    
    public static void splash(int x, int y) {
        for(int i = 0; i < 3; i++) {
            Splash splash = new Splash(x, y, i);
            splashes.add(splash);
            refresh();
            
            try {
                Thread.sleep(100);
            } catch(InterruptedException exc) {}
            
            splashes.remove(splash);
            refresh();
        }
    }
    
    public static void healPlayer1(int amount) {
        if(amount > 0) {
            player1Health += amount;
            
            if(player1Health > maxHealth) {
                player1Health = maxHealth;
            }
        }
    }
    
    public static void healPlayer2(int amount) {
        if(amount > 0) {
            player2Health += amount;
            
            if(player2Health > maxHealth) {
                player2Health = maxHealth;
            }
        }
    }
    
    public static boolean outOfMap(int x, int y) {
        return x < 0 || y < 0 || x >= mapWidth || y >= mapWidth;
    }
    
    public static boolean blockedByPlayer1(int x, int y) {
        return player1X == x && player1Y == y;
    }
    
    public static boolean blockedByPlayer2(int x, int y) {
        return player2X == x && player2Y == y;
    }
    
    public static class Pot {
        private int x, y;
        
        public Pot(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
    }
    
    public static class Splash {
        int x, y, range;
        
        public Splash(int x, int y, int range) {
            this.x = x;
            this.y = y;
            this.range = range;
        }
        
        public int getRange() {
            return range;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent event) {}
    
    @Override
    public void keyReleased(KeyEvent event) {}
}
