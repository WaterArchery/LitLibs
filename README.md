# LitLibs

LitLibs is a Java based plugin and library for Minecraft servers. It provides a set of libraries and hooks to enhance the functionality of your plugin and make plugin development easier.

## Features

- **HookHandler**: Handles various hooks for the plugin.
- **MessageHandler**: Handles messages within the plugin.
- **SoundHandler**: Handles sound effects.
- **TitleHandler**: Handles title-related operations.
- **NBTAPIHook**: Provides an interface to the NBT API.
- **VersionHandler**: Handles version-related operations.
- **ConfigManager**: Manages configuration and language files.

## Getting Started

To use LitLibs in your project, you need to include it as a dependency. You can compile it and use it as depencency. Since it's a Maven project, you can do this by adding the following lines to your `pom.xml` file:

```xml
<dependencies>
    <dependency>
        <groupId>me.waterarchery</groupId>
        <artifactId>litlibs</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
