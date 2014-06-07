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
		if(cmd.getName().equalsIgnoreCase("createvote"))
		{
			String[] choices = args[1].split(",");
 +			String[] disallowed;
 +			if (args[2].equalsIgnoreCase("none"))
 +				disallowed = null;
 +			else
 +				disallowed = args[2].split(",");
 +			long endtime = (long) ((System.currentTimeMillis() / 1000L) + (Double.valueOf(args[0]) * 3600));
 +		  //TODO
		}
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
