package org.md2.gameobjects.item.weapons;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.rendering.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.ThrownBoomerang;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WeaponItem;
import org.md2.main.Game;
import org.md2.main.GraphicRendererV2;

public abstract class BoomerangItem extends WeaponItem
{
	
	public BoomerangItem(Texture[] texture)
	{
		super(texture, 0.8F, 15);
		size = new Vec2(getWeaponSize()/2, getWeaponSize()/2);
	}

	@Override
	public boolean onPrimaryUse(LivingEntity user)
	{
		if(currentlyInUse)
			return false;
		Vec2 mousePos = GraphicRendererV2.getMousePos();
		mousePos.normalize();
		Vec2 entityPos = user.getPosition().add(mousePos.mul(0.6F+0.5F*this.getWeaponSize()));
		if(worldManager.isPositionBlocked(entityPos))
			return false;
		WorldObject wo = new ThrownBoomerang(user, this);
		worldManager.spawnObjectAt(wo, entityPos);
		setCurrentlyInUse(true);
		return true;
	}
}
