package me.michello.minepacks;

import at.pcgamingfreaks.Minepacks.Bukkit.API.MinepacksPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class Main extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "minepacks";
    }

    @Override
    public @NotNull String getAuthor() {
        return "michello";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    public String onPlaceholderRequest(Player p, String arg) {
        try {

            String[] args = arg.split(" ");
            System.out.println(arg + " | " + args[0]);
            Player plr = p;
            if(args.length > 2 && !args[0].equalsIgnoreCase("set")) {
                plr = Bukkit.getPlayer(args[1]);
            }
            if(plr == null) {
                return "Unknown player";
            }
            if(args[0].equalsIgnoreCase("backpack_contents")) {
                return getMinepacks().getBackpackCachedOnly(plr).getInventory().getContents().toString();
            }
            if(args[0].equalsIgnoreCase("backpack_size")) {
                return String.valueOf(getMinepacks().getBackpackCachedOnly(plr).getInventory().getSize());
            }
            if(args[0].equalsIgnoreCase("isblocked")) {
                if(args.length < 3) {
                    return "Usage: %minepacks_isblocked item amount% | %minepacks_isblocked AIR 1%";
                }
                String blocked = args[1];
                String amount = args[2];
                return getMinepacks().getItemFilter().isItemBlocked(new ItemStack(Material.getMaterial(blocked), Integer.parseInt(amount))) ? "Yes" : "No";
            }
            if(args[0].equalsIgnoreCase("get")) {
                if(args.length < 4) {
                    return "Usage: %minepacks_get config path% | %minepacks_set config BackpackTitle%";
                }
                String config = args[1];

                String path = args[2];

                File file = new File(Bukkit.getServer().getWorldContainer() + "/plugins/Minepacks/" + config + ".yml");
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                return String.valueOf(configuration.get(path));
            }
            if(args[0].equalsIgnoreCase("set")) {
                if(args.length < 4) {
                    return "Usage: %minepacks_set config path value% | %minepacks_set config BackpackTitle &bBackpack%";
                }
                String config = args[1];

                String path = args[2];
                String value = args[3];


                File file = new File(Bukkit.getServer().getWorldContainer() + "/plugins/Minepacks/" + config + ".yml");
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

                configuration.set(path, convert(value));
                try {
                    configuration.save(file);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bp reload");
                } catch (IOException e) {
                    return "Failed to save the file!";
                }
            }
            return "";
        } catch (Exception e) {
            return "Minepacks not found!";
        }
    }

    public static MinepacksPlugin getMinepacks() {
        Plugin bukkitPlugin = Bukkit.getPluginManager().getPlugin("Minepacks");
        if(!(bukkitPlugin instanceof MinepacksPlugin)) {
            return null;
        }
        return (MinepacksPlugin) bukkitPlugin;
    }

    public Object convert(String str) throws IllegalArgumentException {
        if (str.equalsIgnoreCase("true")) {
            return true;
        } else if (str.equalsIgnoreCase("false")) {
            return false;
        } else {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }

}