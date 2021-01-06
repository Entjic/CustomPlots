package de.duckdev.entjic.customplots.commands.claim.plotcommands;

import de.duckdev.entjic.customplots.file.Config;
import java.util.ArrayList;
import java.util.Objects;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CreateCommand implements CommandExecutor {

  public static ArrayList<Player> players = new ArrayList<>();

  @Override
  public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
    if (src instanceof Player) {
      Player player = (Player) src;
      if (player.hasPermission(
          Objects.requireNonNull(Config.getInstance().getConfig().getNode("permissions")
              .getNode("create").getValue()).toString())) {
        if (!players.contains(player)) {
          players.add(player);
          player.sendMessage(Text.of(TextColors.GREEN, "Please click the 2 corners of the plot"));
        } else {
          players.remove(player);
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
