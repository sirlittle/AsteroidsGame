/*
 // To change this template, choose Tools | Templates
 // and open the template in the editor.
 */

/**
 *
 * @author Salil Vanvari
 */
import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class AsteroidsGame extends Applet implements Runnable, KeyListener{
    Thread thread;
    long startTime, endTime, framePeriod;
    Dimension dim;
    Image img;
    Graphics g;
    Ship ship;
    boolean paused;
    ArrayList<Shot> shots;
    boolean shooting;
    Asteroids[] asters;
    int numAsteroids;
    double astRadius,minAstVel,maxAstVel;
    int astNumHits,astNumSplit;
    int lives;
    int level; 
    boolean gameover;
    int score;
    int thisLevelScore;
    
    @Override
    public void init(){
        this.resize(800, 800);
        startTime = 0;
        score = 0;
        thisLevelScore = 0;
        endTime = 0;
        framePeriod = 15;
        paused = false;
        dim = getSize();
        shooting = false;
        shots = new ArrayList();
        gameover = false;
        img = this.createImage(dim.width, dim.height);
        g = img.getGraphics();
        this.addKeyListener(this);
        
        numAsteroids=0; 
        level=0; //will be incremented to 1 when first level is set up
        astRadius=60; //values used to create the asteroids
        minAstVel=.5; 
        maxAstVel=5; 
        astNumHits=3; 
        astNumSplit=2; 
        lives = 3;
        
        thread = new Thread(this);
        thread.start();
    }
    
   public void setUpNextLevel(){
       level++;
       score += thisLevelScore;
       thisLevelScore = 0;
       shots = new ArrayList();
       ship = new Ship(250,250,0,.2,.98,.05,12);
       paused = false;
       shooting = false;
       asters =new Asteroids[level * (int)Math.pow(astNumSplit,astNumHits-1)+1];
       numAsteroids=level;
       for(int i=0;i<numAsteroids;i++) 
            asters[i]=new Asteroids(Math.random()*dim.width, 
            Math.random()*dim.height,astRadius,minAstVel, 
            maxAstVel,astNumHits,astNumSplit); 
   }
    @Override
   public void paint(Graphics gfx){
        g.setColor(Color.black);
        g.fillRect(0,0,dim.width,dim.height);
        ship.draw(g);
        for(Shot s: shots)
        {
            s.draw(g);
        }
        for(int i = 0; i <numAsteroids; i++)
            asters[i].draw(g);
        ship.draw(g);
        g.setColor(Color.cyan); 
        g.drawString("Level " + level,20,20);
        g.drawString("Lives " + lives,20,780);
        g.drawString("Score " + score, 720,20);
        g.drawString("Level Score " + thisLevelScore , 700,40);
        if(gameover){
            g.setColor(Color.red);
            g.drawString("GAME OVER",400,400);
            g.drawString("Your Score: " + score, 395,420);
        }
        gfx.drawImage(img,0,0,this);
   } 
   
    @Override
   public void update(Graphics gcf){
       this.paint(gcf);
   }
   
    @Override
   public void run(){
        for(;;){
            startTime=System.currentTimeMillis(); 
            if(numAsteroids<=0) 
                setUpNextLevel();
            
            if(!paused){
                ArrayList<Integer> ints = new ArrayList();
                ship.move(dim.width, dim.height);
                for(Shot s: shots){
                    s.move(dim.width,dim.height);
                    if(s.getLifeLeft()<=0){
                        ints.add(shots.indexOf(s));
                    }
                }
                for(int i: ints){
                    shots.remove(i);
                }
                updateAsteroids();

                if(shooting && ship.canShoot()){                            
                    shots.add(ship.shoot());
                    thisLevelScore --;
                }
            }

            repaint(); 
            try{ 
                endTime=System.currentTimeMillis(); 
                if(framePeriod-(endTime-startTime)>0) 
                    Thread.sleep(framePeriod-(endTime-startTime)); 
            }          
            catch(InterruptedException e){                
            } 
       }   
   }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ENTER)
        {
            if(!ship.isActive() && !paused){
                ship.setActive(true);
            }
            else{
                paused = !paused;
                if(paused)
                    ship.setActive(false);
                else
                    ship.setActive(true);
            }
        }
       else if(paused || !ship.isActive()){
        }
        
        else if(e.getKeyCode()==KeyEvent.VK_LEFT) {
            ship.setTurningLeft(true);
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            ship.setTurningRight(true);
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            ship.setAccelerating(true);
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE){
            shooting = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP){
            ship.setAccelerating(false);
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT){
            ship.setTurningLeft(false);
        }
        else if( e.getKeyCode()==KeyEvent.VK_RIGHT){
            ship.setTurningRight(false);
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE){
            shooting = false;
        }
    }
    private void deleteAsteroid(int index){ 
        numAsteroids--; 
        for(int i=index;i<numAsteroids;i++) 
            asters[i]=asters[i+1]; 
        asters[numAsteroids]=null; 
    } 
    
    private void addAsteroid(Asteroids ast){ 
        asters[numAsteroids]=ast; 
        numAsteroids++; 
    }
    private void updateAsteroids(){ 
        for(int i=0;i<numAsteroids;i++){ 
            asters[i].move(dim.width,dim.height); 
            if(asters[i].shipCollision(ship)){ 
                level--; //restart this level
                lives--;
                thisLevelScore = 0;
                if(lives == 0){
                    gameOver();
                }
                numAsteroids=0; 
                shots = new ArrayList();
                return; 
            } 
            for(int j=0;j<shots.size();j++){ 
                if(asters[i].shotCollision(shots.get(j))){ 
                    shots.remove(j); 
                    thisLevelScore += asters[i].radius * level;
                    if(asters[i].getHitsLeft()>1){ 
                        for(int k=0;k<asters[i].getNumSplit();k++) 
                            addAsteroid( 
                            asters[i].split( 
                            minAstVel,maxAstVel)); 
                    } 
                    deleteAsteroid(i); 
                    j=shots.size(); 
                    i--; 
                } 
           } 
        } 
    }
    
    private void gameOver(){
        gameover = true;
        score += thisLevelScore;
        this.repaint();
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException e){
            
        }
        level = 0;
        lives = 3;
        score = 0;
        gameover = false;
    }
}
