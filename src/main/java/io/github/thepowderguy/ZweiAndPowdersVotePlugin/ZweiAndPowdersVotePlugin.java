package io.github.thepowderguy.ZweiAndPowdersVotePlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZweiAndPowdersVotePlugin extends JavaPlugin {
	@Override
	public void onEnable()
	{
		// We might need something here later
	}
	@Override
	public void onDisable()
	{
		// We might need something here later
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("vote"))
		{
			if(args.length == 0)
			{
				// No matter what, send person question and choices
				// if they are a disallowed player, tell them that they cannot vote
				// PS add way to store question in config.yml
			}
		}
		return false;
	}
}
