package com.slamdunk.wordarena.screens.preeditor;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.slamdunk.wordarena.screens.SlamScene;
import com.slamdunk.wordarena.screens.home.components.ArenaSelector;
import com.slamdunk.wordarena.screens.preeditor.components.CreateButton;
import com.slamdunk.wordarena.screens.preeditor.components.GroupLabel;
import com.slamdunk.wordarena.screens.preeditor.components.HeightTextField;
import com.slamdunk.wordarena.screens.preeditor.components.InputLabel;
import com.slamdunk.wordarena.screens.preeditor.components.ModifyButton;
import com.slamdunk.wordarena.screens.preeditor.components.NameTextField;
import com.slamdunk.wordarena.screens.preeditor.components.WidthTextField;

/**
 * Created by Didier on 05/09/2015.
 */
public class PreEditorScene extends SlamScene {

    @Override
    public void create(Skin skin) {
        createNewArenaGroupLabel(skin);
        createNewArenaNameInputLabel(skin);
        createNewArenaNameTextField(skin);
        createNewArenaSizeInputLabel(skin);
        createNewArenaWidthTextField(skin);
        createNewArenaXInputLabel(skin);
        createNewArenaHeightTextField(skin);
        createNewArenaButton(skin);

        createModifyArenaGroupLabel(skin);
        createModifyArenaNameInputLabel(skin);
        createArenaSelector(skin);
        createModifyArenaButton(skin);
    }

    private void createNewArenaGroupLabel(Skin skin) {
        Label lblNewArena = new GroupLabel("Create new arena", skin);
        lblNewArena.setSize(480, 50);
        lblNewArena.setPosition(0, 600);
        addActor(lblNewArena);
    }

    private void createNewArenaNameInputLabel(Skin skin) {
        Label lblName = new InputLabel("Name : ", skin);
        lblName.setPosition(240, 580, Align.right);
        addActor(lblName);
    }

    private void createNewArenaNameTextField(Skin skin) {
        TextField txtName = new NameTextField(skin);
        txtName.setSize(150, 50);
        txtName.setPosition(240, 580);
        addActor(txtName);
    }

    private void createNewArenaSizeInputLabel(Skin skin) {
        Label lblSize = new InputLabel("Size : ", skin);
        lblSize.setPosition(240, 530, Align.right);
        addActor(lblSize);
    }

    private void createNewArenaWidthTextField(Skin skin) {
        TextField txtWidth = new WidthTextField(skin);
        txtWidth.setSize(50, 50);
        txtWidth.setPosition(240, 530);
        addActor(txtWidth);
    }

    private void createNewArenaXInputLabel(Skin skin) {
        Label lblX = new InputLabel(" x ", skin);
        lblX.setPosition(290, 530);
        addActor(lblX);
    }

    private void createNewArenaHeightTextField(Skin skin) {
        TextField txtHeight = new HeightTextField(skin);
        txtHeight.setSize(50, 50);
        txtHeight.setPosition(310, 530);
        addActor(txtHeight);
    }

    private void createNewArenaButton(Skin skin) {
        Button btnCreate = new CreateButton(skin, this);
        btnCreate.setSize(150, 50);
        btnCreate.setPosition(240 - btnCreate.getWidth() / 2, 470);
        addActor(btnCreate);
    }

    private void createModifyArenaGroupLabel(Skin skin) {
        Label lblModifyArena = new GroupLabel("Modify existing arena", skin);
        lblModifyArena.setSize(480, 50);
        lblModifyArena.setPosition(0, 400);
        addActor(lblModifyArena);
    }

    private void createModifyArenaNameInputLabel(Skin skin) {
        Label lblName = new InputLabel("Name : ", skin);
        lblName.setPosition(240, 380, Align.right);
        addActor(lblName);
    }

    private void createArenaSelector(Skin skin) {
        SelectBox<String> selArena = new ArenaSelector(skin);
        selArena.setSize(150, 50);
        selArena.setPosition(240, 380);
        addActor(selArena);
    }

    private void createModifyArenaButton(Skin skin) {
        Button btnModify = new ModifyButton(skin, this);
        btnModify.setSize(150, 50);
        btnModify.setPosition(240 - btnModify.getWidth() / 2, 320);
        addActor(btnModify);
    }
}
