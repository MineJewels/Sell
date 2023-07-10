package org.minejewels.jewelssell.commands.sellwand;

import net.abyssdev.abysslib.command.AbyssCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Lists;
import org.minejewels.jewelssell.JewelsSell;
import org.minejewels.jewelssell.commands.sellwand.sub.SellwandGiveCommand;

public class SellwandCommand extends AbyssCommand<JewelsSell, CommandSender> {

    public SellwandCommand(final JewelsSell plugin) {
        super(plugin, "sellwand", CommandSender.class);

        this.setAliases(Lists.mutable.of("sellwand", "sellwands"));

        this.register(new SellwandGiveCommand(plugin));
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        final CommandSender sender = context.getSender();

        if (!sender.hasPermission("sellwand.admin")) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.no-permission");
            return;
        }

        this.plugin.getMessageCache().sendMessage(sender, "messages.sellwand-help");

    }
}
