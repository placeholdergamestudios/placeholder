package org.md2.gameobjects.item;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.rendering.Texture;
import org.md2.gameobjects.entity.living.LivingEntity;


public class Herb extends Item
{
	public Herb()
	{
		super(new Texture[]{Texture.HERB}, 3, false);
		size = new Vec2(0.2F, 0.4F);
	}
	
	protected void initVars()
    {
    	varOnUse = 20;
    	varOnThrow = 0;
    	varOnAttack = 0;
    }

	public boolean onPrimaryUse(LivingEntity user)
	{
		return user.heal(this.varOnUse);
	}

}
