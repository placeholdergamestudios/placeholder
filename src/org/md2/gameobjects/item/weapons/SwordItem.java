package org.md2.gameobjects.item.weapons;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.rendering.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.SwungSword;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WeaponItem;
import org.md2.main.Game;
import org.md2.main.GraphicRendererV2;
import org.md2.main.SoundManager;

public abstract class SwordItem extends WeaponItem
{
	
	public SwordItem(Texture[] texture, float length, float swingingSpeed)
	{
		super(texture, length, swingingSpeed);
	}

	@Override
	public boolean onPrimaryUse(LivingEntity user)
	{
		if(currentlyInUse)
			return false;
		Vec2 mousePos = GraphicRendererV2.getMousePos();
		mousePos.normalize();
		Vec2 entityPos = user.getPosition().add(mousePos.mul(0.5F+0.5F*this.getWeaponSize()));
		WorldObject wo = new SwungSword(user, this);
		worldManager.spawnObjectAt(wo, entityPos);
		Game.getGame().getSoundManager().playSoundID(SoundManager.SOUNDSWORDSLASH);
		setCurrentlyInUse(true);
		return true;
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
