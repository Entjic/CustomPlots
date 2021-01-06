package de.duckdev.entjic.customplots.commands.plot;

import de.duckdev.entjic.customplots.file.Config;
import de.duckdev.entjic.customplots.util.PlotContainer;
import java.util.Objects;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ReloadCommand implements CommandExecutor {

  @Override
  public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

    if (src.hasPermission(
        Objects.requireNonNull(Config.getInstance().getConfig().getNode("permissions")
            .getNode("reload").getValue()).toString())) {
      PlotContainer.getInstance().savePlots();
      PlotContainer.getInstance().getPlots().clear();
      Config.getInstance().reload();
      PlotContainer.getInstance().loadPlots();
      src.sendMessage(Text.of(TextColors.GOLD, "Custom Plots reloaded"));
    } else {
      src.sendMessage(Text.of(TextColors.RED, "You do not have permission to do this"));

    }

    return CommandResult.success();
  }
}
