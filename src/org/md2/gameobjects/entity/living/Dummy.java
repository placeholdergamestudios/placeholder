package org.md2.gameobjects.entity.living;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.rendering.Texture;

public class Dummy extends LivingEntity
{

	public Dummy() 
	{
		super(new Texture[]{Texture.DUMMY}, 0, new Attributes(1,1,2,1));
		size = new Vec2(0.3F, 0.2F);
	}
	
	public BodyDef getBodyDef()
    {
    	BodyDef bd = new BodyDef();
    	bd.type = BodyType.STATIC;
    	
    	return bd;
    }
	
	
	
}
