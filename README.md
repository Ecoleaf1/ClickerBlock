# ClickerBlocks
ClickerBlocks is a simple bukkit plugin that allows the ability to execute commands by clicking on certain blocks, licenced under MIT

Supported Version: 1.14+
## Commands
`/ClickerBlock` - Prints a list of command usages.
`/ClickerBlock add {Run by: PLAYER/CONSOLE} {Command}` - Binds a command where the player is looking
`/ClickerBlock remove` -  Removes all commands bind to the block being looked at by the player.
`/ClickerBlock reload` - Reloads all clickerblocks and all of its commands. Run this if you maunally added ClickerBlock files or/and added more commands without restarting the server.

## Permission Nodes
`/clickerblock` - `net.wigoftime.clickycommand.customblock`

## Advanced
### How it works
Every time the server starts up, it scans the `Blocks` folder under the plugin folder. From there it scans every file.

The plugin creates a ClickerBlock for each file scanned and sets the location by the file name in this format: World_Name X Y Z.yml
Then once the ClickerBlock is created, it is saved in Memory (RAM) or more specifically for programmers out there, a hashmap, to bind a Location to a Clickerblock.

From there when a player interacts with a block, it checks if the clicked location is bind to any ClickerBlock in the hashmap. If it does contain a spot in the hashmap, cancel the interact event (To prevent things like chests from opening) and execute the ClickerBlock commands.

### YAML File Format
If you want to manually add in commands through the way of opening up text editors, then here is the default layout.

```yaml
Run by: {PLAYER/CONSOLE}
Commands:
- {Command 1}
- {Command 2}
- {Command 3}
- {...Etc}
```

If you manually added a new Clickerblock file or/and edited files, be sure to run the reload command or restart the server to have the new changes come to effect.
