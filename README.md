# Nexora - Advanced RuneScape Map Editor

A modernized and rebuilt version of RSPSi, now called **Nexora**, maintaining full compatibility with all existing plugins while providing an enhanced and maintainable codebase for map editing and game data management.

## Features

- **Full Plugin Compatibility**: All existing RSPSi plugins work without modification
- **Multi-Version Support**: Support for RS2, RS3, OSRS, and 317-based caches
- **Plugin Architecture**: Dynamic plugin loading via ServiceLoader
- **Editor UI**: JavaFX-based graphical editor interface
- **Client**: Game rendering engine with cache support
- **Enhanced Performance**: Optimized rendering and resource management
- **Modern Build System**: Gradle-based multi-module project

## Project Structure

```
Nexora/
├── Client/              # Core game client and rendering engine
├── Editor/              # JavaFX-based editor UI
├── Plugins/             # Plugin modules and loaders
├── RS-Cache-Library/    # Cache file handling
└── build.gradle         # Gradle build configuration
```

## Building

```bash
# Build all modules
./gradlew build

# Run the editor
./gradlew Editor:run

# Build and deploy plugins
./gradlew Plugins:buildAndMove
```

## Plugin Development

Create plugins by implementing the `ClientPlugin` interface:

```java
public class MyPlugin implements ClientPlugin {
    @Override
    public void initializePlugin() {
        // Initialize plugin
    }
    
    @Override
    public void onGameLoaded(Client client) throws Exception {
        // Called when game is loaded
    }
    
    @Override
    public void onResourceDelivered(ResourceResponse resource) {
        // Handle resource delivery
    }
}
```

Plugins are automatically discovered and loaded from the `plugins/active` directory.

## Architecture

### Core Components

- **Client**: Main game engine handling rendering, caching, and game logic
- **Editor**: User interface for map editing and configuration
- **Plugin System**: Dynamic plugin loading with ServiceLoader pattern
- **Cache Handling**: Support for multiple cache formats (317, OSRS, RS2, RS3)

### Plugin System

The plugin system uses Java's ServiceLoader to dynamically load plugins at runtime. Each plugin must:
1. Implement the `ClientPlugin` interface
2. Be packaged as a JAR file
3. Include a META-INF/services file with the plugin class name
4. Be placed in the `plugins/active` directory

## Requirements

- Java 8 or higher
- Gradle 6.1+

## License

MIT License - See LICENSE file for details

## Contributing

Contributions are welcome! Please ensure:
- Code follows existing style conventions
- All plugins maintain backward compatibility
- Documentation is updated with significant changes
