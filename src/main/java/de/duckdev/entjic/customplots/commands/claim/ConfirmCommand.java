package de.duckdev.entjic.customplots.commands.claim;

import de.duckdev.entjic.customplots.commands.plot.DeleteCommand;
import de.duckdev.entjic.customplots.listener.RightclickBlockListener;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ConfirmCommand implements CommandExecutor {

  @Override
  public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

    if (src instanceof Player) {
      Player player = (Player) src;
      if (RightclickBlockListener.readyToConfirm(player)) {
        player.sendMessage(Text.of(TextColors.GOLD, "You created a new plot!"));
        RightclickBlockListener.plotConfirmed(player);
      } else if (UnclaimCommand.getMap().containsKey(player)) {
        UnclaimCommand.plotConfirmed(player);
      } else if (DeleteCommand.getPlayers().containsKey(player)) {
        DeleteCommand.deletingConfirmed(player);
      } else {
        player.sendMessage(Text.of(TextColors.RED, "You got nothing to confirm"));
      }


    }
    return CommandResult.success();
  }
}
