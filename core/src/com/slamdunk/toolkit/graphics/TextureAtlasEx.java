package com.slamdunk.toolkit.graphics;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

/**
 * Un atlas capable de corriger le texture bleeding et de renvoyer des animations
 */
public class TextureAtlasEx extends TextureAtlas {
	public TextureAtlasEx(String internalPackFile) {
		super(internalPackFile);
	}
	
	public Animation findAnimation(String name, float frameDuration, boolean fixTextureBleeding) {
		Array<AtlasRegion> regions = findRegions(name);
		
		if (regions == null || regions.size == 0) {
			return null;
		}
		
		if (fixTextureBleeding) {
			for (TextureRegion region : regions) {
				fixBleeding(region);
			}
		}
		
		return new Animation(frameDuration, regions);
	}
	
	public AtlasRegion findRegion(String name, boolean fixTextureBleeding) {
		final AtlasRegion region = super.findRegion(name);
		
		if (region == null) {
			return null;
		}
		
		if (fixTextureBleeding) {
			fixBleeding(region);
		}
		
		return region;
	}
	
	public TextureRegionDrawable findRegionDrawable(String name, boolean fixTextureBleeding) {
		final AtlasRegion region = findRegion(name, fixTextureBleeding);
		
		if (region == null) {
			return null;
		}
		
		return new TextureRegionDrawable(region);
	}
	
	/**
	 * Permet de corriger le texture bleeding en décalant les coordonnées de la région d'un demi-pixel.
	 * Cette méthode vient de http://www.wendytech.de/2012/08/fixing-bleeding-in-libgdxs-textureatlas/.
	 * @param region
	 */
	public static void fixBleeding(TextureRegion region) {
		float x = region.getRegionX();
		float y = region.getRegionY();
		float width = region.getRegionWidth();
		float height = region.getRegionHeight();
		float invTexWidth = 1f / region.getTexture().getWidth();
		float invTexHeight = 1f / region.getTexture().getHeight();
		region.setRegion((x + .5f) * invTexWidth, (y+.5f) * invTexHeight, (x + width - .5f) * invTexWidth, (y + height - .5f) * invTexHeight);       
	}
}
