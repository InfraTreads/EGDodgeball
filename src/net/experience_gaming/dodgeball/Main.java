package net.experience_gaming.dodgeball;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	
	//define some vars
	public final Logger logger = Logger.getLogger("Minecraft");
	public Plugin plugin;
	public static Map<Player, Integer> playerOriginalLocX = new HashMap<Player, Integer>();
	public static Map<Player, Integer> playerOriginalLocY = new HashMap<Player, Integer>();
	public static Map<Player, Integer> playerOriginalLocZ = new HashMap<Player, Integer>();
	public static Map<Player, Integer> playerOriginalLocYaw = new HashMap<Player, Integer>();
	public static Map<Player, Integer> playerOriginalLocPitch = new HashMap<Player, Integer>();
	public static Map<Player, World> playerOriginalLocWorld = new HashMap<Player, World>();
    public static ArrayList<Player> playersInGame = new ArrayList<Player>();
    public static ArrayList<Player> playersInGameRed = new ArrayList<Player>();
    public static ArrayList<Player> playersInGameBlue = new ArrayList<Player>();
    public int corner = 0;
    public int spawns = 0;
    public int middle = 0;
    public static String arenaName = "NO_NAME";
    
    //Plugin disable/server stop
    @Override
    public void onDisable() {
            PluginDescriptionFile pdfFile = this.getDescription();
            this.logger.info(pdfFile.getName() + " Has Been Disabled!");
    }
   
    //plugin enable/server start
    @Override
    public void onEnable() {
            PluginDescriptionFile pdfFile = this.getDescription();
    		getServer().getPluginManager().registerEvents(new EventListener(this), this);
            this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!");
            this.getConfig().options().copyDefaults(true);
            saveConfig();
            
    }

    //Commands check
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    		//get player who send the command
            Player player = (Player) sender;
           
            //Current commands:
            //
            //help
            //commands
            //start
            //stop
            //leave
            //join
            //create
            
            
            
            //do something if command = dodgeball
            if(commandLabel.equalsIgnoreCase("dodgeball")){
            	//if no arguments provided
            	if(args.length == 0){
            		player.sendMessage(ChatColor.GOLD + "Use " + ChatColor.RED + "/dodgeball help" + ChatColor.GOLD + " to see the help page!");
            		
            	//if arg0 = help
            	}else if(args[0].equalsIgnoreCase("help")){
            		player.sendMessage(ChatColor.GREEN + "This will be the Help page.");
            		
            	//if arg0 = commands
            	} else if(args[0].equalsIgnoreCase("commands")){       
            		player.sendMessage(ChatColor.GOLD + "Dodgeball commands:");
            		player.sendMessage(ChatColor.DARK_AQUA + "/dodgeball start              Starts the game.");
            		player.sendMessage(ChatColor.DARK_AQUA + "/dodgeball stop               Stops the game.");
            		player.sendMessage(ChatColor.DARK_AQUA + "/dodgeball commands         Shows this page.");
            		player.sendMessage(ChatColor.DARK_AQUA + "/dodgeball join               Join the game.");
            		player.sendMessage(ChatColor.DARK_AQUA + "/dodgeball leave              Leave the game.");
            		player.sendMessage(ChatColor.DARK_AQUA + "/dodgeball info               Shows some info.");
            	
            	//TODO: command usage for starting countdown of game
            	//if arg0 = start
            	} else if(args[0].equalsIgnoreCase("start")){
            		if(args.length == 1){
            			player.sendMessage(ChatColor.RED + "Please specify the arena name to start");
            		} else {
            			if(getConfig().getString("Arenas" + args[1]) != ""){
            				player.sendMessage(ChatColor.RED + "Starting arena: " + args[1]);
            				tpPlayersToArena(args[1]);
            			} else { 
            				player.sendMessage(ChatColor.RED + "Unknown arena: " + args[1] + ".\nUse '/dodgeball list' to list the arenas (Case sensitive!).");
            			}
            		}
            	
            	//TODO: command usage for force stopping a game
            	//if arg0 = stop
            	} else if(args[0].equalsIgnoreCase("stop")){
            		player.sendMessage(ChatColor.RED + "Currently unused command");
            		
            	//if arg0 = leave
            	} else if(args[0].equalsIgnoreCase("leave")){
            		player.sendMessage(ChatColor.RED + "You left the game!");
            		playersInGame.remove(player);
            		playersInGameRed.remove(player);
            		playersInGameBlue.remove(player);
            		tpPlayerToOriginalLoc(player);
            		
            	//if arg0 = join
            	} else if(args[0].equalsIgnoreCase("join")){
            		if(playersInGame.contains(player)){
            			player.sendMessage(ChatColor.RED + "You have already joined a game!\nUse /dodgeball leave to leave the game.");
            		} else {
            			player.setHealth(20);
            			playersInGame.add(player);
            			if(args.length == 1){
            				if(playersInGameRed.size() == playersInGameBlue.size()){
            					playersInGameRed.add(player);
            					player.sendMessage(ChatColor.RED + "You joined team RED!");
            					tpPlayerToLobby(player);
            				} else if (playersInGameRed.size() < playersInGameBlue.size()) {
            					playersInGameRed.add(player);
            					player.sendMessage(ChatColor.RED + "You joined team RED!");
            					tpPlayerToLobby(player);
            				} else if (playersInGameRed.size() > playersInGameBlue.size()){
            					playersInGameBlue.add(player);
            					player.sendMessage(ChatColor.BLUE + "You joined team BLUE!");
            					tpPlayerToLobby(player);
            				}
            			} else if(args[1].equalsIgnoreCase("blue")){
            				playersInGameBlue.add(player);
            				player.sendMessage(ChatColor.BLUE + "You joined team BLUE!");
            				tpPlayerToLobby(player);
            			} else if(args[1].equalsIgnoreCase("red")){
            				playersInGameRed.add(player);
            				player.sendMessage(ChatColor.RED + "You joined team RED!");
            				tpPlayerToLobby(player);
            			}
            		}
            		
            	//if arg0 = create
            	} else if(args[0].equalsIgnoreCase("create")){
            		if(args.length == 1){
            			player.sendMessage(ChatColor.RED + "Specify an action, use /dodgeball commands for the availible commands!");
            		} else if(args.length == 2 && args[1].equalsIgnoreCase("lobby")){
            			this.getConfig().set("Lobby.X", Math.floor(player.getLocation().getX()));
            			this.getConfig().set("Lobby.Y", Math.floor(player.getLocation().getY()));
            			this.getConfig().set("Lobby.Z", Math.floor(player.getLocation().getZ()));
            			this.getConfig().set("Lobby.Yaw", Math.floor(player.getLocation().getYaw()));
        				this.getConfig().set("Lobby.World", player.getLocation().getWorld().getName());
        				saveConfig();
            			player.sendMessage(ChatColor.RED + "Lobby spawn set!");
            		} else if(args.length == 3 && args[1].equalsIgnoreCase("arena")){
            			this.getConfig().set("Arenas."+ args[2], "");
            			arenaName = args[2];
            			player.sendMessage(ChatColor.RED + "Arena Created: " + args[2]);
            			player.sendMessage(ChatColor.RED + "Define the arena corners with /dodgeball create corner next");
            		} else if(args.length == 3 && args[1].equalsIgnoreCase("corner") && args[2].equalsIgnoreCase("next") && arenaName != "NO_NAME"){
            			if(corner == 0){
            				this.getConfig().set("Arenas." + arenaName + ".X1", Math.floor(player.getLocation().getX()));
            				this.getConfig().set("Arenas." + arenaName + ".Z1", Math.floor(player.getLocation().getZ()));
            				player.sendMessage(ChatColor.RED + "Corner 1/4 set!");
            				corner++;
            			} else if(corner == 1){
            				this.getConfig().set("Arenas." + arenaName + ".X2", Math.floor(player.getLocation().getX()));
            				this.getConfig().set("Arenas." + arenaName + ".Z2", Math.floor(player.getLocation().getZ()));
            				player.sendMessage(ChatColor.RED + "Corner 2/4 set!");
            				corner++;
            			} else if(corner == 2){
            				this.getConfig().set("Arenas." + arenaName + ".X3", Math.floor(player.getLocation().getX()));
            				this.getConfig().set("Arenas." + arenaName + ".Z3", Math.floor(player.getLocation().getZ()));
            				player.sendMessage(ChatColor.RED + "Corner 3/4 set!");
            				corner++;
            			} else if(corner == 3){
            				this.getConfig().set("Arenas." + arenaName + ".X4", Math.floor(player.getLocation().getX()));
            				this.getConfig().set("Arenas." + arenaName + ".Z4", Math.floor(player.getLocation().getZ()));
            				this.getConfig().set("Arenas." + arenaName + ".World", player.getLocation().getWorld().getName());
            				player.sendMessage(ChatColor.RED + "Corner 4/4 set!");
            				corner++;
            				player.sendMessage(ChatColor.RED + "Use '/dodgeball create middle next' to define the middle");
            			}
            		} else if(args.length == 3 && args[1].equalsIgnoreCase("middle")){
            			if(args[2].equalsIgnoreCase("next")){
            				if(middle == 0){
            					this.getConfig().set("Arenas." + arenaName + ".MiddleLine.X1", Math.floor(player.getLocation().getX()));
            					this.getConfig().set("Arenas." + arenaName + ".MiddleLine.Z1", Math.floor(player.getLocation().getZ()));
            					middle++;
            					player.sendMessage(ChatColor.RED + "Middle Line boundary 1/2 set!");
            				} else if(middle == 1){
            					this.getConfig().set("Arenas." + arenaName + ".MiddleLine.X2", Math.floor(player.getLocation().getX()));
            					this.getConfig().set("Arenas." + arenaName + ".MiddleLine.Z2", Math.floor(player.getLocation().getZ()));
            					middle++;
            					player.sendMessage(ChatColor.RED + "Middle Line boundary 2/2 set!");
            					player.sendMessage(ChatColor.RED + "Use '/dodgeball create spawn red' and '/dodgeball create spawn blue' to create the game spawns.");
            				} else {
            					player.sendMessage(ChatColor.RED + "Middle Line boundary already defined!");
            				}
            			
            			} else {
            			
            			}
            		} else if(args.length == 3 && args[1].equalsIgnoreCase("spawn")){
            			if(args[2].equalsIgnoreCase("red")){
            				this.getConfig().set("Arenas." + arenaName + ".SpawnRed.X", Math.floor(player.getLocation().getX()));
            				this.getConfig().set("Arenas." + arenaName + ".SpawnRed.Y", Math.floor(player.getLocation().getY()));
            				this.getConfig().set("Arenas." + arenaName + ".SpawnRed.Z", Math.floor(player.getLocation().getZ()));
            				this.getConfig().set("Arenas." + arenaName + ".SpawnRed.Yaw", Math.floor(player.getLocation().getYaw()));
            				player.sendMessage(ChatColor.RED + "Red Spawn set!");
            				spawns++;
            				if(spawns == 2){
            					spawns = 0;
            					corner = 0;
            					middle = 0;
            					arenaName = "NO_NAME";
            					saveConfig();
            					player.sendMessage(ChatColor.RED + "Done!");
            				}
            			} else if(args[2].equalsIgnoreCase("blue")){
            				this.getConfig().set("Arenas." + arenaName + ".SpawnBlue.X", Math.floor(player.getLocation().getX()));
            				this.getConfig().set("Arenas." + arenaName + ".SpawnBlue.Y", Math.floor(player.getLocation().getY()));
            				this.getConfig().set("Arenas." + arenaName + ".SpawnBlue.Z", Math.floor(player.getLocation().getZ()));
            				this.getConfig().set("Arenas." + arenaName + ".SpawnBlue.Yaw", Math.floor(player.getLocation().getYaw()));
            				player.sendMessage(ChatColor.RED + "Blue Spawn set!");
            				spawns++;
            				if(spawns == 2){
            					spawns = 0;
            					corner = 0;
            					middle = 0;
            					arenaName = "NO_NAME";
            					saveConfig();
            					player.sendMessage(ChatColor.RED + "Done!");
            				}
            			} else {
            				player.sendMessage(ChatColor.RED + "Invalid team, choose either Red or Blue!");
            			}
            			
            		} else {
            			player.sendMessage(ChatColor.RED + "You must first specify an Arena name!");
            		}
            	}
            }
            return false;
    }
    
	public void tpPlayerToLobby(Player player){
		int playerX = (int) Math.round(player.getLocation().getX());
		int playerY = (int) Math.round(player.getLocation().getY());
		int playerZ = (int) Math.round(player.getLocation().getZ());
		int playerYaw = (int) Math.round(player.getLocation().getYaw());
		int playerPitch = (int) Math.round(player.getLocation().getPitch());
		World playerWorld = (World)player.getLocation().getWorld();
		
		playerOriginalLocX.put(player, playerX);
		playerOriginalLocY.put(player, playerY);
		playerOriginalLocZ.put(player, playerZ);
		playerOriginalLocYaw.put(player, playerYaw);
		playerOriginalLocPitch.put(player, playerPitch);
		playerOriginalLocWorld.put(player, playerWorld);
		
		//teleport
		
		int tpX = this.getConfig().getInt("Lobby.X");
		int tpY = this.getConfig().getInt("Lobby.Y");
		int tpZ = this.getConfig().getInt("Lobby.Z");
		int tpYaw = this.getConfig().getInt("Lobby.Yaw");
		World tpWorld = getServer().getWorld((String) this.getConfig().get("Lobby.World"));
		
		Location loc = new Location(tpWorld, tpX, tpY, tpZ, tpYaw, player.getLocation().getPitch());
		player.teleport(loc);
	}
	
	public void tpPlayersToArena(String arena){
		for(Player player:Main.playersInGameRed){
			int spawnX = getConfig().getInt("Arenas." + arena + ".SpawnRed.X");
			int spawnY = getConfig().getInt("Arenas." + arena + ".SpawnRed.Y");
			int spawnZ = getConfig().getInt("Arenas." + arena + ".SpawnRed.Z");
			int spawnYaw = getConfig().getInt("Arenas." + arena + ".SpawnRed.Yaw");
			World world = getServer().getWorld((String)getConfig().get("Arenas." + arena + ".World"));
			
			Location loc = new Location(world, spawnX, spawnY, spawnZ, spawnYaw, player.getLocation().getPitch());
			player.teleport(loc);
		}
		
		for(Player player:Main.playersInGameBlue){
			int spawnX = getConfig().getInt("Arenas." + arena + ".SpawnBlue.X");
			int spawnY = getConfig().getInt("Arenas." + arena + ".SpawnBlue.Y");
			int spawnZ = getConfig().getInt("Arenas." + arena + ".SpawnBlue.Z");
			int spawnYaw = getConfig().getInt("Arenas." + arena + ".SpawnBlue.Yaw");
			World world = getServer().getWorld((String)getConfig().get("Arenas." + arena + ".World"));
			
			Location loc = new Location(world, spawnX, spawnY, spawnZ, spawnYaw, player.getLocation().getPitch());
			player.teleport(loc);
		}
	}
	
	public void tpPlayerToOriginalLoc(Player player){
		int playerX = playerOriginalLocX.get(player);
		int playerY = playerOriginalLocY.get(player);
		int playerZ = playerOriginalLocZ.get(player);
		int playerYaw = playerOriginalLocYaw.get(player);
		int playerPitch = playerOriginalLocPitch.get(player);
		World playerWorld = playerOriginalLocWorld.get(player);
		
		Location loc = new Location(playerWorld, playerX, playerY, playerZ, playerYaw, playerPitch);
		player.teleport(loc);
	}
}