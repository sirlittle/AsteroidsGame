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

public class Shot {
    int lifeLeft;
    
    double x;
    double y;
    double xVelocity;
    double yVelocity;
    final double shotSpeed = 5;
    
    public Shot(double initialX, double initialY, double angle, double xVel, double yVel, int life){
        x = initialX;
        y = initialY;
        xVelocity = shotSpeed*Math.cos(angle) + xVel;
        yVelocity = shotSpeed*Math.sin(angle) + yVel;
        lifeLeft = life; 
    }
    
    public void move(int screenW, int screenH)
    {
        lifeLeft--;
        x+= xVelocity;
        y+= yVelocity;
        if(x<0){
            x+= screenW;
        }
        else if(x>screenW){
            x-= screenW;
        }
        if(y<0){
            y+= screenH;
        }
        else if(y>0){
            y-=screenH;
        }
        
    }
    public void draw(Graphics g){
        g.setColor(Color.yellow);
        g.fillOval((int)(x-.5), (int)(y-.5), 5, 5);
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public int getLifeLeft(){
        return lifeLeft;
    }
}
