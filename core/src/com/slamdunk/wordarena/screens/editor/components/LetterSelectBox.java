package com.slamdunk.wordarena.screens.editor.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.screen.SlamScene;
import com.slamdunk.wordarena.enums.Letters;
import com.slamdunk.wordarena.screens.editor.EditorScreen;
import com.slamdunk.wordarena.screens.editor.tools.LetterTool;

/**
 * Liste déroulante proposant le choix d'une lettre
 */
public class LetterSelectBox extends SelectBox<Letters> {
    public static final String NAME = LetterSelectBox.class.getName();

    public LetterSelectBox(Skin skin, final SlamScene scene) {
        super(skin);
        setName(NAME);

        Array<Letters> letters = new Array<Letters>();
        letters.add(Letters.FROM_TYPE);
        for (Letters letter : Letters.values()) {
            // Seules les lettres avec une représentation de 0 sont affichées.
            // On n'affiche donc pas les lettres spéciales comme le JOKER, EMPTY ou FROM_TYPE.
            if (letter.representation > 0) {
                letters.add(letter);
            }
        }
        setItems(letters);

        // Ajoute le listener pour màj la valeur applicable dans l'outil
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                final EditorScreen screen = (EditorScreen)scene.getScreen();
                LetterTool tool = screen.getTool(LetterTool.class);
                tool.setValue(getSelected());
            }
        });

        // Suppression barbare de la sélection avec le clear puis sélection du premier item :
        // cette technique vise à déclencher le changeListener pour que l'outil lié reçoive
        // bien la valeur actuellement visible comme étant sélectionnée aux yeux de l'utilisateur
        getSelection().clear();
        setSelectedIndex(0);
    }
}
