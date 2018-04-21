package org.md2.gameobjects;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;
import org.md2.common.VAOType;
/**
 * Beschreiben Sie hier die Klasse WorldObject.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
/**
 * @author Marius
 *
 */
public abstract class WorldObject extends GameObject
{
    protected Body body;
    protected Vec2 size; //the physical size of this object
    
        
    
    public WorldObject(Texture[] texture)
    {
    	super(texture);
    	size = new Vec2(1F, 1F);
    }
    
    /**
     * Used after this WorldObject has been deployed in the world and a body has been created
     */
    public void afterDeploySetup()
    {
    	return;
    }
    
    public void animationUpdate() 
	{
		return;
	}
    
    public boolean isSensor()
    {
    	return body.getFixtureList().isSensor();
    }
    
    //overwrite to determine, what happens when this object is collided with
    //param: the worldObject that has collided with this one
    public void onCollision(WorldObject o)
    {
    	return;
    }
    
    public void addBody(Body b)
    {
    	if(this.body == null)
    		this.body = b;
    }
    
    public Body getBody()
    {
    	return body;
    	
    }
    
    public Vec2 getSize()
    {
    	return size;
    }
    
    public Vec2 getPosition()
    {
        return body.getPosition().clone();
    }
    
    public void setTransform(Vec2 position, float angle)
    {
    	body.setTransform(position, angle);
    }
    
    public float getAngle()
    {
    	float angle = body.getAngle();
//    	while(angle > 2*Math.PI){
//    		angle -= 2*Math.PI;
//    	}
//    	while(angle < 0){
//    		angle += 2*Math.PI;
//    	}
    	return angle;
    }
    
    //this is called by the world management when this object is placed in the world
    //a WorldObject has by default a static body; overwrite if you want a different body
    public BodyDef getBodyDef()
    {
    	BodyDef bd = new BodyDef();
    	bd.type = BodyType.STATIC;
    	
    	return bd;
    }
    
    //overwrite this to determine what (physical) bounds this object has
    public abstract FixtureDef getFixtureDef();


	

    
}
