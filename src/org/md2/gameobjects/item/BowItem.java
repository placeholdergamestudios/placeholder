package org.md2.gameobjects.item;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Sound;
import org.md2.common.Texture;
import org.md2.gameobjects.deco.BowAnimation;
import org.md2.gameobjects.DecoObject;
import org.md2.gameobjects.entity.Arrow;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.main.Game;
import org.md2.main.GraphicRendererV2;

public abstract class BowItem extends Item
{
	
	protected float drawingSpeed;
	protected Texture[] animationTextures;
	protected Texture arrowTexture;
	public boolean preparationFinished;

	public BowItem(Texture[] texture1, Texture[] animationTextures, Texture arrowTexture, float drawingSpeed) 
	{
		super(texture1, 1, true);
		this.drawingSpeed = drawingSpeed;
		this.animationTextures = animationTextures;
		this.arrowTexture = arrowTexture;
	}
	
	@Override
	public boolean onUse(LivingEntity user) 
	{
		if(currentlyInUse && !preparationFinished){
			return false;
		}
		Vec2 mousePos = GraphicRendererV2.getMousePos();
		mousePos.normalize();
		Vec2 pos = user.getPosition().add(mousePos.mul(1F));
		if(preparationFinished){
			preparationFinished = false;
			Game.getGame().getMechanicManager().getWorldManager().spawnObjectAt(new Arrow(user, this), pos, (float)Math.atan2(mousePos.y, mousePos.x));
			Game.getGame().getSoundManager().playSound(Sound.BOWRELEASE, 0.3f);
			return super.onUse(user);
		}
		
		DecoObject deco = new BowAnimation(user, this);
		deco.setTransform(pos, (float)Math.atan2(mousePos.y, mousePos.x));
		Game.getGame().getMechanicManager().getWorldManager().spawnDecorationAt(deco);
		setCurrentlyInUse(true);
		Game.getGame().getSoundManager().playSound(Sound.BOWTENSION, 0.3f);
		return super.onUse(user);
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
	
	public float getDrawingSpeed()
	{
		return drawingSpeed;
	}
	
	public Texture[] getAnimationTextures()
	{
		return animationTextures;
	}

	public Texture getArrowTexture()
	{
		return arrowTexture;
	}
	
	
	
	
	
}
