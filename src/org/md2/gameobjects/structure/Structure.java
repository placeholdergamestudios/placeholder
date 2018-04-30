package org.md2.gameobjects.structure;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.rendering.RenderPrio;
import org.md2.rendering.Texture;
import org.md2.gameobjects.WorldObject;


/**
 * 
 * Alle unbeweglichen objekte
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
abstract public class Structure extends WorldObject
{
	
    public Structure(Texture[] texture)
    {
        super(texture);
        renderPriorisation = RenderPrio.STRUCTURE;
    }

    public FixtureDef getFixtureDef()
    {
        PolygonShape cs = new PolygonShape();
        cs.setAsBox(size.x, size.y);

        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;

        return fd;
    }
    
}
