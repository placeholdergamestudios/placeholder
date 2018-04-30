package org.md2.gameobjects.item;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.rendering.Texture;
import org.md2.gameobjects.entity.living.LivingEntity;


/**
 * Ein Apfel
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Apple extends Item
{
    public Apple()
    {
        super(new Texture[]{Texture.APPLE}, 2, false);
    }
    
    protected void initVars()
    {
    	varOnUse = 50;
    	varOnThrow = 0;
    	varOnAttack = 0;
    }
    
    public boolean onPrimaryUse(LivingEntity user)
    {
    	return user.heal(this.varOnUse);

    }
    
    
    


}
