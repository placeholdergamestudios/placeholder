package org.md2.gameobjects.structure;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.RenderPrio;
import org.md2.common.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;


public class Glue extends Structure
{

	public Glue() 
	{
		super(new Texture[]{Texture.GLUE});
		renderPriorisation = RenderPrio.FLAT;
	}
	
	@Override
	public void onCollision(WorldObject o)
	{
		if(o instanceof LivingEntity){
			LivingEntity le = (LivingEntity)o;
			le.modMovement(0.5f);
		}
			
	}
	
	
	public FixtureDef getFixtureDef()
    {
    	PolygonShape cs = new PolygonShape();
    	cs.setAsBox(0.4f, 0.4f);  

    	FixtureDef fd = new FixtureDef();
    	fd.shape = cs;
    	fd.density = 0.0f;
    	fd.friction = 0.0f;        
    	fd.restitution = 0.0f;
    	fd.setSensor(true);
    	
    	return fd;
    }
}
