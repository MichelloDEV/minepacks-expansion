package me.michello.minepacks;

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
import java.util.HashMap;

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

    @Override
    public @NotNull String getRequiredPlugin() {
        return "Minepacks";
    }

    public YamlConfiguration cache;

    public String onPlaceholderRequest(Player p, String arg) {
        try {

            String[] args = arg.split(" ");
            Player plr = p;
            if(args.length > 2 && !args[0].equalsIgnoreCase("get")) {
                plr = Bukkit.getPlayer(args[1]);
            }
            if(plr == null) {
                return "Unknown player";
            }
            if(args[0].equalsIgnoreCase("backpack_contents")) {
                return MinepacksWrapper.getMinepacks().getBackpackCachedOnly(plr).getInventory().getContents().toString();
            }
            if(args[0].equalsIgnoreCase("backpack_size")) {
                return String.valueOf(MinepacksWrapper.getMinepacks().getBackpackCachedOnly(plr).getInventory().getSize());
            }
            if(args[0].equalsIgnoreCase("isblocked")) {
                if(args.length < 3) {
                    return "Usage: %minepacks_isblocked item amount% | %minepacks_isblocked AIR 1%";
                }
                String blocked = args[1];
                String amount = args[2];
                return MinepacksWrapper.getMinepacks().getItemFilter().isItemBlocked(new ItemStack(Material.getMaterial(blocked), Integer.parseInt(amount))) ? "Yes" : "No";
            }
            if(args[0].equalsIgnoreCase("get")) {
                if(args.length < 3) {
                    return "Usage: %minepacks_get config path% | %minepacks_set config BackpackTitle%";
                }
                String config = args[1];

                String path = args[2];

                if(cache == null) {
                    File file = new File(Bukkit.getServer().getWorldContainer() + "/plugins/Minepacks/" + config + ".yml");
                    cache = YamlConfiguration.loadConfiguration(file);
                }
                Object value = cache.get(path);
                return String.valueOf(value);
            }
            return "";
        } catch (NoClassDefFoundError e) {
            return "Minepacks not found!";
        }
    }


}
