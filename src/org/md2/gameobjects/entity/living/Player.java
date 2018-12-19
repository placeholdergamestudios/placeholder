package org.md2.gameobjects.entity.living;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.rendering.RenderPrio;
import org.md2.common.Sound;
import org.md2.rendering.Texture;

/**
 * Beschreiben Sie hier die Klasse Player.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Player extends LivingEntity
{
    
    public Player()
    {
        super(new Texture[]{Texture.PLAYERFRONT}, 5, new Attributes(2,2,2,2));
        size = new Vec2(0.5F, 0.3F);
        renderPriorisation = RenderPrio.PLAYER;
    }

	public FixtureDef getFixtureDef()
    {
		PolygonShape cs = new PolygonShape();
		Vec2 [] vertices = {new Vec2(-this.size.x, 0), new Vec2(0, -this.size.y), new Vec2(this.size.x, 0), new Vec2(0, this.size.y)};
		cs.set(vertices, 4);

    	FixtureDef fd = new FixtureDef();
    	fd.shape = cs;
    	fd.density = 0.5f;
    	fd.friction = 0.0f;        
    	fd.restitution = 0.0f;
    	
    	return fd;
    }

	@Override
	public void initWalkingSprites() {
		walkingSprites = new Texture[]{
				Texture.PLAYERFRONT, Texture.PLAYERFRONTW1, Texture.PLAYERFRONT, Texture.PLAYERFRONTW2,
				Texture.PLAYERBACK, Texture.PLAYERBACKW1, Texture.PLAYERBACK, Texture.PLAYERBACKW2,
				Texture.PLAYERRIGHT, Texture.PLAYERRIGHTW1, Texture.PLAYERRIGHTW2, Texture.PLAYERRIGHTW3,
				Texture.PLAYERLEFT, Texture.PLAYERLEFTW1, Texture.PLAYERLEFTW2, Texture.PLAYERLEFTW3};
	}

	@Override
	public Sound getWalkingSound()
	{
		return Sound.WALK;
	}
    
}
