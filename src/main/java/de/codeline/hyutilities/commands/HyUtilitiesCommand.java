package de.codeline.hyutilities.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import de.codeline.hyutilities.HyUtilities;
import de.codeline.hyutilities.config.PluginConfig;
import de.codeline.hyutilities.lang.LangManager;
import io.netty.util.concurrent.CompleteFuture;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class HyUtilitiesCommand extends AbstractCommandCollection {

    private final LangManager langManager;
    private final PluginConfig mainConfig;
    private final HyUtilities hyUtilities;

    public HyUtilitiesCommand(HyUtilities hyUtilities) {
        super("hyutilities", "Primary command for using subcommands directly related to the HyUtilities plugin.");
        this.langManager = HyUtilities.getLangManager();
        this.mainConfig = hyUtilities.getMainConfig();
        this.hyUtilities = hyUtilities;
        this.addAliases("hy", "hyutils");
        this.addSubCommand(new ReloadCommand(hyUtilities));
    }

    public static class ReloadCommand extends AbstractAsyncCommand {

        RequiredArg<ReloadType> reloadableArg = this.withRequiredArg("reload_type", "Specify what should be reloaded",
                ArgTypes.forEnum("ReloadType", ReloadType.class));
        private final LangManager langManager;
        private final HyUtilities hyUtilities;

        public ReloadCommand(HyUtilities hyUtilities) {
            super("reload", "Reload specific plugin configurations.", false);
            langManager = HyUtilities.getLangManager();
            this.hyUtilities = hyUtilities;
        }

        @Override
        protected @NotNull CompletableFuture<Void> executeAsync(@NotNull CommandContext commandContext) {
            try {
                hyUtilities.createConfigFiles(hyUtilities.getDataDirectory());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            switch(reloadableArg.get(commandContext)) {
                case ReloadType.ALL:
                    hyUtilities.reloadConfig(commandContext);
                    langManager.reload(hyUtilities.getMainConfig().general.language);
                    commandContext.sendMessage(langManager.get("general.hyUtilities.reload.all"));
                    break;
                case ReloadType.CONFIG:
                    hyUtilities.reloadConfig(commandContext);
                    commandContext.sendMessage(langManager.get("general.hyUtilities.reload.config"));
                    break;
                case ReloadType.LANG:
                    langManager.reload(hyUtilities.getMainConfig().general.language);
                    commandContext.sendMessage(langManager.get("general.hyUtilities.reload.lang"));
                    break;
                default:
                    commandContext.sendMessage(langManager.get("general.hyUtilities.reload.notFound"));
                    break;
            }
            return CompletableFuture.completedFuture(null);
        }
    }

    private enum ReloadType {
        ALL,
        CONFIG,
        LANG
    }

}
