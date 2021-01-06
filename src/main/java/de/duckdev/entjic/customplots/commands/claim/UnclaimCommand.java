package de.duckdev.entjic.customplots.commands.claim;

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

public class UnclaimCommand implements CommandExecutor {

  private static HashMap<Player, Plot> players = new HashMap<>();

  public static HashMap<Player, Plot> getMap() {
    return players;
  }

  public static void plotConfirmed(Player player) {
    if (players.get(player).getOwnerName().equals(player.getName())) {
      player.sendMessage(Text.of(TextColors.GREEN, "You are no longer the owner of this plot"));
    } else {
      player.sendMessage(Text.of(TextColors.GREEN,
          players.get(player).getOwnerName() + " is no longer the owner of this plot"));
    }
    players.get(player).setOwner(null);
    players.get(player).setOwnerName(null);
    players.remove(player);
  }

  public static void plotCanceled(Player player) {
    Plot plot = players.get(player);
    if (player.getName().equals(plot.getOwnerName())) {
      player.sendMessage(Text.of(TextColors.GREEN, "You canceled the unclaiming of your plot"));
    } else {
      player.sendMessage(Text.of(TextColors.GOLD,
          "You canceled the unclaiming of " + plot.getOwnerName() + "s plot"));
    }
    players.remove(player);
  }

  @Override
  public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

    if (src instanceof Player) {
      Player player = (Player) src;
      Plot plot = PlotContainer.getInstance().getPlotWherePlayerIs(player.getLocation());
      if (plot != null) {
        if (plot.getOwner() != null) {
          if (plot.getOwner().equals(player.getUniqueId())) {
            if (!players.containsKey(player)) {
              players.put(player, plot);
              player.sendMessage(Text.of(TextColors.GOLD,
                  "Please type /confirm to unclaim your plot or type /cancel to cancel"));
            } else {
              players.remove(player);
              player.sendMessage(
                  Text.of(TextColors.GOLD, "You canceled the unclaiming of your plot"));
            }
          } else {
            if (player.hasPermission(
                Objects.requireNonNull(Config.getInstance().getConfig().getNode("permissions")
                    .getNode("unclaim").getValue()).toString())) {
              if (!players.containsKey(player)) {
                players.put(player, plot);
                player.sendMessage(Text.of(TextColors.GOLD,
                    "Please type /confirm to unclaim " + plot.getOwnerName()
                        + "s plot or type /cancel to cancel"));
              } else {
                players.remove(player);
                player.sendMessage(Text.of(TextColors.GOLD,
                    "You canceled the unclaiming of " + plot.getOwnerName() + "s plot"));
              }
            } else {
              player.sendMessage(Text.of(TextColors.RED, "You are not the owner of this plot"));
            }
          }
        } else {
          player.sendMessage(Text.of(TextColors.RED, "This plot is not claimed"));
        }
      } else {
        player.sendMessage(Text.of(TextColors.RED, "You have to be on your plot to do this"));
      }


    }

    return CommandResult.success();
  }
}
