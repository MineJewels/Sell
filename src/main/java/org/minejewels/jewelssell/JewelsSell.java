package org.minejewels.jewelssell;

import lombok.Getter;
import net.abyssdev.abysslib.config.AbyssConfig;
import net.abyssdev.abysslib.economy.provider.Economy;
import net.abyssdev.abysslib.economy.registry.impl.DefaultEconomyRegistry;
import net.abyssdev.abysslib.patterns.registry.Registry;
import net.abyssdev.abysslib.plugin.AbyssPlugin;
import net.abyssdev.abysslib.text.MessageCache;
import org.minejewels.jewelssell.commands.sell.SellCommand;
import org.minejewels.jewelssell.commands.sellwand.SellwandCommand;
import org.minejewels.jewelssell.item.registry.ItemRegistry;
import org.minejewels.jewelssell.listeners.InteractListener;

@Getter
public final class JewelsSell extends AbyssPlugin {

    private static JewelsSell api;

    private final AbyssConfig langConfig = this.getAbyssConfig("lang");
    private final AbyssConfig settingsConfig = this.getAbyssConfig("settings");
    private final AbyssConfig itemsConfig = this.getAbyssConfig("items");

    private final MessageCache messageCache = new MessageCache(langConfig);

    private final Registry<String, Double> itemRegistry = new ItemRegistry();

    private Economy economy;

    @Override
    public void onEnable() {
        JewelsSell.api = this;

        this.economy = new DefaultEconomyRegistry().getEconomy(this.settingsConfig.getString("economy-provider"));

        this.loadMessages(this.messageCache, langConfig);
        this.loadItems();

        new SellCommand(this).register();
        new SellwandCommand(this).register();

        new InteractListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadItems() {
        for (final String key : this.itemsConfig.getSectionKeys("items")) {
            this.itemRegistry.register(key.toUpperCase(), this.itemsConfig.getDouble("items." + key));
        }
    }

    public static JewelsSell get() {
        return JewelsSell.api;
    }
}
