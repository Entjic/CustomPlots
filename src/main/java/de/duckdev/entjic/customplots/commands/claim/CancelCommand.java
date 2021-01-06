package de.duckdev.entjic.customplots.commands.claim;

import de.duckdev.entjic.customplots.commands.claim.plotcommands.DeleteCommand;
import de.duckdev.entjic.customplots.listener.RightclickBlockListener;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CancelCommand implements CommandExecutor {

  @Override
  public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

    if (src instanceof Player) {
      Player player = (Player) src;
      if (RightclickBlockListener.readyToConfirm(player)) {
        player.sendMessage(Text.of(TextColors.GOLD, "You canceld a new plot!"));
        RightclickBlockListener.plotCanceled(player);
      } else if (UnclaimCommand.getMap().containsKey(player)) {
        UnclaimCommand.plotCanceled(player);
      } else if (DeleteCommand.getPlayers().containsKey(player)) {
        DeleteCommand.deletingCanceled(player);
      } else {
        player.sendMessage(Text.of(TextColors.RED, "You got nothing to cancel"));
      }

    }

    return CommandResult.success();
  }
}
