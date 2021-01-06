package de.duckdev.entjic.customplots.util;

import de.duckdev.entjic.customplots.CustomPlots;
import de.duckdev.entjic.customplots.file.Config;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class PlotContainer {

  private static final PlotContainer instance = new PlotContainer();

  private final ArrayList<Plot> plots = new ArrayList<>();


  public static PlotContainer getInstance() {
    return instance;
  }

  public void savePlots() {
    Config.getInstance().getConfig().getNode("s").setValue(plots.size());
    int i = 0;
    for (Plot p : plots) {
      p.save(i);
      i++;
    }
    Config.getInstance().save();
    CustomPlots.getInstance().getLogger().info(i + " plot(s) saved");
  }


  public void loadPlots() {
    int size = Config.getInstance().getConfig().getNode("s").getInt();
    for (int i = 0; i < size; i++) {
      String path = i + "";
      int[] a = new int[2];
      int[] b = new int[2];
      a[0] = Config.getInstance().getConfig().getNode(path, "x1").getInt();
      a[1] = Config.getInstance().getConfig().getNode(path, "z1").getInt();
      b[0] = Config.getInstance().getConfig().getNode(path, "x2").getInt();
      b[1] = Config.getInstance().getConfig().getNode(path, "z2").getInt();
      Plot plot = new Plot(a, b);
      plot.loadLocations();
      if (Config.getInstance().getConfig().getNode(path, "owner").getString() != null) {
        plot.setOwner(UUID.fromString(Objects
            .requireNonNull(Config.getInstance().getConfig().getNode(path, "owner").getString())));
        plot.setOwnerName(Objects.requireNonNull(
            Config.getInstance().getConfig().getNode(path, "owner").getComment().get()));
      }
      for (int j = 0; j < Config.getInstance().getConfig().getNode(path, "t").getInt(); j++) {
        if (!Config.getInstance().getConfig().getNode(path, "trusted", j).getComment()
            .isPresent()) {
          continue;
        }
        plot.getTrusted().put(UUID.fromString(Objects.requireNonNull(
            Config.getInstance().getConfig().getNode(path, "trusted", j).getString())),
            Config.getInstance().getConfig().getNode(path, "trusted", j).getComment().get());
      }
      plots.add(plot);
    }
    CustomPlots.getInstance().getLogger().info(plots.size() + " plot(s) loaded");
  }

  public void addPlot(Plot plot) {
    plots.add(plot);
  }

  public void removePlot(Plot plot) {
    plots.remove(plot);
  }

  private boolean playerInPlot(Location<World> location, Plot plot) {
    if (location.getExtent().getName()
        .equals(Config.getInstance().getConfig().getNode("plotwordname").getString("world"))) {
      int[] loc = new int[2];
      loc[0] = location.getBlockX();
      loc[1] = location.getBlockZ();
      for (int[] i : plot.getLocations()) {
        if (i[0] == loc[0] && i[1] == loc[1]) {
          return true;
        }
      }
    }
    return false;
  }

  public Plot getPlotWherePlayerIs(Location<World> playerloc) {
    for (Plot plot : plots) {
      if (playerInPlot(playerloc, plot)) {
        return plot;
      }
    }
    return null;
  }

  public ArrayList<Plot> getPlots() {
    return plots;
  }
}
