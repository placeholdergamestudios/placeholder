package org.md2.worldmanagement;

import org.jbox2d.common.Vec2;
import org.joml.Vector4f;
import org.md2.common.Tools;
import org.md2.gameobjects.item.Item;
import org.md2.main.GraphicRendererV2;
import org.md2.rendering.Texture;

public class InventorySlot
{
    private Vec2 coordinates;
    private Vec2 size;
    private Texture[] textures;
    private Item storedItem;

    public InventorySlot(Vec2 coordinates, Vec2 size, Texture specialTexture)
    {
        this.coordinates = coordinates;
        this.size = size;
        textures = new Texture[]{Texture.INVENTORY_SLOT};
    }

    public Texture[] getTexture()
    {
        return textures;
    }

    public Vec2 getCoordinates()
    {
        return coordinates;
    }

    public Vec2 getSize()
    {
        return size;
    }

    public boolean wasClicked()
    {
        return Tools.vec2InsideRect(coordinates, size, GraphicRendererV2.getMousePos());
    }

    public boolean addItem(Item i)
    {
        if(storedItem == null)
        {
            storedItem = i;
            return true;
        }
        else if(storedItem.addItemToStack(i))
        {
            return true;
        }
        return false;
    }

    public Item getItem()
    {
        return storedItem;
    }

    public Item removeItem()
    {
        Item buffer = storedItem;
        storedItem = null;
        return buffer;
    }

    public Item switchItem(Item i)
    {
        if(i != null && storedItem != null && storedItem.addItemToStack(i))
        {
            return null;
        }
        Item buffer = storedItem;
        storedItem = i;
        return buffer;
    }

}
