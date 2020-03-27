package com.cdejong.nomorephantoms.hooks;

import com.cdejong.nomorephantoms.NoMorePhantoms;
import org.jetbrains.annotations.NotNull;

public abstract class PluginHook {
    protected NoMorePhantoms plugin;
    private String name;

    /**
     * @param name Name of the plugin hooking into.
     * @param plugin Running BiemBlocks instance.
     */
    public PluginHook(@NotNull String name, @NotNull NoMorePhantoms plugin) {
        this.name = name;
        this.plugin = plugin;
    }

    /**
     * Called when the hook gets loaded.
     * @return Boolean whether the hook is loaded correctly.
     */
    public boolean onHook() {
        return true;
    }

    /**
     * Called when the hook gets unloaded.
     * @return Boolean whether the hook is unloaded correctly.
     */
    public boolean onUnhook() {
        return true;
    }

    /**
     * Returns the name of the plugin hook.
     * @return Name of the plugin hook.
     */
    @NotNull
    public String getName() {
        return name;
    }
}
