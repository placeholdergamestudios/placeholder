package org.md2.gameobjects.deco;

import org.jbox2d.common.Vec2;
import org.md2.common.RenderPrio;
import org.md2.common.Texture;
import org.md2.common.VAOType;
import org.md2.gameobjects.DecoObject;
import org.md2.gameobjects.entity.Entity;
import org.md2.gameobjects.entity.living.LivingEntity;

public class Shadow extends DecoObject
{

	protected Entity user;
	
	public Shadow(Entity user) {
		super(new Texture[]{Texture.SHADOW});
		vaoType = VAOType.UNITSQUARE;
		renderPriorisation = RenderPrio.SHADOW;
		this.user = user;
		this.position = new Vec2();
	}
	
	public void performTick()
	{
		this.setTransform(user.getPosition(), user.getAngle());
	}

	@Override
	public float getRenderAngle() {
		return this.getAngle();
	}

	public Vec2 getRenderSize()
	{
		return user.getSize();
	}
	
}
