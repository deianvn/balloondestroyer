package io.github.deianvn.gameutils.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureEntity extends RoomEntity {

    private TextureRegion textureRegion;

    public TextureEntity(RoomEntity parent) {
        super(parent);
    }

    public void setTextureRegion(String regionName, String atlasName) {
        setTextureRegion(getRoom().getAsset(atlasName, TextureAtlas.class).findRegion(regionName));
    }

    public void setTextureRegion(String textureName) {
        Texture texture = getRoom().getAsset(textureName, Texture.class);
        textureRegion = new TextureRegion(texture, texture.getWidth(), texture.getHeight());
        setTextureRegion(textureRegion);
    }

    public void setTextureRegion(Texture texture) {
        setTextureRegion(new TextureRegion(texture, texture.getWidth(), texture.getHeight()));
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        onRegionChanged();
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void removeTextureRegion() {
        textureRegion = null;
    }

    @Override
    public void draw() {
        SpriteBatch sb = getRoom().getGame().getSpriteBatch();

        if (textureRegion != null) {
            sb.draw(textureRegion,
                    getPositionX() + getOriginX(), getPositionY() + getOriginY(),
                    getOriginX(), getOriginY(),
                    textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
                    getScaleX(), getScaleY(), getRotation());
        }
    }

    @Override
    protected void computeDimensions() {
        if (textureRegion != null) {
            setWidth(textureRegion.getRegionWidth());
            setHeight(textureRegion.getRegionHeight());
        }
    }

    protected void onRegionChanged() {
        computeDimensions();
    }
}
