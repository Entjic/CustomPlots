package de.duckdev.entjic.customplots;

import com.google.inject.Inject;
import de.duckdev.entjic.customplots.commands.claim.CancelCommand;
import de.duckdev.entjic.customplots.commands.claim.ClaimCommand;
import de.duckdev.entjic.customplots.commands.claim.ConfirmCommand;
import de.duckdev.entjic.customplots.commands.claim.TrustCommand;
import de.duckdev.entjic.customplots.commands.claim.UnclaimCommand;
import de.duckdev.entjic.customplots.commands.claim.UntrustCommand;
import de.duckdev.entjic.customplots.commands.claim.plotcommands.CreateCommand;
import de.duckdev.entjic.customplots.commands.claim.plotcommands.DeleteCommand;
import de.duckdev.entjic.customplots.commands.claim.plotcommands.InfoCommand;
import de.duckdev.entjic.customplots.commands.claim.plotcommands.ReloadCommand;
import de.duckdev.entjic.customplots.commands.claim.plotcommands.SetOwnerCommand;
import de.duckdev.entjic.customplots.commands.claim.plotcommands.SetPriceCommand;
import de.duckdev.entjic.customplots.file.Config;
import de.duckdev.entjic.customplots.listener.PlayerJoinListener;
import de.duckdev.entjic.customplots.listener.PlayerMoveListener;
import de.duckdev.entjic.customplots.listener.PreventListeners;
import de.duckdev.entjic.customplots.listener.RightclickBlockListener;
import de.duckdev.entjic.customplots.util.PlotContainer;
import java.io.File;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

@Plugin(
    id = "customplots",
    name = "CustomPlots",
    version = "1.3",
    description = "Custom Plots Plugin",
    authors = {
        "Entjic"
    }
)
public class CustomPlots {

  private static CustomPlots instance;
  @Inject
  @DefaultConfig(sharedRoot = true)
  ConfigurationLoader<CommentedConfigurationNode> configManager;
  @Inject
  @DefaultConfig(sharedRoot = true)
  private File configFile;
  @Inject
  private Logger logger;

  public static CustomPlots getInstance() {
    return instance;
  }

  public Logger getLogger() {
    return logger;
  }

  @Listener
  public void onServerStart(GameStartedServerEvent event) {
    instance = this;
    Config.getInstance().setUp(configFile, configManager);
    if (Config.getInstance().getConfig().getNode("permission").getNode("create").getString()
        == null) {
      Config.getInstance().setPermissions();
    }
    if (Config.getInstance().getConfig().getNode("plotworldname").getString() == null) {
      Config.getInstance().getConfig().getNode("plotworldname").setValue("world");
      Config.getInstance().save();
    }
    PlotContainer.getInstance().loadPlots();
    register();
  }

  @Listener
  public void onServerShutDown(GameStoppingServerEvent event) {
    PlotContainer.getInstance().savePlots();
  }


  private void register() {
    // Registering plot cmd
    Sponge.getCommandManager()
        .register(this, CommandSpec.builder()
                .child(CommandSpec.builder().description(Text.of("Create a new plot"))
                    .executor(new CreateCommand()).build(), "create")
                .child(CommandSpec.builder().description(Text.of("Delete the plot you're on"))
                    .executor(new DeleteCommand()).build(), "delete")
                .child(CommandSpec.builder().description(Text.of("Set the owner of the plot you're on"))
                    .arguments(GenericArguments.player(Text.of("player")))
                    .executor(new SetOwnerCommand()).build(), "setowner")
                .child(CommandSpec.builder().description(Text.of("Set the price of the plot you're on"))
                    .arguments(GenericArguments.player(Text.of("player")))
                    .executor(new SetPriceCommand()).build(), "setprice")
                .child(CommandSpec.builder().arguments(GenericArguments.player(Text.of("player")))
                    .executor(new TrustCommand()).description(Text.of("Trust a player on your plot"))
                    .build(), "trust")
                .child(CommandSpec.builder().arguments(GenericArguments.player(Text.of("player")))
                    .executor(new UntrustCommand())
                    .description(Text.of("Untrust a player from your plot")).build(), "untrust")
                .child(CommandSpec.builder()
                    .executor(new ClaimCommand()).description(Text.of("Claim a plot")).build(), "claim")
                .child(CommandSpec.builder()
                        .executor(new UnclaimCommand()).description(Text.of("Unclaim a plot")).build(),
                    "unclaim")
                .child(CommandSpec.builder()
                    .executor(new InfoCommand())
                    .description(Text.of("Shows info about the plot your on")).build(), "info")
                .child(CommandSpec.builder().executor(new InfoCommand())
                    .description(Text.of("Shows info about the plot your on")).build(), "info", "i")
                .child(CommandSpec.builder().executor(new ReloadCommand())
                    .description(Text.of("Reloads CustomPlots")).build(), "reload", "rl").build(),
            "plot", "p");
    // Registering clear cmd
//        Sponge.getCommandManager().register(this, CommandSpec.builder()
//                .executor(new ClearCommand()).description(Text.of("Clear your plot")).build(), "clear");
    // Registering confirm cmd
    Sponge.getCommandManager().register(this, CommandSpec.builder()
        .executor(new ConfirmCommand()).description(Text.of("Confirm stuff")).build(), "confirm");
    Sponge.getCommandManager().register(this,
        CommandSpec.builder().executor(new CancelCommand()).description(Text.of("Cancel stuff"))
            .build(), "cancel");

    // Registering Events
    Sponge.getEventManager().registerListeners(this, new RightclickBlockListener());
    Sponge.getEventManager().registerListeners(this, new PreventListeners());
    Sponge.getEventManager().registerListeners(this, new PlayerJoinListener());
    Sponge.getEventManager().registerListeners(this, new PlayerMoveListener());
  }
}
