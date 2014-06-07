package io.github.thepowderguy.ZweiAndPowdersVotePlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZweiAndPowdersVotePlugin extends JavaPlugin {
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new PlayerLoginHandler(this), this);
		this.saveDefaultConfig();
	}
	@Override
	public void onDisable()
	{
		this.saveConfig();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("createvote"))
		{
			if(args.length < 4)
			{
				return false;
			}
 			long endtime;
 			try {
 				endtime = (long) ((System.currentTimeMillis() / 1000L) + (Double.valueOf(args[0]) * 3600));
 			} catch (NumberFormatException e) {
 				sender.sendMessage("Invalid number");
 				return true;
 			}
			String[] choices = args[1].split(",");
 			List<String> disallowed = new ArrayList<String>();
 			if (args[2].equalsIgnoreCase("none"))
 				disallowed.add("$NONE");
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
 			return true;
		}
		if(cmd.getName().equalsIgnoreCase("vote"))
		{
			if(args.length == 0)
			{
				int currentId = getConfig().getInt("current-id"); 
				if(getConfig().getInt("votes." + currentId + ".time-end") > System.currentTimeMillis() / 1000L)
				{
					if(!sender.hasPermission("vote.canvote") || getConfig().getStringList("votes." + currentId + ".disallowed").contains(sender.getName()))
					{
						sender.sendMessage(ChatColor.DARK_RED + "(You are not allowed to participate in this vote)");
					}
					String question = getConfig().getString(
							"votes." + currentId + ".question");
					Set<String> choices = getConfig().getConfigurationSection(
							"votes." + currentId + ".choices").getKeys(false);
					sender.sendMessage(ChatColor.AQUA + "Question:");
					sender.sendMessage(ChatColor.GRAY + question);
					for (String i : choices) {
						sender.sendMessage(ChatColor.RED + "- " + i);
					}
					return true;
				}
				else
				{
					if(!sender.hasPermission("vote.canvote") || getConfig().getStringList("votes." + currentId + ".disallowed").contains(sender.getName()))
					{
						sender.sendMessage(ChatColor.DARK_RED + "You are not allowed to participate in this vote.");
						return true;
					}
					sender.sendMessage(ChatColor.RED + "There are currently no votes going on!");
					return true;
				}
			}
			int weight = 1;
			for (int i = 25; i > 0; i--)
			{
				if (sender.isPermissionSet("vote.weight." + String.valueOf(i)))
				{
					weight = i;
					break;
				}
			}
			if (this.getConfig().contains("votes." + getConfig().getInt("current-id") + ".choices." + args[0]))
			{
				int currentId = getConfig().getInt("current-id");
				Set<String> choices = getConfig().getConfigurationSection("votes." + currentId + ".choices").getKeys(false);
				for(String i : choices)
				{
					Set<String> voters = getConfig().getConfigurationSection("votes." + currentId + ".choices." + i).getKeys(false);
					if (voters.contains(sender.getName()))
					{
						this.getConfig().set("votes." + currentId + ".choices." + i + "." + sender.getName(), null);
					}
				}
				this.getConfig().set("votes." + getConfig().getInt("current-id") + ".choices." + args[0] + "." + sender.getName(), weight);
					
			}
			else
				sender.sendMessage(ChatColor.RED + "Your choice is invalid!");
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("getres"))
		{
			int currentId = getConfig().getInt("current-id");
			Set<String> choices = getConfig().getConfigurationSection("votes." + currentId + ".choices").getKeys(false);
			int choiceTotal;
			for(String i : choices)
			{
				choiceTotal = 0;
				Set<String> voters = getConfig().getConfigurationSection("votes." + currentId + ".choices." + i).getKeys(false);
				for(String j : voters)
				{
					choiceTotal += getConfig().getInt("votes." + currentId + ".choices." + i + "." + j);
				}
				sender.sendMessage(ChatColor.AQUA + i + ": " + choiceTotal + " weight points");
			}
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("bcres"))
		{
			Bukkit.broadcastMessage(ChatColor.AQUA + "==== Vote results ===");
			int currentId = getConfig().getInt("current-id");
			Set<String> choices = getConfig().getConfigurationSection("votes." + currentId + ".choices").getKeys(false);
			int choiceTotal;
			for(String i : choices)
			{
				choiceTotal = 0;
				Set<String> voters = getConfig().getConfigurationSection("votes." + currentId + ".choices." + i).getKeys(false);
				for(String j : voters)
				{
					choiceTotal += getConfig().getInt("votes." + currentId + ".choices." + i + "." + j);
				}
				Bukkit.broadcastMessage(ChatColor.AQUA + "[vote] " + i + ": " + choiceTotal + " weight points");
			}
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("endvote"))
		{
			int currentId = getConfig().getInt("current-id");
			getConfig().set("votes." + currentId + ".time-end", 0);
		}
		return false;
	}
}
