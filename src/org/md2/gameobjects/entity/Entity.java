package org.md2.gameobjects.entity;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.md2.common.RenderPrio;
import org.md2.common.Texture;
import org.md2.common.VAOType;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.deco.Shadow;
import org.md2.main.Game;

/**
 * Alle beweglichen WorldObjecte
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */


public abstract class Entity extends WorldObject
{
	protected float modifiedMovement = 1;
	protected Shadow shadow;
	
    public Entity(Texture[] texture)
    {
    	super(texture);
    	renderPriorisation = RenderPrio.ENTITY;
    	initShadow();
    }
    
    public Shadow initShadow()
	{
		shadow = new Shadow(this);
		return shadow;
	}
    
	public Shadow getShadow()
	{
		return shadow;
	}
    
    public boolean isMoving()
    {
    	Vec2 v = body.getLinearVelocity();
    	return v.x != 0 || v.y != 0;
    }

    public void removeFromWorld()
	{
		super.removeFromWorld();
		shadow.removeFromWorld();
	}
    
    public BodyDef getBodyDef()
    {
    	BodyDef bd = new BodyDef(); 
    	bd.setFixedRotation(true);
    	bd.type = BodyType.DYNAMIC;
    	
    	return bd;
    }
    
}