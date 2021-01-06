package de.duckdev.entjic.customplots.listener;

import de.duckdev.entjic.customplots.file.Config;
import de.duckdev.entjic.customplots.util.Plot;
import de.duckdev.entjic.customplots.util.PlotContainer;
import java.util.List;
import java.util.Objects;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.hanging.ItemFrame;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PreventListeners {

  @Listener
  public void onBlockBreak(ChangeBlockEvent.Break event) {

    if (event.getSource() instanceof Player) {
      Player player = (Player) event.getSource();
      if (!allowAction(event.getTransactions(), player, "break")) {
        sendError(player);
        event.setCancelled(true);
      }
    }
  }


  @Listener
  public void onBlockBreak(ChangeBlockEvent.Place event) {
    if (event.getSource() instanceof Player) {
      Player player = (Player) event.getSource();
      if (!allowAction(event.getTransactions(), player, "place")) {
        sendError(player);
        event.setCancelled(true);
      }
    }
  }

  @Listener
  public void onBlockInteract(InteractBlockEvent event) {
    if (event.getSource() instanceof Player) {
      if (event.getTargetBlock().getLocation().isPresent()) {
        Player player = (Player) event.getSource();
        Plot plot = PlotContainer.getInstance()
            .getPlotWherePlayerIs(event.getTargetBlock().getLocation().get());
        if (plot != null) {
          if (!plot.getTrusted().containsKey(player.getUniqueId()) &&
              !plot.getOwner().equals(player.getUniqueId()) &&
              !player.hasPermission(Objects.requireNonNull(
                  Config.getInstance().getConfig().getNode("permissions").getNode("open")
                      .getString()))) {
            event.setCancelled(true);
          }
//                } else {
//                    if (! player.hasPermission(Objects.requireNonNull(Config.getInstance().getConfig().getNode("permissions").getNode("open").getString()))) {
//                        event.setCancelled(true);
//                    }
//                }
        }
      }
    }
  }

  // FIXME: 17.04.2020 Kein Plan ob das funktioniert weil nicht getestet, grüße an Paul in der Zukunft :)


  @Listener
  public void onEntityInteract(InteractEntityEvent.Primary event) {
    if (event.getTargetEntity() instanceof ItemFrame) {
      doStuff(event);
    }
  }

  private void doStuff(InteractEntityEvent.Primary event) {
    if (event.getSource() instanceof Player) {
      Player player = (Player) event.getSource();
      Plot plot = PlotContainer.getInstance()
          .getPlotWherePlayerIs(event.getTargetEntity().getLocation());
      if (plot != null) {
        if (!plot.getTrusted().containsKey(player.getUniqueId()) &&
            !plot.getOwner().equals(player.getUniqueId()) &&
            !player.hasPermission(Objects.requireNonNull(
                Config.getInstance().getConfig().getNode("permissions").getNode("open")
                    .getString()))) {
          event.setCancelled(true);
        }
      } else {
        if (!player.hasPermission(Objects.requireNonNull(
            Config.getInstance().getConfig().getNode("permissions").getNode("open").getString()))) {
          event.setCancelled(true);
        }
      }
    }
  }


  @Listener
  public void onInventoryOpen(InteractInventoryEvent.Open event) {
    if (event.getSource() instanceof Player) {
      Player player = (Player) event.getSource();
      if (player.getWorld().getName()
          .equals(Config.getInstance().getConfig().getNode("plotwordname").getString("world"))) {
        Plot plot = PlotContainer.getInstance().getPlotWherePlayerIs(
            event.getContext().get(EventContextKeys.BLOCK_HIT).get().getLocation().get());
        if (!player.hasPermission(Objects.requireNonNull(
            Config.getInstance().getConfig().getNode("permissions").getNode("open").getString()))) {
          if (plot != null) {
            if (plot.getOwner() != null) {
              if (!plot.getOwner().equals(player.getUniqueId())) {
                if (plot.getTrusted() != null) {
                  if (plot.getTrusted().containsKey(player.getUniqueId())) {

                  } else {
                    sendError(player);
                    event.setCancelled(true);
                  }
                } else {
                  sendError(player);
                  event.setCancelled(true);
                }
              }
            } else {
              sendError(player);
              event.setCancelled(true);
            }
          } else {
            sendError(player);
            event.setCancelled(true);
          }
        }
      }
    }
  }

  private boolean allowAction(List<Transaction<BlockSnapshot>> list, Player player,
      String permission) {
    if (player.getWorld().getName()
        .equals(Config.getInstance().getConfig().getNode("plotwordname").getString("world"))) {
      if (!player.hasPermission(
          Objects.requireNonNull(Config.getInstance().getConfig().getNode("permissions")
              .getNode(permission).getString()))) {
        for (Transaction<BlockSnapshot> t : list) {
          Plot plot = PlotContainer.getInstance()
              .getPlotWherePlayerIs(t.getFinal().getLocation().get());
          if (plot != null) {
            if (plot.getOwner() != null) {
              if (!plot.getOwner().equals(player.getUniqueId())) {
                if (plot.getTrusted() != null) {
                  if (!plot.getTrusted().containsKey(player.getUniqueId())) {
                    return false;
                  }
                } else {
                  return false;
                }
              }
            } else {
              return false;
            }
          } else {
            return false;
          }
        }
      }
    }
    return true;
  }

  private void sendError(Player player) {
    player.sendMessage(Text.of(TextColors.RED, "You dont have the permission to do this"));
  }

}
