# QuikCore [![build-status]][build-link]
A better way to code MinecraftForge mods.

## Reasoning
MinecraftForge requires the modder to write far too much boilerplate code that distracts the modder from the actual coding, and winds up breaking with every Minecraft update. Take for example the typical BlockRegistry class. You have to manually list every block in your mod, just to make sure that they get registered. This is made worse when you consider version control, where if you have multiple modders adding blocks, then you have a bunch of merge conflicts on a class with only one use case. As such, wouldn't it be nice if you could eliminate the class entirely? What if you just had to annotate an item or a block class, and everything else was handled for you? If so, QuikCore is the Forge library mod for you!

## Integration with QuikMod
QuikCore is planned to be directly integrated with QuikMod, so that you can go from zero to a Minecraft mod in less time than it took to run the Forge buildscripts prior to @AbrarSyed101's glorious Gradle intervention. Specifically, QuikCore aims to be used as part of QuikMod's code generation functionality, so that you can instantly generate functional item or block classes from which to build upon.

## Planned Features
- QuikConfigurable --> as per AgriCore's @AgriConfigurable
- QuikCommand --> as per ModularCommands' @Command
- QuikBlock --> an annotation for block classes that removes the need for manual registration.
- QuikItem --> an annotation for item classes that removes the need for manual registration.
- QuikMod --> an annotation for mods that inverts the flow of control, so the modder can be more efficient.
- QuikPlugin --> an annotation that handles api resolution for mod compatibility plugins.

## Inspiration
QuikCore is designed after a foray into Spring Boot's annotation magic, where with a few well-placed annotations, you can have a working performant web app.

## A Note on Generation
QuikCore has no intention of generating all the code for you, like some other proposed mod solutions. Most of the time, said solutions generate tenuous code that lacks any expandability beyond the basics. QuikMod, on the other hand, simply aims to take care of some of the boilerplate stuff as to leave you free to focus on things like implementing those methane chickens. Bawk Bawk Boom. That's QuikMod + QuikCore for you.

[build-link]:https://travis-ci.org/QuikMod/QuikCore
[build-status]:https://travis-ci.org/QuikMod/QuikCore.svg?branch=master "Travis-CI Build Status"
