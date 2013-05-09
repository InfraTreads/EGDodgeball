package net.experience_gaming.dodgeball;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener{
	
	private Main plugin;
	
	public EventListener(Main instance) {
		plugin = instance;
	}

	@EventHandler
    public void OnSnowballHitPlayer(EntityDamageByEntityEvent event){
    	if(event.getDamager() instanceof Snowball && event.getEntity() instanceof Player){
    		
    		int X1 = plugin.getConfig().getInt("Arenas.Test.X1");
    		int X2 = plugin.getConfig().getInt("Arenas.Test.X2");
    		int X3 = plugin.getConfig().getInt("Arenas.Test.X3");
    		int X4 = plugin.getConfig().getInt("Arenas.Test.X4");
    		
    		int Z1 = plugin.getConfig().getInt("Arenas.Test.Z1");
    		int Z2 = plugin.getConfig().getInt("Arenas.Test.Z2");
    		int Z3 = plugin.getConfig().getInt("Arenas.Test.Z3");
    		int Z4 = plugin.getConfig().getInt("Arenas.Test.Z4");
    		
    		//Do some comparison
    		
    		int xMax = Math.max(Math.max(Math.max(X1,X2),X3),X4);
    		int xMin = Math.min(Math.min(Math.min(X1,X2),X3),X4);
    		
    		int zMax = Math.max(Math.max(Math.max(Z1,Z2),Z3),Z4);
    		int zMin = Math.min(Math.min(Math.min(Z1,Z2),Z3),Z4);
    		
    		Snowball snowball = (Snowball)event.getDamager();
    		Player player = (Player)event.getEntity();
    		LivingEntity shooter = snowball.getShooter();
    		if(Math.floor(snowball.getLocation().getX()) >= xMin && Math.floor(snowball.getLocation().getX()) <= xMax && Math.floor(snowball.getLocation().getZ()) >= zMin && Math.floor(snowball.getLocation().getZ()) <= zMax){
    			if(Main.playersInGame.contains(player)){
    				if(Main.playersInGameRed.contains(player) && Main.playersInGameBlue.contains(shooter)){
    					player.setHealth(player.getHealth()-plugin.getConfig().getInt("Gamemodes.Standard.Snowball_Damage"));
    				} else if(Main.playersInGameBlue.contains(player) && Main.playersInGameRed.contains(shooter)){
    					player.setHealth(player.getHealth()-plugin.getConfig().getInt("Gamemodes.Standard.Snowball_Damage"));
    				}
    			}
    		}
    	}
	}
	
	@EventHandler
	public void OnSnowballHitBlock(ProjectileHitEvent event){
		this.plugin = (Main) plugin;
		if(event.getEntity() instanceof Snowball){
			
    		
    		int X1 = plugin.getConfig().getInt("Arenas.Test.X1");
    		int X2 = plugin.getConfig().getInt("Arenas.Test.X2");
    		int X3 = plugin.getConfig().getInt("Arenas.Test.X3");
    		int X4 = plugin.getConfig().getInt("Arenas.Test.X4");
    		
    		int Z1 = plugin.getConfig().getInt("Arenas.Test.Z1");
    		int Z2 = plugin.getConfig().getInt("Arenas.Test.Z2");
    		int Z3 = plugin.getConfig().getInt("Arenas.Test.Z3");
    		int Z4 = plugin.getConfig().getInt("Arenas.Test.Z4");
    		
    		//Do some comparison
    		
    		int xMax = Math.max(Math.max(Math.max(X1,X2),X3),X4);
    		int xMin = Math.min(Math.min(Math.min(X1,X2),X3),X4);
    		
    		int zMax = Math.max(Math.max(Math.max(Z1,Z2),Z3),Z4);
    		int zMin = Math.min(Math.min(Math.min(Z1,Z2),Z3),Z4);
    		
			Snowball snowball = (Snowball)event.getEntity();
			Location snowballloc = snowball.getLocation();
			if(Math.floor(snowball.getLocation().getX()) >= xMin && Math.floor(snowball.getLocation().getX()) <= xMax && Math.floor(snowball.getLocation().getZ()) >= zMin && Math.floor(snowball.getLocation().getZ()) <= zMax){
				ItemStack drop = new ItemStack(Material.SNOW_BALL, 1);
				World world = snowball.getWorld();
				world.dropItem(snowballloc, drop);
			}
		}
	}
	
	@EventHandler
	public void playerMovement(PlayerMoveEvent event){
		int team;
		int SpawnX = 0;
		int SpawnZ = 0;
		int playerNewX;
		int playerNewZ;
		Player player = (Player)event.getPlayer();
		if(Main.playersInGame.contains(player)){
			if(Main.playersInGameBlue.contains(player)){
				team = 2;
			} else {
				team = 1;
			}
			
			if(team == 1){
				SpawnX = plugin.getConfig().getInt("Arenas.Test.SpawnRed.X");
				SpawnZ = plugin.getConfig().getInt("Arenas.Test.SpawnRed.Z");
			} else if(team == 2){
				SpawnX = plugin.getConfig().getInt("Arenas.Test.SpawnBlue.X");
				SpawnZ = plugin.getConfig().getInt("Arenas.Test.SpawnBlue.Z");
			}
			
			int X1 = plugin.getConfig().getInt("Arenas.Test.MiddleLine.X1");
			int X2 = plugin.getConfig().getInt("Arenas.Test.MiddleLine.X2");
			int Z1 = plugin.getConfig().getInt("Arenas.Test.MiddleLine.Z1");
			int Z2 = plugin.getConfig().getInt("Arenas.Test.MiddleLine.Z2");
			Object world = player.getWorld();
			
    		int bX1 = plugin.getConfig().getInt("Arenas.Test.X1");
    		int bX2 = plugin.getConfig().getInt("Arenas.Test.X2");
    		int bX3 = plugin.getConfig().getInt("Arenas.Test.X3");
    		int bX4 = plugin.getConfig().getInt("Arenas.Test.X4");
    		
    		int bZ1 = plugin.getConfig().getInt("Arenas.Test.Z1");
    		int bZ2 = plugin.getConfig().getInt("Arenas.Test.Z2");
    		int bZ3 = plugin.getConfig().getInt("Arenas.Test.Z3");
    		int bZ4 = plugin.getConfig().getInt("Arenas.Test.Z4");
			
			int xMax = Math.max(X1, X2);
			int xMin = Math.min(X1, X2);
			
			int zMax = Math.max(Z1, Z2);
			int zMin = Math.min(Z1, Z2);
			if(Math.floor(player.getLocation().getX()) <= xMax && Math.floor(player.getLocation().getX()) >= xMin && Math.floor(player.getLocation().getZ()) <= zMax && Math.floor(player.getLocation().getZ()) >= zMin){
				
				if(Math.max(SpawnX, xMax) == SpawnX){
					playerNewX = (int) Math.floor(player.getLocation().getX()) + 1;
					if(Math.floor(player.getLocation().getX()) == bX1 || Math.floor(player.getLocation().getX()) == bX2 || Math.floor(player.getLocation().getX()) == bX3 || Math.floor(player.getLocation().getX()) == bX4){
						playerNewX--;
					}
				} else {
					playerNewX = (int) Math.floor(player.getLocation().getX()) - 1;
					if(Math.floor(player.getLocation().getX()) == bX1 || Math.floor(player.getLocation().getX()) == bX2 || Math.floor(player.getLocation().getX()) == bX3 || Math.floor(player.getLocation().getX()) == bX4){
						playerNewX++;
					}
				}
				
				if(Math.max(SpawnZ, zMax) == SpawnZ){
					playerNewZ = (int) Math.floor(player.getLocation().getZ()) + 1;
					if(Math.floor(player.getLocation().getZ()) == bZ1 || Math.floor(player.getLocation().getZ()) == bZ2 || Math.floor(player.getLocation().getZ()) == bZ3 || Math.floor(player.getLocation().getZ()) == bZ4){
						playerNewZ--;
					}
				} else {
					playerNewZ = (int) Math.floor(player.getLocation().getZ()) - 1;
					if(Math.floor(player.getLocation().getZ()) == bZ1 || Math.floor(player.getLocation().getZ()) == bZ2 || Math.floor(player.getLocation().getZ()) == bZ3 || Math.floor(player.getLocation().getZ()) == bZ4){
						playerNewZ++;
					}
				}
				
				Location loc = new Location((World)world, playerNewX, player.getLocation().getY(), playerNewZ);
				loc.setPitch(player.getLocation().getPitch());
				loc.setYaw(player.getLocation().getYaw());
				player.teleport(loc);
			}
			
		}
	}
}