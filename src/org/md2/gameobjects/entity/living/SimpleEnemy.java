package org.md2.gameobjects.entity.living;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Sound;
import org.md2.rendering.RenderPrio;
import org.md2.rendering.Texture;

public class SimpleEnemy extends LivingEntity {
    public SimpleEnemy()
    {
        super(new Texture[]{Texture.ENEMYFRONT}, 3, new Attributes(1,1,1,1));
        size = new Vec2(0.5F, 0.3F);
        renderPriorisation = RenderPrio.ENTITY;
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

    public void setShouldMove(int shouldMoveY) {
        this.shouldMoveY = shouldMoveY;
    }

    public void setShouldRotate(int shouldMoveX) {
        this.shouldMoveX = -shouldMoveX;
    }


    /*@Override
    public void initWalkingSprites() {
        walkingSprites = new Texture[]{
                Texture.ENEMYFRONT, Texture.PLAYERFRONTW1, Texture.ENEMYFRONT, Texture.PLAYERFRONTW2,
                Texture.PLAYERBACK, Texture.PLAYERBACKW1, Texture.PLAYERBACK, Texture.PLAYERBACKW2,
                Texture.PLAYERRIGHT, Texture.PLAYERRIGHTW1, Texture.PLAYERRIGHTW2, Texture.PLAYERRIGHTW3,
                Texture.PLAYERLEFT, Texture.PLAYERLEFTW1, Texture.PLAYERLEFTW2, Texture.PLAYERLEFTW3};
    }*/

    @Override
    public Sound getWalkingSound()
    {
        return Sound.WALK;
    }
}
