package io.github.thepowderguy.ZweiAndPowdersVotePlugin;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerLoginHandler implements Listener {
	ZweiAndPowdersVotePlugin plugin;
	public PlayerLoginHandler(ZweiAndPowdersVotePlugin instance)
	{
		plugin = instance;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		int currentId = plugin.getConfig().getInt("current-id");
		if (!this.plugin.contains("votes." + currentId))
		{
			return;
		}
		if(plugin.getConfig().getInt("votes." + currentId + ".time-end") > System.currentTimeMillis() / 1000L)
		{
			event.getPlayer().sendMessage(ChatColor.AQUA + "There is a vote going on! The question is:");
			event.getPlayer().sendMessage(ChatColor.GRAY + plugin.getConfig().getString("votes." + currentId + ".question"));
			event.getPlayer().sendMessage(ChatColor.RED + "Do /vote to see options, and do /vote (choice) to vote!");
		}
	}
}
