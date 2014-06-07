package io.github.thepowderguy.ZweiAndPowdersVotePlugin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
			if(args.length < 4)
			{
				//sender sendmessage commandsyntax
				return true;
			}
 			long endtime;
 			try {
 				endtime = (long) ((System.currentTimeMillis() / 1000L) + (Double.valueOf(args[0]) * 3600));
 			} catch (NumberFormatException e) {
 				sender.sendMessage("Invalid numbutt");
 				return true;
 			}
			String[] choices = args[1].split(",");
 			List<String> disallowed;
 			if (args[2].equalsIgnoreCase("none"))
 				disallowed = null;
 			else
 				disallowed = Arrays.asList(args[2].split(","));
 			StringBuilder tempQuestion = new StringBuilder();
 			String question;
 			for (int i = 3; i < args.length; i++)
 			{
 				tempQuestion.append(args[i]);
 				tempQuestion.append(' ');
 			}
 			question = tempQuestion.substring(0, tempQuestion.length() - 2);
 			
 			
 			
 			String current = String.valueOf(this.getConfig().getInt("current-id") + 1);
 			this.getConfig().set("votes." + current + ".question", question);
 			this.getConfig().set("votes." + current + ".time-end", endtime);
 			this.getConfig().set("votes." + current + ".disallowed", disallowed);
 			for (String i : choices)
 			{
 				this.getConfig().createSection("votes." + current + ".choices." + i);
 			}
 			this.getConfig().set("current-id", this.getConfig().getInt("current-id") + 1);
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



class endOfVoteChecker extends BukkitRunnable
{
	public void run()
	{
		//check if vote is coming to an end
	}
}
