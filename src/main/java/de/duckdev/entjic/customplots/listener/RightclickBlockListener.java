package de.duckdev.entjic.customplots.listener;

import de.duckdev.entjic.customplots.commands.claim.plotcommands.CreateCommand;
import de.duckdev.entjic.customplots.util.Plot;
import de.duckdev.entjic.customplots.util.PlotContainer;
import java.util.Arrays;
import java.util.HashMap;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class RightclickBlockListener {

  private static final HashMap<Player, int[][]> hashMap = new HashMap<>();

  public static boolean readyToConfirm(Player player) {
    if (hashMap.containsKey(player)) {
      int[][] array = hashMap.get(player);
      int[] first = array[0];
      int[] second = array[1];

      return first != null && second != null;
    } else {
      return false;
    }
  }

  public static void plotConfirmed(Player player) {
    Plot plot = new Plot(hashMap.get(player)[0], hashMap.get(player)[1]);
    PlotContainer.getInstance().addPlot(plot);
    hashMap.remove(player);
    CreateCommand.players.remove(player);
  }

  public static void plotCanceled(Player player) {
    hashMap.remove(player);
    CreateCommand.players.remove(player);
  }

  @Listener
  public void onRightClick(InteractBlockEvent.Secondary event) {
    if (event.getSource() instanceof Player) {
      Player player = (Player) event.getSource();
      if (event.getHandType().equals(HandTypes.MAIN_HAND)) {
        if (CreateCommand.players.contains(player)) {
          if (!event.getTargetBlock().getState().getType().equals(BlockTypes.AIR)) {
            int[] loc = new int[2];
            loc[0] = event.getTargetBlock().getLocation().get().getBlockX();
            loc[1] = event.getTargetBlock().getLocation().get().getBlockZ();

            if (hashMap.get(player) == null) {
              int[][] result = new int[2][2];
              result[0] = loc;
              hashMap.put(player, result);
              player.sendMessage(
                  Text.of(TextColors.GREEN, "First corner set to " + Arrays.toString(loc)));
            } else {
              int[][] result = hashMap.get(player);
              result[1] = loc;
              hashMap.put(player, result);
              player.sendMessage(
                  Text.of(TextColors.GREEN, "Second corner set to " + Arrays.toString(loc)));
              player.sendMessage(Text.of(TextColors.GOLD,
                  "Please type /confirm to create the plot or type /cancel to cancel the creating"));

            }

          }
        }
      }
    }
  }
}
