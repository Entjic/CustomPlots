package de.duckdev.entjic.customplots.listener;

import de.duckdev.entjic.customplots.file.Config;
import de.duckdev.entjic.customplots.util.Plot;
import de.duckdev.entjic.customplots.util.PlotContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

public class PlayerMoveListener {

  @Listener
  public void onPlayerMove(MoveEntityEvent entityEvent) {
    if (entityEvent.getSource() instanceof Player) {
      Player player = (Player) entityEvent.getSource();

      World t = entityEvent.getToTransform().getExtent();

      if (t.getName()
          .equals(Config.getInstance().getConfig().getNode("plotwordname").getString("world"))) {
        Plot from = PlotContainer.getInstance()
            .getPlotWherePlayerIs(entityEvent.getFromTransform().getLocation());
        Plot to = PlotContainer.getInstance()
            .getPlotWherePlayerIs(entityEvent.getToTransform().getLocation());
        if (from != null) {
          // Aus Plot
          if (to != null) {
            if (!from.equals(to)) {
              if (to.getOwnerName() != null) {
                player.sendMessage(
                    Text.of(TextColors.GOLD, "You entered the plot of " + to.getOwnerName()));
              } else {
                player.sendMessage(Text.of(TextColors.GOLD, "You entered an unclaimed plot"));
              }
            }
          } else if (from.getOwnerName() != null) {
            player.sendMessage(
                Text.of(TextColors.GOLD, "You left the plot of " + from.getOwnerName()));
          } else {
            player.sendMessage(Text.of(TextColors.GOLD, "You left an unclaimed plot"));
          }
        } else if (to != null) {
          if (to.getOwnerName() != null) {
            player.sendMessage(
                Text.of(TextColors.GOLD, "You entered the plot of " + to.getOwnerName()));
          } else {
            player.sendMessage(Text.of(TextColors.GOLD, "You entered an unclaimed plot"));
          }
        }
      }
    }
  }
}
