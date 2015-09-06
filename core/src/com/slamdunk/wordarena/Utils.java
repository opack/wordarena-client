package com.slamdunk.wordarena;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class Utils {
	/**
	 * Crée une liste avec le nom de toutes les arènes présentes dans les assets
	 * @return
	 */
	public static Array<String> loadArenaNames() {
		Array<String> names = new Array<String>();
		
		FileHandle dirArenas;
		if (Gdx.app.getType() == ApplicationType.Android) {
			dirArenas = Gdx.files.internal("arenas");
		} else {
			// ApplicationType.Desktop ..

			// Pour Eclipse, la racine est le répertoire des binaires compilés car le répertoire
			// des assets est un lien vers android/assets et est donc recopié dans les binaires
			// dirArenas = Gdx.files.internal("./bin/arenas");

			// Pour IntelliJ IDEA, la racine de Gdx.files.internal est le working directory,
			// donc le répertoire android/assets
			dirArenas = Gdx.files.internal("./arenas");
		}
		
		FileHandle[] filesArenas = dirArenas.list(".json");
		for (FileHandle file : filesArenas) {
			names.add(file.nameWithoutExtension());
		}
		return names;
	}
}
