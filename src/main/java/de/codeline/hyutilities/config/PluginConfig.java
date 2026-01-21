package de.codeline.hyutilities.config;

import com.membercat.issuelib.api.annotation.Property;
import com.membercat.issuelib.api.issue.IssueContext;
import com.membercat.issuelib.api.loader.ConfigLoadable;
import com.membercat.issuelib.api.loader.LoaderContext;
import de.codeline.hyutilities.HyUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * PluginConfig (typically config.yml) defining the values of the main config
 */
public class PluginConfig implements ConfigLoadable {

    public @Property GeneralMainConfig general;
    public @Property EnabledFeaturesConfig features;

    public static class GeneralMainConfig implements ConfigLoadable {
        @Override
        public @Nullable Object onLoadProperty(@NotNull String key, @Nullable Object current, @NotNull Field field, IssueContext ctx, LoaderContext loaderCtx) {
            if (!key.equals("language") || !(current instanceof String language)) return current;
            if (HyUtilities.getLangManager().loadFile(language) == null) {
                ctx.append(Namespace.INSTANCE.langNotFoundIssue);
                return "en_US";
            }
            return current;
        }

        public @Property String language = "en_US";
    }

    public static class EnabledFeaturesConfig implements ConfigLoadable {
        public @Property boolean gamemodeCommandEnabled = true;
    }

}