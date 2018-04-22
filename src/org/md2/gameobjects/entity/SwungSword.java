package org.md2.gameobjects.entity;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.Item;
import org.md2.gameobjects.item.SwordItem;
import org.md2.main.GraphicRendererV2;

public class SwungSword extends Entity
{
	private LivingEntity user;
	private SwordItem usedItem;
	private int liveTime;
	
	private float swingingDirection;
	
	
	public SwungSword(LivingEntity user, SwordItem usedItem) 
	{
		super(usedItem.getTextures().clone());
		renderType = RENDER_TYPE_3D;
		this.user = user;
		this.usedItem = usedItem;
		this.size = new Vec2(usedItem.getSwordLength(), 0.2F);
		setDeltaY(0.25F*user.getHeight());
		liveTime = 0;
	}
	
	public void performTick()
	{
		liveTime++;
		float angle = calcSwordAngle(liveTime);
		if(Math.abs(swingingDirection-angle) > 0.25*Math.PI){
			this.removeFromWorld();
			usedItem.setCurrentlyInUse(false);
			user.setCurrentlyUsing(null);
			return;
		}
		this.setTransform(new Vec2((float)Math.cos(angle), (float)Math.sin(angle)).mul((0.5F+0.5F*usedItem.getSwordLength())).add(user.getPosition()), angle);
	}
	
	private float calcSwordAngle(int time)
	{
		return (float)(swingingDirection+time*usedItem.getSwingingSpeed()/200*2*Math.PI-0.25*Math.PI);
	}
	
	public float getRenderAngle()
	{
		return getAngle()-0.25F*(float)Math.PI;
	}
	
	public Vec2 getRenderSize()
	{
		return super.getRenderSize().mul(usedItem.getSwordLength());
	}
	
	public void afterDeploySetup()
	{
		Vec2 userPos = user.getPosition();
		Vec2 thisPos = this.body.getPosition();
		Vec2 dif = userPos.sub(thisPos);
		dif.normalize();
		swingingDirection = (float)Math.atan2(-dif.y, -dif.x);
		this.setTransform(this.body.getPosition(), calcSwordAngle(liveTime));
		this.user.setCurrentlyUsing(dif.negate());
	}
	
	public void onCollision(WorldObject o)
	{
		if(o instanceof LivingEntity){
			LivingEntity le = (LivingEntity)o;
			if(le != user){
				le.damage(usedItem.getVarOnAttack());
			}
			
		}
	}
	
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
