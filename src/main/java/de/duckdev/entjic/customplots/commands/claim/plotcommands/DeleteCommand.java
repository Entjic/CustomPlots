package de.duckdev.entjic.customplots.commands.claim.plotcommands;

import de.duckdev.entjic.customplots.file.Config;
import de.duckdev.entjic.customplots.util.Plot;
import de.duckdev.entjic.customplots.util.PlotContainer;
import java.util.HashMap;
import java.util.Objects;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class DeleteCommand implements CommandExecutor {

  private static final HashMap<Player, Plot> map = new HashMap<>();

  public static HashMap<Player, Plot> getPlayers() {
    return map;
  }

  public static void deletingConfirmed(Player player) {
    Plot plot = map.get(player);
    player.sendMessage(Text.of(TextColors.GREEN, "You deleted a plot"));
    PlotContainer.getInstance().removePlot(plot);
    map.remove(player);
  }

  public static void deletingCanceled(Player player) {
    getPlayers().remove(player);
    player.sendMessage(Text.of(TextColors.GREEN, "You canceled the deleting"));
  }

  @Override
  public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
    if (src instanceof Player) {
      Player player = (Player) src;
      if (player.hasPermission(
          Objects.requireNonNull(Config.getInstance().getConfig().getNode("permissions")
              .getNode("delete").getValue()).toString())) {
        Plot plot = PlotContainer.getInstance().getPlotWherePlayerIs(player.getLocation());
        if (plot != null) {
          if (!map.containsKey(player)) {
            map.put(player, plot);
            player.sendMessage(Text.of(TextColors.GOLD,
                "Please type /confirm to delete the plot or type /cancel to cancel"));
          } else {
            map.remove(player);
            player.sendMessage(Text.of(TextColors.GREEN, "You canceled the deleting"));
          }
        } else {
          player.sendMessage(Text.of(TextColors.RED, "You have to be on a plot to do this"));
        }
      } else {
        player.sendMessage(Text.of(TextColors.RED, "You do not have permission to do this"));
      }
    } else {
      System.out.println("This command can not be used by the console");
    }
    return CommandResult.success();
  }
}
