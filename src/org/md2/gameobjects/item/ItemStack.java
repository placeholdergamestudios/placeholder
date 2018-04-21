package org.md2.gameobjects.item;

import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;
import org.md2.gameobjects.entity.living.LivingEntity;

public class ItemStack extends Item 
{
	private Item stackedType;
	private int currentStackSize;
	public ItemStack(int size, Item stackedType) {
		super(stackedType.getTextures().clone(), 0, true);
		this.stackedType = stackedType;
		currentStackSize = size;
	}

	@Override
	public boolean onUse(LivingEntity user) {
		if(stackedType.onUse(user)){
			currentStackSize--;
			if(currentStackSize < 1)
				return true;
		}
		return false;
	}
	
	public int getCurrentStackSize()
    {

    	return currentStackSize;
    }

	
	public boolean addItemToStack(Item i)
	{
		if(currentStackSize < stackedType.maxStackSize && this.getClass().equals(i.getClass())){
			currentStackSize++;
			return true;
		}
		return false;
	}

	@Override
	public FixtureDef getFixtureDef() 
	{
		return stackedType.getFixtureDef();
	}
	
	
}
