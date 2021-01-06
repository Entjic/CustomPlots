package de.duckdev.entjic.customplots.commands.claim;

import de.duckdev.entjic.customplots.util.Plot;
import de.duckdev.entjic.customplots.util.PlotContainer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ClaimCommand implements CommandExecutor {

  @Override
  public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

    if (src instanceof Player) {
      Player player = (Player) src;
      Plot plot = PlotContainer.getInstance().getPlotWherePlayerIs(player.getLocation());
      if (plot != null) {
        if (plot.getOwner() == null) {
          plot.setOwner(player.getUniqueId());
          plot.setOwnerName(player.getName());
          player.sendMessage(Text.of(TextColors.GREEN, "You are the new owner of this plot"));
        } else {
          player.sendMessage(Text.of(TextColors.RED,
              "This plot is already owned by " + Sponge.getServer().getPlayer(plot.getOwner()).get()
                  .getName()));
        }
      } else {
        player.sendMessage(Text.of(TextColors.RED, "You have to be on a plot to do this"));
      }

    } else {
      System.out.println("This command can not be used by the console");
    }

    return CommandResult.success();
  }
}
