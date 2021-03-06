package org.md2.gameobjects.item.weapons;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.rendering.Texture;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.BowAnimation;
import org.md2.gameobjects.entity.Arrow;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WeaponItem;
import org.md2.main.Game;
import org.md2.main.GraphicRendererV2;
import org.md2.main.SoundManager;

public abstract class BowItem extends WeaponItem
{

	protected Texture[] animationTextures;
	protected Texture arrowTexture;
	public boolean preparationFinished;

	public BowItem(Texture[] texture1, Texture[] animationTextures, Texture arrowTexture, float drawingSpeed) 
	{
		super(texture1, 1.5F, drawingSpeed);
		this.animationTextures = animationTextures;
		this.arrowTexture = arrowTexture;
	}
	
	@Override
	public boolean onPrimaryUse(LivingEntity user)
	{
		if(currentlyInUse && !preparationFinished){
			return false;
		}
		Vec2 mousePos = GraphicRendererV2.getMousePos();
		mousePos.normalize();
		Vec2 pos = user.getPosition().add(mousePos.mul(0.5F));
		if(preparationFinished){
			preparationFinished = false;
			worldManager.spawnObjectAt(new Arrow(user, this, new Texture[]{arrowTexture}), pos);
			Game.getGame().getSoundManager().playSoundID(SoundManager.SOUNDBOWRELEASE);
			return true;
		}
		
		WorldObject wo = new BowAnimation(user, this);
		worldManager.spawnObjectAt(wo, pos);
		setCurrentlyInUse(true);
		Game.getGame().getSoundManager().playSoundID(SoundManager.SOUNDBOWTENSION);
		return false;
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
