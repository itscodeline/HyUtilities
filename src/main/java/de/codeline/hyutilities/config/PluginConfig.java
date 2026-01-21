package de.codeline.hyutilities.config;

import com.membercat.issuelib.api.annotation.Property;
import com.membercat.issuelib.api.loader.ConfigLoadable;

/**
 * PluginConfig (typically config.yml) defining the values of the main config
 */
public class PluginConfig implements ConfigLoadable {

    public @Property GeneralMainConfig general;
    public @Property EnabledFeaturesConfig features;

    public static class GeneralMainConfig implements ConfigLoadable {
        public @Property String language = "en_US";
    }

    public static class EnabledFeaturesConfig implements ConfigLoadable {
        public @Property boolean gamemodeCommandEnabled = true;
    }

}