package de.duckdev.entjic.customplots.commands.claim;

import de.duckdev.entjic.customplots.util.Plot;
import de.duckdev.entjic.customplots.util.PlotContainer;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class TrustCommand implements CommandExecutor {

  @Override
  public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

    if (src instanceof Player) {
      Player player = (Player) src;
      Plot plot = PlotContainer.getInstance().getPlotWherePlayerIs(player.getLocation());
      if (plot != null) {
        if (plot.getOwner() != null) {
          if (plot.getOwner().equals(player.getUniqueId())) {
            if (args.hasAny("player")) {
              if (args.<Player>getOne("player").isPresent()) {
                Player target = args.<Player>getOne("player").get();
                if (!plot.getTrusted().containsKey(target.getUniqueId())) {
                  if (!target.equals(player)) {
                    plot.getTrusted().put(target.getUniqueId(), target.getName());
                    player.sendMessage(Text.of(TextColors.GREEN,
                        target.getName() + " is now trusted on this plot"));
                    target.sendMessage(Text.of(TextColors.GREEN,
                        "You are now trusted on a plot of " + player.getName()));
                    PlotContainer.getInstance().savePlots();
                  } else {
                    player.sendMessage(
                        Text.of(TextColors.RED, "You cannot trust yourself on your own plot"));
                  }
                } else {
                  player.sendMessage(
                      Text.of(TextColors.RED, "This player is already trusted on this plot"));
                }
              } else {
                player.sendMessage(Text.of(TextColors.RED, "This player seems not to be online"));
              }
            } else {
              player.sendMessage(Text.of(TextColors.RED, "Please use /trust <Player>"));
            }
          } else {
            player.sendMessage(Text.of(TextColors.RED, "You have to be on your plot to do this"));
          }
        } else {
          player.sendMessage(Text.of(TextColors.RED, "This plot is not claimed"));
        }
      } else {
        player.sendMessage(Text.of(TextColors.RED, "You have to be on your plot to do this"));
      }

    } else {
      System.out.println("This command can not be used by the console");
    }

    return CommandResult.success();
  }
}
