# Armadillo Macro for Hypixel Skyblock

If you would like to use this QOL modification for Hypixel Skyblock, download this mod and place it into ur `.minecraft/mods` folder.
If you have any inquiries, check out the [DISCORD](https://discord.gg/vHrYUdEgC3).
## CURRENT FEATURES
### **==== Nuker ====**
- Better nuker and a "legit clear" option that displays blocks that you need to mine
- Option to customize ur FOV to minimize ban chance
- Helper render lines in order to see where you have to break blocks (If u want to be 100% safe)

### **==== Route Check ====**
- Option to check for structures around ur route. (run /helpStructureCheck in game for more info).

### **==== Player Failsafes ====**
- Player failsafe that will stop the macro and switch to the skyblock menu if a player is close by. 
- Option to check if the player can actually see you.
- Answer hack accusations with customizable prompts.

### **==== Macro Failsafes ====**
- Auto restart macro if it stops for some reason (I MIGHT create a dedicated module that can turn on pathfinding if it stops or cant tp)
- Option to reattempt TP if the macro tped on the wrong block.
- (BETA) A server TPS failsafe.
- (BETA) Option to actually attempt to find a block where u can tp to then tp to the destination block (MAY LAG!)

### **==== Remote Control ====**
#### **WARNING! THIS IS A BETA FEATURE AND MAY NOT WORK AS INTENDED!**
- Website with various options including "Say", "Restart", and "Stop".
- Chat on website.

### **==== Route Sharing Utils ====**
- Option to import a route from Pastebin (Soopy or coleweight format, use .
- Option to import route from clipboard.
All the routes and data will be stored in the dedicated folder `.minecraft/MiningInTwo`
- (SOON) Include number of default routes that you can quick import and edit to your liking.
## Commands
- /mitmenu -> Opens menu.
- /block -> Adds a block to route.
- /clear -> Clears the route.
- /createRoute (route name) -> Will create a new route and select it.
- /selectRoute (name) -> Will select the route you provided if it exists in the folder.
- /insertInMiddle (number) -> Will insert the block you are standing on to the route.
- /removeBlock (number) -> Removes the block from the route selected.
- /replaceBlock (number) -> Replaces the # block with the one you are standing on.
- /addStructure -> Creates a new structure point to later be used in structure checks.
- /clearStructures -> Deletes all the structures that you have previously added.
- /removeStructure (point) -> Will remove a structure at a specific point.
- /structurePoints -> Will display a point at every structure that you added.
- /checkRoute -> **WARNING! MAY LAG!** This is a complicated process. Run /helpStructureCheck to see how works.
- /currentRoute -> Says the name of the route currently selected.
- /deleteRoute -> Deletes the route completely (including the file).
- /currentRoutes -> Will show all the routes that you currently have.
- /importRoute (name) -> Will attempt to make a new route with whatever you have in ur clipboard
- /importFromWeb (link) (name) -> Imports route from PASTEBIN.COM.
- /helperLines -> Will display route-clear-help lines to help you clear legit (Suggested to use /clearRouteLegit instead).
- /clearRouteLegit -> Will display the blocks you have to break in order for the route to be clear.
- /addAccusation (detection) (answer) -> Will add an accusation to the list. AKA if player says something that contains (detection) then the macro will respond with (answer).
- /removeAccusation (detection) -> will remove an accusation. Yes, u have to put the EXACT 'detection'.
