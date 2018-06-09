package com.gmail.sharpcastle33.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.sharpcastle33.handlers.AlembicHandler;

public class AlembicGUI implements Listener {

	public static final String IN_PROGRESS_MESSAGE = ChatColor.RED
			+ "Inventories of Alembics cannot be modified while they are in progress!";
	public static final String ENDER_PEARL_ERROR = ChatColor.RED + "The magics in this item conflict with the energies inside the Alembic.";
	
	@EventHandler
	public void alembicGUI(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		
		if(event.getClickedInventory() == null) {
			return;
		}

		Player p = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		String invName = event.getInventory().getName();

		if (!(invName.equals(AlembicCreationListener.ALEMBIC_CHEST_NAME)
				|| invName.equals(AlembicCreationListener.ALEMBIC_BREWINGSTAND_NAME)
				|| invName.equals(AlembicCreationListener.ALEMBIC_FURNACE_NAME))) {
			return;
		}

		// Determine whether the alembic assembly is active or not
		Chest alembicChest = null;
		if (invName.equals(AlembicCreationListener.ALEMBIC_CHEST_NAME)) {
			alembicChest = (Chest) event.getInventory().getLocation().getBlock().getState();
		} else if (invName.equals(AlembicCreationListener.ALEMBIC_BREWINGSTAND_NAME)) {
			Location chestLoc = event.getInventory().getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();
			alembicChest = (Chest) chestLoc.getBlock().getState();
		} else if (invName.equals(AlembicCreationListener.ALEMBIC_FURNACE_NAME)) {
			Location chestLoc = event.getInventory().getLocation().getBlock().getRelative(BlockFace.UP).getLocation();
			alembicChest = (Chest) chestLoc.getBlock().getState(); 
		}


		//p.sendMessage(invName);
		if (event.getClickedInventory().getName().equals(AlembicCreationListener.ALEMBIC_CHEST_NAME) || event.getClickedInventory().getName().equals(AlembicCreationListener.ALEMBIC_BREWINGSTAND_NAME)) {
			//p.sendMessage("Alchemy: AlembicGUI");
			if (alembicChest.getInventory().getItem(17).hasItemMeta() && alembicChest.getInventory().getItem(17).getItemMeta().getDisplayName().equals(ChatColor.RED + "In Progress")) {
				event.setCancelled(true);
				p.closeInventory();
				p.sendMessage(IN_PROGRESS_MESSAGE);
			}
		}
		
		if(clicked.getType() == Material.ENDER_PEARL) {
			event.setCancelled(true);
			p.sendMessage(ENDER_PEARL_ERROR);
		}

		if (clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName() && clicked.getItemMeta().getDisplayName().equals(ChatColor.RED + "")) {
			event.setCancelled(true);
		}

		// Implement information thing
		if (clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName() && clicked.getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Information")) {
			event.setCancelled(true);
			p.sendMessage("Information");
		}

		// Implement tutorial
		if (clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName() && clicked.getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Alembic Tutorial")) {
			event.setCancelled(true);
			p.sendMessage("You've clicked the tutorial button");
		}

		// Implement start alchemy
		if (clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName() && clicked.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Start Alchemy")) {
			event.setCancelled(true);

			if (!(event.getInventory().getHolder() instanceof Chest)) {
				p.sendMessage(ChatColor.RED + "Error. Please report. Inventory not instance of chest.");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "Alchemy started");
			AlembicHandler.startAlchemy(event.getInventory().getLocation().getBlock(), p.getName());
		}
	}

}
