package com.nexora.plugins;

import java.io.IOException;

import com.jagex.Client;
import com.jagex.net.ResourceRequest;
import com.jagex.net.ResourceResponse;

/**
 * Base interface for Nexora client plugins.
 * All plugins must implement this interface to be loaded by the plugin system.
 */
public interface ClientPlugin {
    
    /**
     * Called when the plugin is first loaded.
     * Used to initialize plugin resources and register loaders.
     */
    void initializePlugin();
    
    /**
     * Called when the game/client has finished loading.
     * This is where version-specific loaders should be registered.
     * 
     * @param client The game client instance
     * @throws Exception if initialization fails
     */
    void onGameLoaded(Client client) throws Exception;
    
    /**
     * Called when a resource is delivered from the cache.
     * Default implementation does nothing.
     * 
     * @param resource The delivered resource
     */
    default void onResourceDelivered(ResourceResponse resource) {
        
    }

}
