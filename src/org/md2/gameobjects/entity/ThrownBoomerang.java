package org.md2.gameobjects.entity;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.Item;
import org.md2.main.GraphicRendererV2;

public class ThrownBoomerang extends Entity
{
	private LivingEntity user;
	private Item usedItem;
	private int collisionCounter;
	
	
	public ThrownBoomerang(LivingEntity user, Item usedItem) 
	{
		super(usedItem.getTextures().clone());
		renderType = RENDER_TYPE_3D;
		this.user = user;
		this.usedItem = usedItem;
		this.size = usedItem.getSize();
		setDeltaY(0.25F*user.getHeight());
		collisionCounter = 0;
	}
	
	public void performTick()
	{
		Vec2 userPos = user.getPosition();
		Vec2 thisPos = this.getPosition();
		Vec2 dif = userPos.sub(thisPos);
		if(collisionCounter > 10){
			dif.normalize();
			dif.mulLocal(15);
			this.body.setLinearVelocity(dif);
			return;
			
		}
		this.body.applyForceToCenter(dif);
		this.user.setCurrentlyUsing(dif.negate());
	}
	
	public void afterDeploySetup()
	{
		Vec2 mousePos = GraphicRendererV2.getMousePos();
		mousePos.normalize();
		mousePos = mousePos.mul(15);
		this.body.setLinearVelocity(mousePos);
		this.body.setAngularVelocity(10);
		this.user.setCurrentlyUsing(mousePos);
	}
	
	public void onCollision(WorldObject o)
	{
		if(!o.isSensor())
			collisionCounter++;
		if(collisionCounter > 10)
			this.body.getFixtureList().setSensor(true);
		if(o instanceof LivingEntity){
			LivingEntity le = (LivingEntity)o;
			if(le == user){
				this.removeFromWorld();
				usedItem.setCurrentlyInUse(false);
				user.setCurrentlyUsing(null);
				return;
			}
			le.damage(usedItem.getVarOnThrow());
		}

			
	}

	public float getRenderAngle()
	{
		return getAngle()-0.25F*(float)Math.PI;
	}
	
	public FixtureDef getFixtureDef()
    {
    	

    	CircleShape cs = new CircleShape();
    	cs.m_radius = size.x/2;

    	FixtureDef fd = new FixtureDef();
    	fd.shape = cs;
    	fd.density = 0.3f;
    	fd.friction = 0.0f;        
    	fd.restitution = 0.5f;
     	
    	return fd;
    
    }
}
