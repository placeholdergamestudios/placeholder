package org.md2.gameobjects.entity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.gameobjects.DecoObject;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.weapons.BowItem;
import org.md2.main.Game;
import org.md2.main.GraphicRendererV2;

public class BowAnimation extends WeaponEntity
{
	private int liveTime;
	private int drawingTime;

	public BowAnimation(LivingEntity user, BowItem usedItem) 
	{
		super(user, usedItem);
		size = new Vec2(0.8F, 0.4F);
		liveTime = 0;
		drawingTime = (int) (60/usedItem.getWeaponSpeed());
	}
	
	public void performTick()
	{
		BowItem bowItem = (BowItem) usedItem;
		liveTime++;
		user.modMovement(0.55f);
		if(!(Game.getGame().getMechanicManager().getWorldManager().getPlayer().getInventory().isIteminHotbar(usedItem)))
        {
            liveTime = -1;
        }
		if(liveTime == -1){
			this.removeFromWorld();
			bowItem.preparationFinished = false;
			usedItem.setCurrentlyInUse(false);
			user.setCurrentlyUsing(null);
			return;
		}
		else if(liveTime < 0){}
		else if(liveTime > drawingTime+1 && bowItem.preparationFinished == false){
			this.setTexture(usedItem.getTexture());
			liveTime = -drawingTime/2;
		}
		else if(liveTime > drawingTime){
			this.setTexture(bowItem.getAnimationTextures()[1]);
			bowItem.preparationFinished = true;
		}
		else if(liveTime > drawingTime/2){
			this.setTexture(bowItem.getAnimationTextures()[0]);
		}
			
		Vec2 mousePos = GraphicRendererV2.getMousePos();
		mousePos.normalize();
		this.setTransform(user.getPosition().add(mousePos.mul(initialDistance)), (float)Math.atan2(mousePos.y, mousePos.x));
		user.setCurrentlyUsing(mousePos);
	}
	
	public Vec2 getRenderSize()
	{
		return super.getRenderSize().mul(usedItem.getWeaponSize());
	}

	@Override
	public void afterDeploySetup()
	{
		super.afterDeploySetup();
		this.setTransform(this.body.getPosition(), initialDirection);
	}

	public FixtureDef getFixtureDef()
	{
		PolygonShape cs = new PolygonShape();
		cs.setAsBox(0.0f, 0.0f);

		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.setSensor(true);
		return fd;
	}
	
}