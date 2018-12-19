package org.md2.gameobjects.entity.living;

import org.jbox2d.common.Vec2;
import org.md2.rendering.Texture;
import org.md2.rendering.VAOType;
import org.md2.gameobjects.entity.Entity;
import org.md2.gameobjects.item.Item;
import org.md2.main.Game;
import org.md2.main.SoundManager;
import org.md2.worldmanagement.Inventory;


/**
 * Beschreiben Sie hier die Klasse LivingEntity.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public abstract class LivingEntity extends Entity
{
	protected static int walkingAnimationFrames = 4;
	protected int walkingAnimationState = 0;
	protected Texture[] walkingSprites;
	protected float height;
	protected float shouldMoveY;
	protected float shouldMoveX;
	protected float shouldAttack;
    protected int maxHealth;
    protected Inventory inventory;
    protected float maxMS;// the maximal movement speed this LE can reach under normal circumstances
    protected int health;
    protected Vec2 currentlyUsed; //whether this LE is currently using an entity, e.g. swinging a sword
	protected Attributes attributes;
    
    public LivingEntity(Texture[] texture, float maxMS, Attributes attributes)
    {
        super(texture);
        renderType = RENDER_TYPE_3D;
        vaoType = VAOType.ENTITY_TALL;
        
        initWalkingSprites();
        height = 1.25F;
        setDeltaY(height/2);
        
        inventory = new Inventory(25);
        this.attributes = attributes;
        this.maxHealth = (attributes.getVitality())*8+25;
        this.health = maxHealth;
        this.maxMS = maxMS;
        this.currentlyUsed = null;
    }

    //the walking sprites need to be filled into the array like this: Front, Back, Right, Left (look it up in 'Player')
    //will set all the walking sprites to the front texture if this method isnt overwritten
    public void initWalkingSprites()
    {
    	walkingSprites = new Texture[4*walkingAnimationFrames];
    	for(int x = 0; x < walkingSprites.length; x++){
    		walkingSprites[x] = this.texture[0];
    	}
    }
    
    public boolean heal(int i)
    {
    	if(i<0)
    		return false;
    	if(health >= maxHealth)
    		return false;
    	
    	health = health + i;
    	if(health > maxHealth)
    		health = maxHealth;
    	return true;
    }
    
	public void damage(int i) 
	{
		if(i<0 || this.willBeRemoved())
    		return;
		health = health - i;
		if(health <= 0)
			this.removeFromWorld();
		Game.getGame().getSoundManager().playSoundID(SoundManager.SOUNDAUA);
			
	}
    
    public float getPercentHealth()
    {
    	return health/(float)maxHealth;
    }
    
    public void pickUpItem(Item i)
    {
    	if(inventory.add(i))
		{
			i.removeFromWorld();
			Game.getGame().getSoundManager().playSoundID(SoundManager.SOUNDPICKUP);
		}
    }
    
    public void useItem(Item i, int usageType)
    {
    	if(i == null)
    		return;
    	if(i.onUse(this, usageType))
    		inventory.removeItem(i);
    }

    public void modMovement(float multiplier)
    {
    	modifiedMovement = modifiedMovement*multiplier;
    	
    }
    
    public Inventory getInventory()
    {
    	return inventory;
    }
    
    public void performTick()
    {
    	float fX = shouldMoveX*maxMS*modifiedMovement;
        float fY = shouldMoveY*maxMS*modifiedMovement;
        if(shouldMoveX != 0 && shouldMoveY != 0){
        	fX *= 0.708F;
        	fY *= 0.708F;
        }
        body.setLinearVelocity(new Vec2(fX, fY));
        modifiedMovement = 1;
        updateWalkingTexture();
        super.performTick();
        
    }
    
    private void updateWalkingTexture()
    {
    	int index = 0;
    	if(shouldMoveX != 0 || shouldMoveY != 0)
    		index = walkingAnimationState;
    	float fX = shouldMoveX, fY = shouldMoveY;
    	if(currentlyUsed != null){
    		Vec2 dif = currentlyUsed;
    		if(Math.abs(dif.x) > Math.abs(dif.y)){
    			fX = dif.x;
    			fY = 0;
    		} else{
    			fX = 0;
    			fY = dif.y;
    		}
    			
    	}
    	if(fY < 0){ //front
        	this.setTexture(walkingSprites[0*walkingAnimationFrames+index]);
        }
        else if(fY > 0){ //back
        	this.setTexture(walkingSprites[1*walkingAnimationFrames+index]);
        }
        else if(fX > 0){ //right
        	this.setTexture(walkingSprites[2*walkingAnimationFrames+index]);
        }
        else if(fX < 0){ //left
        	this.setTexture(walkingSprites[3*walkingAnimationFrames+index]);
        }
        else{
        	this.setTexture(walkingSprites[0]);
        }
    }
    
    public void animationUpdate()
	{
		if(walkingAnimationState < walkingAnimationFrames-1)
			walkingAnimationState++;
		else
			walkingAnimationState = 0;
	}
    
	public void resetShoulds()
	{
		shouldMoveY = 0;        
		shouldMoveX = 0;      
		shouldAttack = 0;      
		                   
	}
	
	public void useHotbarItem(int index, int usageType)
	{
		
		Item i = inventory.getHotbarItem(index);
		if(i != null)
			useItem(i, usageType);
			
	}

	public void setShouldMove(int shouldMoveY) {
		this.shouldMoveY = shouldMoveY;
	}

	public void setShouldRotate(int shouldMoveX) {
		this.shouldMoveX = -shouldMoveX;
	}
	
	public void setCurrentlyUsing(Vec2 v)
	{
		currentlyUsed = v;
	}
	

	
	public float getHeight()
	{
		return height;
	}

	public Attributes getAttributes(){return attributes;}

    
    
}
