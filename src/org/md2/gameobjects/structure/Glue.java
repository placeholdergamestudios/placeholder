package org.md2.gameobjects.structure;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.rendering.RenderPrio;
import org.md2.rendering.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;


public class Glue extends Structure
{

	public Glue() 
	{
		super(new Texture[]{Texture.GLUE});
		renderPriorisation = RenderPrio.FLAT;
		size = new Vec2(0.4F, 0.4F);
	}
	
	@Override
	public void onCollision(WorldObject o)
	{
		if(o instanceof LivingEntity){
			LivingEntity le = (LivingEntity)o;
			le.modMovement(0.5f);
		}
			
	}

	@Override
	public FixtureDef getFixtureDef()
	{
		FixtureDef fd = super.getFixtureDef();
		fd.setSensor(true);
		return fd;
	}

}
