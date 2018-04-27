package org.md2.gameobjects.item.weapons;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.Entity;
import org.md2.gameobjects.entity.ThrownBoomerang;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.Item;
import org.md2.gameobjects.item.WeaponItem;
import org.md2.main.Game;
import org.md2.main.GraphicRendererV2;

public abstract class BoomerangItem extends WeaponItem
{
	
	public BoomerangItem(Texture[] texture)
	{
		super(texture, 0.8F, 15);
		size = new Vec2(0.8F, 0.8F);
	}

	@Override
	public boolean onPrimaryUse(LivingEntity user)
	{
		if(currentlyInUse)
			return false;
		Vec2 mousePos = GraphicRendererV2.getMousePos();
		mousePos.normalize();
		Vec2 entityPos = user.getPosition().add(mousePos.mul(0.5F+0.5F*weaponSize));
		if(Game.getGame().getMechanicManager().getWorldManager().isPositionBlocked(entityPos))
			return false;
		WorldObject wo = new ThrownBoomerang(user, this);
		Game.getGame().getMechanicManager().getWorldManager().spawnObjectAt(wo, entityPos);
		setCurrentlyInUse(true);
		return true;
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
