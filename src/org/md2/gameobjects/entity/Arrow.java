package org.md2.gameobjects.entity;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.BowItem;
import org.md2.main.GraphicRendererV2;

public class Arrow extends Entity
{

	private LivingEntity user;
	private BowItem usedBow;
	
	private int postHitCounter;
	private WorldObject hitObject;
	private Vec2 hittingVec;
	
	public Arrow(LivingEntity user, BowItem usedBow) 
	{
		super(new Texture[]{usedBow.getArrowTexture()});
		renderType = RENDER_TYPE_3D;
		hitObject = null;
		postHitCounter = 0;
		size = new Vec2(0.6F, 0.4F);
		this.user = user;
		this.usedBow = usedBow;
		setDeltaY(0.25F*user.getHeight());
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
		hittingVec = hitObject.getPosition().sub(this.getPosition());
		this.body.setLinearVelocity(new Vec2());
		if(o instanceof LivingEntity){
			((LivingEntity)hitObject).damage(usedBow.getVarOnThrow());
		}	
	}
	
	public float getRenderAngle()
	{
		return getAngle()-0.25F*(float)Math.PI;
	}
	
	public void performTick()
	{
		if(hitObject != null){
			postHitCounter++;
			if(postHitCounter > 5){
				this.removeFromWorld();
				return;
			}
			this.setTransform(hitObject.getPosition().sub(hittingVec), this.body.getAngle());
		}
		
	}
	
	public void afterDeploySetup()
	{
		Vec2 dir = this.getPosition().sub(user.getPosition());
		dir.normalize();
		this.body.setLinearVelocity(dir.mul(10));
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
