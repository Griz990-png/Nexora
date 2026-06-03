package com.nexora.plugins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * Dynamic plugin loader for Nexora client plugins.
 * Loads all plugins from the plugins/active directory using Java's ServiceLoader.
 */
@Slf4j
public class ClientPluginLoader {
    
    private static int count = 0;
    private static ServiceLoader<ClientPlugin> serviceLoader;
    
    /**
     * Gets the ServiceLoader instance, loading plugins if necessary.
     * @return ServiceLoader for ClientPlugin
     */
    public static ServiceLoader<ClientPlugin> getServiceLoader(){
        if(serviceLoader == null)
            loadPlugins();
        return serviceLoader;
    }
    
    /**
     * Loads all plugin JAR files from the plugins/active directory.
     * Uses a URLClassLoader to dynamically load plugins at runtime.
     */
    public static void loadPlugins() {
        File pluginPath = new File("plugins" + File.separator + "active");
        if(!pluginPath.exists()) {
            log.warn("Plugin directory does not exist: {}", pluginPath.getAbsolutePath());
            serviceLoader = ServiceLoader.load(ClientPlugin.class);
            return;
        }
        
        File[] pluginFiles = pluginPath.listFiles();
        if(pluginFiles == null || pluginFiles.length == 0) {
            log.info("No plugins found in plugin directory");
            serviceLoader = ServiceLoader.load(ClientPlugin.class);
            return;
        }
        
        log.info("Plugin folder contains {} files.", pluginFiles.length);
        File[] plugins = pluginPath.listFiles((File dir, String name) -> name.endsWith(".jar"));
        
        if(plugins == null || plugins.length == 0) {
            log.warn("No plugin JAR files found");
            serviceLoader = ServiceLoader.load(ClientPlugin.class);
            return;
        }
        
        List<URL> urls = Lists.newArrayList();
        
        for(File pluginFile : plugins) {
            try {
                URL url = pluginFile.toURI().toURL();
                urls.add(url);
                log.info("Added {} to plugin URL", url.toString());
            } catch (MalformedURLException e) {
                log.error("Failed to add plugin URL: {}", pluginFile.getName(), e);
            }
        }
        
        URLClassLoader urlClassLoader = URLClassLoader.newInstance(urls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());

        serviceLoader = ServiceLoader.load(ClientPlugin.class, urlClassLoader);
        forEach(plugin -> {
            plugin.initializePlugin();
            count++;
        });
        log.info("Loaded {} client plugins!", count);
        try {
            urlClassLoader.close();
        } catch (IOException e) {
            log.error("Error closing plugin class loader", e);
        }
    }
    
    /**
     * Iterates over all loaded plugins and applies a consumer function.
     * 
     * @param consumer Function to apply to each plugin
     */
    public static void forEach(Consumer<ClientPlugin> consumer) {
        ServiceLoader<ClientPlugin> loader = getServiceLoader();
        
        for(ClientPlugin plugin : loader) {
            consumer.accept(plugin);
        }
    }
}
