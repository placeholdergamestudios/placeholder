package org.md2.gameobjects.deco;

import org.jbox2d.common.Vec2;
import org.md2.gameobjects.DecoObject;
import org.md2.gameobjects.entity.Arrow;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.BowItem;
import org.md2.gameobjects.item.WoodenBow;
import org.md2.main.Game;
import org.md2.main.GraphicRendererV2;

public class BowAnimation extends DecoObject
{
	protected LivingEntity user;
	protected BowItem usedItem;
	private int liveTime;
	private int drawingTime;

	public BowAnimation(LivingEntity user, BowItem usedItem) 
	{
		super(usedItem.getTextures().clone());
		renderType = RENDER_TYPE_3D;
		this.user = user;
		this.usedItem = usedItem;
		setDeltaY(0.25F*user.getHeight());
		liveTime = 0;
		drawingTime = (int) (60/usedItem.getDrawingSpeed());
	}
	
	public void performTick()
	{
		liveTime++;
		if(!(Game.getGame().getMechanicManager().getWorldManager().getPlayer().getInventory().isIteminHotbar(usedItem)))
        {
            liveTime = -1;
        }
		if(liveTime == -1){
			this.removeFromWorld();
            usedItem.preparationFinished = false;
			usedItem.setCurrentlyInUse(false);
			user.setCurrentlyUsing(null);
			return;
		}
		else if(liveTime < 0){}
		else if(liveTime > drawingTime+1 && usedItem.preparationFinished == false){
			this.setTexture(usedItem.getTexture());
			liveTime = -drawingTime/2;
		}
		else if(liveTime > drawingTime){
			this.setTexture(usedItem.getAnimationTextures()[1]);
			usedItem.preparationFinished = true;
		}
		else if(liveTime > drawingTime/2){
			this.setTexture(usedItem.getAnimationTextures()[0]);
		}
		
			
		Vec2 mousePos = GraphicRendererV2.getMousePos();
		mousePos.normalize();
		Vec2 pos = user.getPosition().add(mousePos.mul(0.5F));
		this.setTransform(pos, (float)Math.atan2(mousePos.y, mousePos.x));
		user.setCurrentlyUsing(mousePos);
	}
	
	public float getRenderAngle()
	{
		return super.getAngle()-0.25F*(float)Math.PI;
	}
	
	public Vec2 getRenderSize()
	{
		return super.getRenderSize().mul(1.5F);
	}
	
}