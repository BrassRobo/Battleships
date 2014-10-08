package com.michaelbasov.battleship.gui.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.michaelbasov.battleship.gui.BattleshiplibGDX;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(840, 500);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new BattleshiplibGDX();
        }
}