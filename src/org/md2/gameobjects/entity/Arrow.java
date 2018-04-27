package org.md2.gameobjects.entity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.weapons.BowItem;

public class Arrow extends WeaponEntity
{


	private WorldObject hitObject;
	
	public Arrow(LivingEntity user, BowItem usedBow) 
	{
		super(user, usedBow);
		this.setTextures(new Texture[]{usedBow.getArrowTexture()});
		hitObject = null;
		size = new Vec2(0.6F, 0.4F);
	}
	
	
	
	public void onCollision(WorldObject o)
	{
		if(o.equals(user))
			return;
		if(hitObject != null)
			return;
		if(o.isSensor())
			return;
		hitObject = o;
		this.body.setLinearVelocity(new Vec2());
		if(o instanceof LivingEntity){
			((LivingEntity)hitObject).damage(usedItem.getVarOnThrow());
		}	
	}
	
	public void performTick()
	{
		if(hitObject != null){
				this.removeFromWorld();
		}
	}

	@Override
	public void afterDeploySetup()
	{
		super.afterDeploySetup();
		this.body.setLinearVelocity(initialDirectionVec2.mul(usedItem.getWeaponSpeed()*5));
	}

	@Override
	public float getRenderAngle()
	{
		return super.getRenderAngle()+initialDirection;
	}

	@Override
	public FixtureDef getFixtureDef()
    {
		PolygonShape cs = new PolygonShape();
		cs.setAsBox(size.x/2, size.y/2);


		FixtureDef fd = new FixtureDef();
    	fd.shape = cs;
    	fd.density = 0.0f;
    	fd.friction = 0.0f;        
    	fd.restitution = 0.0f;
    	fd.setSensor(true);
    	return fd;
    }
	
	
}
