package org.minejewels.jewelssell.commands.sellwand.sub;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Sets;
import org.minejewels.jewelssell.JewelsSell;
import org.minejewels.jewelssell.sellwand.Sellwand;

import java.util.Optional;

public class SellwandGiveCommand extends AbyssSubCommand<JewelsSell> {

    public SellwandGiveCommand(final JewelsSell plugin) {
        super(plugin, 3, Sets.immutable.of("give"));
    }

    @Override
    public void execute(final CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!sender.hasPermission("sellwand.admin")) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.no-permission");
            return;
        }

        final Optional<Player> optionalPlayer = Optional.ofNullable(context.asPlayer(0));

        if (!optionalPlayer.isPresent()) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.player-doesnt-exist");
            return;
        }

        final Player player = optionalPlayer.get();

        if (!Utils.isInteger(String.valueOf(context.asInt(1)))) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.invalid-number");
            return;
        }

        final long uses = context.asInt(1);

        if (uses <= -2) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.invalid-number");
            return;
        }

        if (!Utils.isLong(String.valueOf(context.asInt(1)))) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.invalid-number");
            return;
        }

        final double multiplier = context.asDouble(2);

        if (multiplier <= 0) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.invalid-number");
            return;
        }

        if (!Utils.isDouble(String.valueOf(context.asDouble(2)))) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.invalid-number");
            return;
        }

        final Sellwand sellwand = new Sellwand(plugin, multiplier, uses);

        sellwand.giveWand(player);
    }
}
