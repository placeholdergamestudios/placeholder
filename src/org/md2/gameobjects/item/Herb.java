package org.md2.gameobjects.item;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;
import org.md2.gameobjects.entity.living.LivingEntity;


public class Herb extends Item
{
	public Herb()
	{
		super(new Texture[]{Texture.HERB}, 3, false);
	}
	
	protected void initVars()
    {
    	varOnUse = 20;
    	varOnThrow = 0;
    	varOnAttack = 0;
    }
	
	public FixtureDef getFixtureDef()
    {
    	PolygonShape cs = new PolygonShape();
    	cs.setAsBox(0.2f, 0.4f);  

    	FixtureDef fd = new FixtureDef();
    	fd.shape = cs;
    	fd.density = 0.0f;
    	fd.friction = 0.0f;        
    	fd.restitution = 0.0f;
    	fd.setSensor(true);
    	
    	return fd;
    }
	

	public boolean onPrimaryUse(LivingEntity user)
	{
		return user.heal(this.varOnUse);
	}

}
