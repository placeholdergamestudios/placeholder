package org.md2.gameobjects.entity;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.md2.main.Game;
import org.md2.rendering.RenderPrio;
import org.md2.common.Sound;
import org.md2.rendering.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.deco.Shadow;

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
		shadow = new Shadow(this);
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

    public void afterDeploySetup()
	{
		worldManager.spawnDecorationAt(shadow);
	}

    public Sound getwalkingSound()
	{
		return null;
	}

    
}