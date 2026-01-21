package de.codeline.hyutilities.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import de.codeline.hyutilities.HyUtilities;
import de.codeline.hyutilities.config.PluginConfig;
import de.codeline.hyutilities.lang.LangManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GamemodeCommand extends AbstractPlayerCommand {

    private final RequiredArg<GameMode> gamemodeArg = this.withRequiredArg("gamemode", "Gamemode to set player's gamemode to.", ArgTypes.GAME_MODE);
    private final OptionalArg<PlayerRef> playerArg = this.withOptionalArg("player", "Player whose gamemode will be changed.", ArgTypes.PLAYER_REF);
    private final LangManager langManager;
    private final PluginConfig mainConfig;

    public GamemodeCommand(HyUtilities hyUtilities) {
        super("gamemode", "Customizable command used to switch the player's gamemode.", false);
        this.langManager = HyUtilities.getLangManager();
        this.mainConfig = hyUtilities.getMainConfig();
        this.addAliases("gm");
    }

    @Override
    protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
        GameMode gamemode = gamemodeArg.get(commandContext);
        PlayerRef playerReference;
        if (playerArg.get(commandContext) == null) {
            playerReference = playerRef;
            playerReference.sendMessage(Message.raw(langManager.get("general.gamemodeChangedSelf")
                    .formatted(gamemode.toString())));
        } else {
            playerReference = playerArg.get(commandContext);
            playerReference.sendMessage(Message.raw(langManager.get("general.gamemodeChangedOther")
                    .formatted(playerReference.getUsername(), gamemode)));
        }
        Player.setGameMode(Objects.requireNonNull(playerReference.getReference()), gamemode, store);
    }

}
