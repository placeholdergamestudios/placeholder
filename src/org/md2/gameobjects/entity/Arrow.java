package org.md2.gameobjects.entity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Damage;
import org.md2.common.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WeaponItem;
import org.md2.gameobjects.item.weapons.BowItem;

public class Arrow extends Missile
{
	
	public Arrow(LivingEntity user, WeaponItem usedItem, Texture[] textures)
	{
		super(user, usedItem, textures);
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
			new Damage(Damage.DAMAGETHRUST, usedItem.getVarOnThrow(), user, (LivingEntity)hitObject);
		}
	}
}
