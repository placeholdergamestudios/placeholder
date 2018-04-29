package org.md2.gameobjects.deco;
import org.md2.rendering.RenderPrio;
import org.md2.rendering.Texture;
import org.md2.gameobjects.DecoObject;




public abstract class Floor extends DecoObject
{
	public Floor(Texture[] texture)
	{
		super(texture);
		renderPriorisation = RenderPrio.FLOOR;
	}

}
