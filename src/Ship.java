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
public class Ship {
    final double[] origXPts = {14,-10,-6,-10};
    final double[] origYPts = {0,-8,0,8};
    final double[] origFlameXPts = {-6,-23,-6};
    final double[] origFlameYPts = {-3,0,3};
    
    final int radius = 6;
    
    double x;
    double y;
    double angle;
    double xVelocity;
    double yVelocity;
    double acceleration;
    double velocityDecay;
    double rotationalSpeed;
    
    boolean turningLeft;
    boolean turningRight;
    boolean accelerating;
    boolean active;
    
    int[] xPts;
    int[] yPts;
    int[] flameXPts;
    int[] flameYPts;
    
    int shotDelay;
    int shotDelayLeft;
    
    public Ship(double x, double y, double angle, double acceleration, double velocityDecay, double rotationalSpeed, int shotDelay){ 
        this.x=x; 
        this.y=y; 
        this.angle=angle; 
        this.acceleration=acceleration; 
        this.velocityDecay=velocityDecay; 
        this.rotationalSpeed=rotationalSpeed; 
        xVelocity=0; // not moving
        yVelocity=0; 
        turningLeft=false; // not turning
        turningRight=false; 
        accelerating=false; // not accelerating
        active=false; // start off paused
        xPts=new int[4]; // allocate space for the arrays
        yPts=new int[4]; 
        flameXPts=new int[3]; 
        flameYPts=new int[3]; 
        this.shotDelay=shotDelay; // # of frames between shots
        shotDelayLeft=0; // ready to shoot
    }  
    
    public void draw(Graphics g){
        if(accelerating && active)
        {
            for(int i = 0; i < 3; i++){
                flameXPts[i] = (int)(origFlameXPts[i]*Math.cos(angle)-origFlameYPts[i]*Math.sin(angle) + x+ .5);
                flameYPts[i] = (int)(origFlameXPts[i]*Math.sin(angle)+origFlameYPts[i]*Math.cos(angle) + y +.5);
            }
            g.setColor(Color.RED);
            g.fillPolygon(flameXPts,flameYPts,3);
            
        }
        for(int i = 0;i<4; i++ ){
            xPts[i] = (int)(origXPts[i]*Math.cos(angle) - origYPts[i]*Math.sin(angle) +x+.5);
            yPts[i] = (int)(origXPts[i]*Math.sin(angle) + origYPts[i]*Math.cos(angle) +y+.5);
        }
        if(active){
            g.setColor(Color.white);
        }
        else{
            g.setColor(Color.DARK_GRAY);
        }
        g.fillPolygon(xPts,yPts,4); 
    }
    public void move(int screenW, int screenH)
    {
        if(shotDelayLeft > 0){
            shotDelayLeft--;
        }
        if(turningLeft)
        {
            angle-=rotationalSpeed;
        }
        if(turningRight)
        {
            angle+=rotationalSpeed;
        }
        if(angle>2*Math.PI){
            angle-=2*Math.PI;
        }
        else if(angle<0){
            angle+= 2*Math.PI;
        }
        if(accelerating){
            xVelocity+=acceleration*Math.cos(angle);
            yVelocity+=acceleration*Math.sin(angle);
        }
        x+=xVelocity;
        y+=yVelocity;
        xVelocity*=velocityDecay;
        yVelocity*= velocityDecay;
        if(x<0) //wrap the ship around to the opposite side of the screen
            x+=screenW; //when it goes out of the screen's bounds
        else if(x>screenW) 
            x-=screenW; 
        if(y<0) 
            y+=screenH; 
        else if(y>screenH) 
            y-=screenH; 
    }   
    public void setAccelerating(boolean setaccl)
    {
        accelerating = setaccl;
    }
    public void setTurningLeft(boolean turningLeft){ 
        this.turningLeft=turningLeft; //start or stop turning the ship
    }
    public void setTurningRight(boolean turningRight){ 
        this.turningRight=turningRight; 
    } 
    public double getX(){ 
        return x; // returns the ship’s x location
    }    
    public double getY(){ 
        return y; 
    }    
    public double getRadius(){ 
        return radius; // returns radius of circle that approximates the ship
    } 
    public void setActive(boolean active){ 
        this.active=active; //used when the game is paused or unpaused
    } 
    public boolean isActive(){ 
        return active; 
    }   
    public boolean canShoot(){
        if(shotDelayLeft == 0){
            return true;
        }
        else{
            return false;
        }
    }
    public Shot shoot(){
        
        shotDelayLeft = shotDelay;
        return new Shot(x,y,angle,xVelocity,yVelocity,100);
    }
}
