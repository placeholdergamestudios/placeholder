package org.md2.gameobjects.entity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Damage;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WeaponItem;
import org.md2.gameobjects.item.weapons.SwordItem;

public class SwungSword extends WeaponEntity
{
	private int liveTime;

	
	
	public SwungSword(LivingEntity user, WeaponItem usedItem)
	{
		super(user, usedItem);
		this.size = new Vec2(usedItem.getWeaponSize()/2, usedItem.getWeaponSize()/8);
		liveTime = 0;
	}
	
	public void performTick()
	{
		liveTime++;
		float angle = calcSwordAngle(liveTime);
		if(Math.abs(initialDirection-angle) > 0.25*Math.PI){
			this.removeFromWorld();
			usedItem.setCurrentlyInUse(false);
			user.setCurrentlyUsing(null);
			return;
		}
		this.setTransform(new Vec2((float)Math.cos(angle), (float)Math.sin(angle)).mul(initialDistance).add(user.getPosition()), angle);
	}
	
	private float calcSwordAngle(int time)
	{
		return (float)(initialDirection+time*usedItem.getWeaponSpeed()/200*2*Math.PI-0.25*Math.PI);
	}
	
	public Vec2 getRenderSize()
	{
		return super.getRenderSize().mul(usedItem.getWeaponSize());
	}
	
	public void afterDeploySetup()
	{
		super.afterDeploySetup();
		this.setTransform(this.getPosition(), calcSwordAngle(0));
	}
	
	public void onCollision(WorldObject o)
	{
		if(o instanceof LivingEntity){
			LivingEntity le = (LivingEntity)o;
			if(le != user){
				new Damage(Damage.DAMAGESLASH, usedItem.getVarOnAttack(), user, le);
			}
			
		}
	}
	
	public FixtureDef getFixtureDef()
    {
		FixtureDef fd = super.getFixtureDef();
    	fd.setSensor(true);
    	return fd;
    }
	
}
