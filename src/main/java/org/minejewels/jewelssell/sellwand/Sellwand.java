package org.minejewels.jewelssell.sellwand;

import lombok.Data;
import net.abyssdev.abysslib.nbt.NBTUtils;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelssell.JewelsSell;

@Data
public class Sellwand {

    private final JewelsSell plugin;
    private final double multiplier;
    private final long uses;

    public void giveWand(final Player player) {

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%multiplier%", Utils.format(this.multiplier))
                .addPlaceholder("%uses%", this.formatUses());

        ItemStack item = this.plugin.getSettingsConfig().getItemBuilder("sellwand")
                .parse(replacer);

        item = NBTUtils.get().setLong(item, "SELLWAND-USES", this.uses);
        item = NBTUtils.get().setDouble(item, "SELLWAND-MULTIPLIER", this.multiplier);

        player.getInventory().addItem(item);

        this.plugin.getMessageCache().sendMessage(player, "messages.sellwand-given", replacer);
    }

    private String formatUses() {
        if (this.uses == -1) return "Infinite";
        return Utils.format(this.uses);
    }
}
