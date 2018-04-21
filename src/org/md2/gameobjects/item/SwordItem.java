package org.md2.gameobjects.item;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.SwungSword;
import org.md2.gameobjects.entity.ThrownBoomerang;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.main.Game;
import org.md2.main.GraphicRendererV2;

public abstract class SwordItem extends Item
{
	protected float swordLength;
	protected float swingingSpeed;
	
	public SwordItem(Texture[] texture, float length, float swingingSpeed)
	{
		super(texture, 1, true);
		this.swordLength = length;
		this.swingingSpeed = swingingSpeed;
	}

	@Override
	public boolean onUse(LivingEntity user) 
	{
		if(currentlyInUse)
			return false;
		Vec2 mousePos = GraphicRendererV2.getMousePos();
		mousePos.normalize();
		Vec2 entityPos = user.getPosition().add(mousePos.mul(0.5F+0.5F*swordLength));
		WorldObject wo = new SwungSword(user, this);
		Game.getGame().getMechanicManager().getWorldManager().spawnObjectAt(wo, entityPos);
		setCurrentlyInUse(true);
		return super.onUse(user);
	}
	
	public float getSwordLength()
	{
		return swordLength;
	}
	
	public float getSwingingSpeed()
	{
		return swingingSpeed;
	}
	
	public FixtureDef getFixtureDef()
    {
    	PolygonShape cs = new PolygonShape();
    	cs.setAsBox(0.5f, 0.5f);  

    	FixtureDef fd = new FixtureDef();
    	fd.shape = cs;
    	fd.density = 0.0f;
    	fd.friction = 0.0f;        
    	fd.restitution = 0.0f;
    	fd.setSensor(true);
    	
    	return fd;
    }
	
}
