package xyz.msws.simpletweaks;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EnderChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(SimpleTweaks.PREFIX + "Only players can use this command.");
            return true;
        }
        if (!allowEnderChest(sender))
            return true;
        player.openInventory(player.getEnderChest());
        return true;
    }

    private boolean allowEnderChest(CommandSender sender) {
        return allowEnderChest(sender, true, true);
    }

    /**
     * Checks if the sender can open their ender chest
     * Requires they have an ender chest in their inventory
     * and a means to get it back in their inventory
     * (either an ender eye or silk touch pickaxe)
     *
     * @param sender
     * @param removeEye
     * @param print
     * @return
     */
    private boolean allowEnderChest(CommandSender sender, boolean removeEye, boolean print) {
        if (!(sender instanceof Player player)) {
            if (print) sender.sendMessage(SimpleTweaks.PREFIX + "Only players can use this command.");
            return false;
        }

        if (!player.getInventory().contains(Material.ENDER_CHEST)) {
            if (print) player.sendMessage(SimpleTweaks.PREFIX + "You need an ender chest to use this command.");
            return false;
        }

        if (hasSilkTouch(player.getInventory())) return true;

        if (!player.getInventory().contains(Material.ENDER_EYE)) {
            if (print)
                player.sendMessage(SimpleTweaks.PREFIX + "You need an eye of ender or silk touch pickaxe to use this command.");
            return false;
        }
        if (removeEye) {
            player.getInventory().removeItem(new ItemStack(Material.ENDER_EYE, 1));
            if (print)
                player.sendMessage(SimpleTweaks.PREFIX + "You used an eye of ender to open your ender chest.");
        }

        return true;
    }

    private boolean hasSilkTouch(Inventory inv) {
        boolean hasSilkTouch = false;
        for (ItemStack item : inv) {
            if (item == null || item.getType().isAir()) continue;
            if (!Tag.ITEMS_PICKAXES.isTagged(item.getType())) continue;
            if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) continue;
            return true;
        }
        return false;
    }
}
