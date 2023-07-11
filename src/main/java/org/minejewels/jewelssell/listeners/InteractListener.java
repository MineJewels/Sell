package org.minejewels.jewelssell.listeners;

import com.google.common.util.concurrent.AtomicDouble;
import net.abyssdev.abysslib.listener.AbyssListener;
import net.abyssdev.abysslib.nbt.NBTUtils;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import net.abyssdev.me.lucko.helper.Events;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelsrealms.events.RealmInteractEvent;
import org.minejewels.jewelssell.JewelsSell;
import org.minejewels.jewelssell.events.PlayerSellEvent;
import org.minejewels.jewelssell.events.SellWandBreakEvent;
import org.minejewels.jewelssell.events.SellWandSellEvent;
import org.minejewels.jewelssell.sellwand.Sellwand;

public class InteractListener extends AbyssListener<JewelsSell> {
    public InteractListener(final JewelsSell plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInteract(final RealmInteractEvent event) {

        final Player player = event.getPlayer();

        if (player.getItemInHand().getType() == Material.AIR) return;

        if (event.getEvent().getHand() != EquipmentSlot.HAND) return;
        if (event.getEvent().getClickedBlock() == null) return;
        if (event.getEvent().getClickedBlock().getType() != Material.CHEST) return;

        final ItemStack item = player.getItemInHand();

        if (!NBTUtils.get().contains(item, "SELLWAND-MULTIPLIER")) return;

        event.setCancelled(true);

        final double multiplier = NBTUtils.get().getDouble(item, "SELLWAND-MULTIPLIER");
        final long uses = NBTUtils.get().getLong(item, "SELLWAND-USES");

        final Chest chest = (Chest) event.getEvent().getClickedBlock().getState();

        final AtomicDouble worth = new AtomicDouble(0);

        for (final ItemStack content : chest.getInventory().getContents()) {
            if (!NBTUtils.get().contains(content, "GENERATOR-LOOT")) continue;
            final String tag = NBTUtils.get().getString(content, "GENERATOR-LOOT");
            if (!this.plugin.getItemRegistry().containsKey(tag)) continue;

            final double price = this.plugin.getItemRegistry().get(tag).get();

            worth.getAndAdd(price * content.getAmount());

            chest.getInventory().removeItem(content);
        }

        if (worth.get() == 0)  {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-sellable-items");
            return;
        }

        worth.set(worth.get() * multiplier);

        if (uses != -1) {
            final Sellwand sellwand = new Sellwand(plugin, multiplier, uses -1);

            sellwand.setWand(player);
        }

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%multiplier%", Utils.format(multiplier))
                .addPlaceholder("%value%", Utils.format(worth.get()));

        this.plugin.getEconomy().addBalance(player, worth.get());

        plugin.getMessageCache().sendMessage(player, "messages.sold-contents-sellwand", replacer);

        final SellWandSellEvent sellEvent = new SellWandSellEvent(player, worth.get());

        Events.call(sellEvent);

        if (uses - 1 == 0) {
            Utils.removeItemsFromHand(player, 1, false);
            this.plugin.getMessageCache().sendMessage(player, "messages.sellwand-broken");

            final SellWandBreakEvent breakEvent = new SellWandBreakEvent(player);

            Events.call(breakEvent);
        }
    }
}
