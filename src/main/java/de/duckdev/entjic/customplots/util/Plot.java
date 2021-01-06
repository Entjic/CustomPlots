package de.duckdev.entjic.customplots.util;

import de.duckdev.entjic.customplots.file.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class Plot {

  int x1, z1, x2, z2, price;
  UUID owner;
  ArrayList<int[]> locations;
  String ownerName;
  HashMap<UUID, String> trusted = new HashMap<>();

  public Plot(Location<World> loc0, Location<World> loc1) {
    x1 = Math.min(loc0.getBlockX(), loc1.getBlockX());
    z1 = Math.min(loc0.getBlockZ(), loc1.getBlockZ());
    x2 = Math.max(loc0.getBlockX(), loc1.getBlockX());
    z2 = Math.max(loc0.getBlockZ(), loc1.getBlockZ());
    locations = getClaimedBlocks();
    setPrice(0);
  }


  public Plot(int[] a, int[] b) {
    x1 = Math.min(a[0], b[0]);
    z1 = Math.min(a[1], b[1]);
    x2 = Math.max(a[0], b[0]);
    z2 = Math.max(a[1], b[1]);
    locations = getClaimedBlocks();
    setPrice(0);
  }

  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

  public UUID getOwner() {
    return owner;
  }

  public void setOwner(UUID owner) {
    this.owner = owner;
  }

  public HashMap<UUID, String> getTrusted() {
    return trusted;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }


  public void untrust(UUID uuid) {
    trusted.remove(uuid);
  }


  private ArrayList<int[]> getClaimedBlocks() {
    ArrayList<int[]> locs = new ArrayList<>();
    for (int a = x1; a <= x2; a++) {
      for (int b = z1; b <= z2; b++) {
        int[] c = new int[2];
        c[0] = a;
        c[1] = b;
        locs.add(c);
      }
    }
    return locs;
  }

  public void loadLocations() {
    locations = getClaimedBlocks();
  }

  public ArrayList<int[]> getLocations() {
    return locations;
  }

  public void save(Object path) {
    if (owner != null) {
      Config.getInstance().getConfig().getNode(path, "owner").setComment(ownerName)
          .setValue(owner.toString());
    }
    Config.getInstance().getConfig().getNode(path, "x1").setValue(x1);
    Config.getInstance().getConfig().getNode(path, "z1").setValue(z1);
    Config.getInstance().getConfig().getNode(path, "x2").setValue(x2);
    Config.getInstance().getConfig().getNode(path, "z2").setValue(z2);
    Config.getInstance().getConfig().getNode(path, "t").setValue(trusted.size());
    int i = 0;
    for (UUID uuid : trusted.keySet()) {
      String name = trusted.get(uuid).trim();
      Config.getInstance().getConfig().getNode(path, "trusted", i).setComment(name)
          .setValue(uuid.toString());
      i++;
    }
  }
}
