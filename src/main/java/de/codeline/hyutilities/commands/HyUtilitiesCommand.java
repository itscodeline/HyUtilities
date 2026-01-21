package de.codeline.hyutilities.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import de.codeline.hyutilities.HyUtilities;
import de.codeline.hyutilities.config.PluginConfig;
import de.codeline.hyutilities.lang.LangManager;
import org.jetbrains.annotations.NotNull;

public class HyUtilitiesCommand extends AbstractPlayerCommand {

    private final LangManager langManager;
    private final PluginConfig mainConfig;
    private final HyUtilities hyUtilities;

    public HyUtilitiesCommand(HyUtilities hyUtilities) {
        super("hyutilities", "Primary command for using subcommands directly related to the HyUtilities plugin.", false);
        this.langManager = hyUtilities.getLangManager();
        this.mainConfig = hyUtilities.getMainConfig();
        this.hyUtilities = hyUtilities;
        this.addSubCommand(new ReloadCommand(hyUtilities));
        this.addUsageVariant(this);
    }

    @Override
    protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
        hyUtilities.reloadConfig();
    }

    public static class ReloadCommand extends AbstractPlayerCommand {

        RequiredArg<String> reloadableArg = this.withRequiredArg("reloadable", "Specify what should be reloaded (Options: 'config', 'lang')", ArgTypes.STRING);
        private final LangManager langManager;
        private final HyUtilities hyUtilities;

        public ReloadCommand(HyUtilities hyUtilities) {
            super("reload", "Reload specific plugin configurations.", false);
            langManager = hyUtilities.getLangManager();
            this.hyUtilities = hyUtilities;
        }

        @Override
        protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
            switch(reloadableArg.get(commandContext)) {
                case "config":
                    hyUtilities.reloadConfig();
                    playerRef.sendMessage(Message.raw(langManager.get("general.hyUtilities.reload.config")));
                    break;
                case "lang":
                    langManager.reload(hyUtilities.getMainConfig().general.language);
                    playerRef.sendMessage(Message.raw(langManager.get("general.hyUtilities.reload.lang")));
                    break;
                default:
                    playerRef.sendMessage(Message.raw(langManager.get("general.hyUtilities.reload.notFound")));
                    break;
            }
        }
    }

}
