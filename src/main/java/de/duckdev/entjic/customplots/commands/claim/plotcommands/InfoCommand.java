package de.duckdev.entjic.customplots.commands.claim.plotcommands;

import de.duckdev.entjic.customplots.util.Plot;
import de.duckdev.entjic.customplots.util.PlotContainer;
import java.util.Optional;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class InfoCommand implements CommandExecutor {

  @Override
  public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

    if (src instanceof Player) {
      Player player = (Player) src;
      Plot plot = PlotContainer.getInstance().getPlotWherePlayerIs(player.getLocation());
      if (plot != null) {
        sendMessage(player, "-----");
        if (plot.getOwner() != null) {
          Optional<Player> optional = Sponge.getServer().getPlayer(plot.getOwner());
          if (optional.isPresent()) {
            sendMessage(player, "Owner: " + optional.get().getName());
          } else {
            sendMessage(player, "Owner: " + plot.getOwnerName());
          }
        } else {
          sendMessage(player, "This plot is not owned");
          sendMessage(player, "Price: " + plot.getPrice());
        }
        for (String string : plot.getTrusted().values()) {
          sendMessage(player, "Trusted players: " + string);
        }
        sendMessage(player, "-----");


      } else {
        player.sendMessage(Text.of(TextColors.RED, "You have to be on a plot to do this"));
      }

    } else {
      System.out.println("This command can not be used by the console");
    }

    return CommandResult.success();
  }


  private void sendMessage(Player player, String text) {
    player.sendMessage(Text.of(TextColors.GOLD, text));
  }
}
