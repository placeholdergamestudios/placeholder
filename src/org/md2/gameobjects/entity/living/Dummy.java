package org.md2.gameobjects.entity.living;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;
import org.md2.common.VAOType;

public class Dummy extends LivingEntity
{

	public Dummy() 
	{
		super(new Texture[]{Texture.DUMMY}, 20, 0);
		size = new Vec2(0.6F, 0.3F);
	}
	
	public FixtureDef getFixtureDef()
	{
		PolygonShape cs = new PolygonShape();
		cs.setAsBox(this.size.x/2, this.size.y/2);  

		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 1.0f;
		fd.friction = 0.0f;        
		fd.restitution = 0.0f;
		
		return fd;
	}
	
	public BodyDef getBodyDef()
    {
    	BodyDef bd = new BodyDef();
    	bd.type = BodyType.STATIC;
    	
    	return bd;
    }
	
	
	
}
