package de.duckdev.entjic.customplots.commands.plot;

import de.duckdev.entjic.customplots.file.Config;
import de.duckdev.entjic.customplots.util.Plot;
import de.duckdev.entjic.customplots.util.PlotContainer;
import java.util.Objects;
import java.util.Optional;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SetOwnerCommand implements CommandExecutor {

  @Override
  public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
    if (src instanceof Player) {
      Player player = (Player) src;
      if (player.hasPermission(
          Objects.requireNonNull(Config.getInstance().getConfig().getNode("permissions")
              .getNode("setowner").getValue()).toString())) {
        Plot plot = PlotContainer.getInstance().getPlotWherePlayerIs(player.getLocation());
        Optional<Player> optional = args.<Player>getOne("player");
        if (optional.isPresent()) {
          Player target = optional.get();
          plot.setOwner(target.getUniqueId());
          plot.setOwnerName(target.getName());
          player.sendMessage(
              Text.of(TextColors.GREEN, "The new owner of this plot is " + target.getName()));
        } else {
          player.sendMessage(Text.of(TextColors.RED, "This player seems not to be online"));
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
