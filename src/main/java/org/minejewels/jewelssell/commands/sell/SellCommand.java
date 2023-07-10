package org.minejewels.jewelssell.commands.sell;

import com.google.common.util.concurrent.AtomicDouble;
import net.abyssdev.abysslib.command.AbyssCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.nbt.NBTUtils;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.eclipse.collections.api.factory.Lists;
import org.minejewels.jewelssell.JewelsSell;

public class SellCommand extends AbyssCommand<JewelsSell, Player> {

    public SellCommand(final JewelsSell plugin) {
        super(plugin, "sell", Player.class);

        this.setAliases(Lists.mutable.of("sell", "sellhand", "sellall"));
    }

    @Override
    public void execute(CommandContext<Player> context) {

        final Player player = context.getSender();

        final AtomicDouble worth = new AtomicDouble(0);

        for (final ItemStack content : player.getInventory()) {
            if (!NBTUtils.get().contains(content, "GENERATOR-LOOT")) continue;
            final String tag = NBTUtils.get().getString(content, "GENERATOR-LOOT");
            if (!this.plugin.getItemRegistry().containsKey(tag)) continue;

            final double price = this.plugin.getItemRegistry().get(tag).get();

            worth.getAndAdd(price * content.getAmount());

            player.getInventory().removeItem(content);
        }

        if (worth.get() == 0)  {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-sellable-items");
            return;
        }

        final double totalValue = worth.get();

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%value%", Utils.format(totalValue));

        this.plugin.getEconomy().addBalance(player, totalValue);

        plugin.getMessageCache().sendMessage(player, "messages.sold-contents", replacer);
    }
}
