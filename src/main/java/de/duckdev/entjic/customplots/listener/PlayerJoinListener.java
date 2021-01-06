package de.duckdev.entjic.customplots.listener;

import de.duckdev.entjic.customplots.util.Plot;
import de.duckdev.entjic.customplots.util.PlotContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class PlayerJoinListener {

  @Listener
  public void onPlayerJoin(ClientConnectionEvent.Join event) {
    Player player = event.getTargetEntity();
    for (Plot plot : PlotContainer.getInstance().getPlots()) {
      if (plot.getTrusted().containsKey(player.getUniqueId())) {
        if (!plot.getTrusted().containsValue(player.getName())) {
          plot.getTrusted().put(player.getUniqueId(), player.getName());
        }
      }
    }
  }

}
