package com.slamdunk.toolkit.ui.loader.builders.layouts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.ui.loader.JsonUIBuilder;

public class ScrollPaneJsonBuilder extends LayoutJsonBuilder {
	
	public ScrollPaneJsonBuilder(JsonUIBuilder creator) {
		super(creator);
	}

	@Override
	protected ScrollPane createEmpty(Skin skin, String style) {
		return new ScrollPane(null);
	}
	
	@Override
	public Actor build(Skin skin) {
		// Gère les propriétés basiques du widget
		ScrollPane scrollPane = (ScrollPane)super.build(skin);
		
		// Gère la propriété widget
		parseWidget(scrollPane);
		
		scrollPane.pack();
		return scrollPane;
	}

	private void parseWidget(ScrollPane scrollPane) {
		if (hasProperty("widget")) {
			Actor widget = getCreator().build(getJsonProperty("widget"));
			scrollPane.setWidget(widget);
		}
	}
}
