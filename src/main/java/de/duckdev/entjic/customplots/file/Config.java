package de.duckdev.entjic.customplots.file;

import java.io.File;
import java.io.IOException;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class Config {

  private static final Config instance = new Config();

  private ConfigurationLoader<CommentedConfigurationNode> loader;
  private CommentedConfigurationNode config;

  public static Config getInstance() {
    return instance;
  }


  public void setUp(File configFile, ConfigurationLoader<CommentedConfigurationNode> configLoader) {
    this.loader = configLoader;

    if (!configFile.exists()) {
      try {
        load();
        configFile.createNewFile();
        save();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      load();
    }
  }

  public void setPermissions() {
    Config.getInstance().getConfig().getNode("permissions")
        .getNode("create").setValue("cp.create");
    Config.getInstance().getConfig().getNode("permissions")
        .getNode("delete").setValue("cp.delete");
    Config.getInstance().getConfig().getNode("permissions")
        .getNode("setowner").setValue("cp.setowner");
    Config.getInstance().getConfig().getNode("permissions")
        .getNode("setprice").setValue("cp.setprice");
    Config.getInstance().getConfig().getNode("permissions")
        .getNode("break").setValue("cp.break");
    Config.getInstance().getConfig().getNode("permissions")
        .getNode("place").setValue("cp.place");
    Config.getInstance().getConfig().getNode("permissions")
        .getNode("open").setValue("cp.open");
    Config.getInstance().getConfig().getNode("permissions")
        .getNode("reload").setValue("cp.reload");
    Config.getInstance().getConfig().getNode("permissions")
        .getNode("unclaim").setValue("cp.unclaim");
  }

  public void load() {
    try {
      config = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void save() {
    try {
      loader.save(config);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public CommentedConfigurationNode getConfig() {
    return config;
  }

  public void reload() {
    save();
    load();
  }

}
