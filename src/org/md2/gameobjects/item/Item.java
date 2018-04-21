package org.md2.gameobjects.item;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.md2.common.RenderPrio;
import org.md2.common.Texture;
import org.md2.common.VAOType;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.LivingEntity;


/**
 * Alle Items
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public abstract class Item extends WorldObject
{
	protected boolean currentlyInUse;
	protected final int maxStackSize;
	protected final boolean reuseable;
	private int currentStackSize;
	
	protected int varOnUse;
	protected int varOnAttack;
	protected int varOnThrow;
	
    public Item(Texture[] texture, int maxStackSize, boolean reuseable)
    {
        super(texture);
        renderPriorisation = RenderPrio.ITEM;
        this.maxStackSize = maxStackSize;
        this.reuseable = reuseable;
        currentlyInUse = false;
        currentStackSize = 1;
        initVars();
    }
    
    /**
     * @param i
     * @return null if the given item couldnt be stacked on this item, the new stack if it could
     */
    public boolean addItemToStack(Item i)
	{
		if(currentStackSize + i.getCurrentStackSize() <= maxStackSize && this.getClass().equals(i.getClass())){
			currentStackSize += i.getCurrentStackSize();
			return true;
		}
		return false;
	}
    
    public int getCurrentStackSize()
    {
    		return currentStackSize;
    }
    
    public int getMaxStackSize()
    {
    		return maxStackSize;
    }
    
    public String getStackInformation()
    {
    	if(maxStackSize == 1)
    		return "";
    	else 
    		return currentStackSize+"";
    }
    
    public boolean onUse(LivingEntity user)
    {
    	return use(user);
    }
    
    public boolean onAttack(LivingEntity user)
    {
    	return use(user);
    }
    
    public boolean onThrow(LivingEntity user)
    {
    	return use(user);
    }

    private boolean use(LivingEntity user)
    {
    	if(!this.reuseable)
    	{
    		currentStackSize--;
			if(currentStackSize < 1)
				return true;
    	}
    	return false;
    }
    
    protected void initVars()
    {
    	varOnUse = 0;
    	varOnThrow = 0;
    	varOnAttack = 0;
    }
    
    public int getVarOnUse()
    {
    	return varOnUse;
    }
    public int getVarOnThrow()
    {
    	return varOnThrow;
    }
    public int getVarOnAttack()
    {
    	return varOnAttack;
    }
    
    public void onCollision(WorldObject o)
    {
    	if(o instanceof LivingEntity){
    		LivingEntity le = (LivingEntity)o;
    		le.pickUpItem(this);
    	}
    }
    
    public void setCurrentlyInUse(boolean b)
	{
		currentlyInUse = b;
	}
    
    public BodyDef getBodyDef(float xpos, float ypos)
    {
    	BodyDef bd = new BodyDef();
    	bd.position.set(xpos, ypos);  
    	bd.type = BodyType.STATIC;
    	
    	return bd;
    }
}
