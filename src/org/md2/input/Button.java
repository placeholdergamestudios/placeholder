package org.md2.input;

import org.jbox2d.common.Vec2;
import org.md2.common.Tools;
import org.md2.main.Game;
import org.md2.main.GraphicRendererV2;
import org.md2.rendering.Texture;

public abstract class Button
{
    private final Vec2 coordinates;
    private final Vec2 size;
    private Texture[] texturesNormal;
    private Texture[] texturesClicked;
    private final int activeGameState; //the state of the game, when this button is clickable
    private boolean isCurrentlyClicked = false;

    public Button(Vec2 coordinates, Vec2 size, int activeGameState, Texture[] texturesNormal)
    {
        this.coordinates = coordinates;
        this.size = size;
        this.activeGameState = activeGameState;
        this.texturesNormal = texturesNormal;
        this.texturesClicked = texturesNormal;

    }

    public Button(Vec2 coordinates, Vec2 size, int activeGameState, Texture[] texturesNormal, Texture[] texturesClicked)
    {
        this.coordinates = coordinates;
        this.size = size;
        this.activeGameState = activeGameState;
        this.texturesNormal = texturesNormal;
        this.texturesClicked = texturesClicked;
    }

    public boolean isActive()
    {
        return activeGameState == Game.getGame().getMenue();
    }

    public boolean refresh()
    {
        if(isCurrentlyClicked && !wasClicked())
            performAction();
        isCurrentlyClicked = wasClicked() && isActive();
        return isCurrentlyClicked;
    }

    protected abstract void performAction();

    public boolean wasClicked()
    {
        return Tools.vec2InsideRect(coordinates, size, GraphicRendererV2.getMousePos());
    }


    public Texture[] getTexture()
    {
        return isCurrentlyClicked ? texturesClicked : texturesNormal;
    }

    public Vec2 getCoordinates() {
        return coordinates;
    }

    public Vec2 getSize() {
        return size;
    }
}
