/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Graphics;
/**
 *
 * @author vanvari
 */
public class Asteroids {
   double x;
   double y;
   double xVelocity;
   double yVelocity;
   double radius;
   
   int hitsLeft;
   int numSplit;
   
   public Asteroids(double x, double y, double radius, double minV, double maxV, int hitsLeft, int numSplit){
      this.x = x;
      this.y = y;
      this.radius = radius;
      this.hitsLeft = hitsLeft;
      this.numSplit = numSplit;
      
      double vel = minV + Math.random()*(maxV-minV);
      double dir = 2*Math.PI*Math.random();
      
      xVelocity = Math.cos(dir)*vel;
      yVelocity = Math.sin(dir)*vel;
   }
   
   public void move(int screenW, int screenH){
      x+=xVelocity;
      y+= yVelocity;
     
      if(x<0-radius){
         x+=screenW+2*radius;
      }
      else if(x>screenW+radius){
         x-=screenW+2*radius; 
      }
      if(y<0-radius) 
         y+=screenH+2*radius; 
      else if(y>screenH+radius) 
         y-=screenH+2*radius; 
   }
   
   public void draw(Graphics g){ 
      g.setColor(Color.gray); 
      g.fillOval((int)(x-radius+.5),(int)(y-radius+.5), 
         (int)(2*radius),(int)(2*radius)); 
   } 
   
   public int getHitsLeft(){ 
      return hitsLeft; 
   } 
   
   public int getNumSplit(){ 
      return numSplit; 
   }
   
   public boolean shipCollision(Ship ship){ 
      if(Math.pow(radius+ship.getRadius(),2) > Math.pow(ship.getX()-x,2)+ Math.pow(ship.getY()-y,2) && ship.isActive())
         return true;
      return false;
   }
   public boolean shotCollision(Shot shot){ 
      if(Math.pow(radius,2) > Math.pow(shot.getX()-x,2)+ Math.pow(shot.getY()-y,2)) 
         return true;
      return false;
   }
   public Asteroids split(double minV, double maxV){
      return new Asteroids(x,y,radius/Math.sqrt(numSplit), minV,maxV,hitsLeft-1,numSplit);
   }
}
