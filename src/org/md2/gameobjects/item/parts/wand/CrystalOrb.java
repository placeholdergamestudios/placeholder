package org.md2.gameobjects.item.parts.wand;

import org.jbox2d.common.Vec2;
import org.md2.gameobjects.WorldObject;
import org.md2.rendering.Texture;
import org.md2.gameobjects.entity.Fireball;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.item.WeaponItem;
import org.md2.gameobjects.item.parts.WeaponPart;
import org.md2.main.Game;
import org.md2.main.GraphicRendererV2;
import org.md2.main.SoundManager;

public class CrystalOrb implements WeaponPart {

    @Override
    public Texture getTexture() {
        return Texture.CRYSTALORB;
    }

    @Override
    public Texture getEffectTexture(){return Texture.FIREBALL;}

    @Override
    public void onUse(LivingEntity user, WeaponItem weapon)
    {
        Vec2 mousePos = GraphicRendererV2.getMousePos();
        mousePos.normalize();
        Vec2 entityPos = user.getPosition().add(mousePos.mul(0.5F+0.5F*weapon.getWeaponSize()));
        WorldObject.worldManager.spawnObjectAt(new Fireball(user, weapon, new Texture[]{getEffectTexture()}), entityPos);
        Game.getGame().getSoundManager().playSoundID(SoundManager.SOUNDFIREBALL);

    }
}
