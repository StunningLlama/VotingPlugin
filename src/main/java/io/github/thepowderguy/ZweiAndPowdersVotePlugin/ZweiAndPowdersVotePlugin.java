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
				// Send line showing question based on current question in config
				// PS add a way to store current question in config
				// send player list of choices based off of a list of values in votes.n.choices
				// only do the above if player is allowed in the vote. If player is disallowed, then
				// tell them the question followed by "You cannot vote in this poll"
				// after all this, return true.
			}
		}
		return false;
	}
}
