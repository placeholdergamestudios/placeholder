package org.md2.gameobjects.structure;
import org.md2.common.RenderPrio;
import org.md2.common.Texture;
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
    
    
    
}
