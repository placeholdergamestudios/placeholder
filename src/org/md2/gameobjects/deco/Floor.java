package org.md2.gameobjects.deco;
import org.md2.common.RenderPrio;
import org.md2.common.Texture;
import org.md2.gameobjects.DecoObject;




public abstract class Floor extends DecoObject
{
	public Floor(Texture[] texture)
	{
		super(texture);
		renderPriorisation = RenderPrio.FLOOR;
	}

}
