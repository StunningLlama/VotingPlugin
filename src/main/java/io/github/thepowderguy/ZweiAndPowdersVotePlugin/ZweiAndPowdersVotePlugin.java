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
			if(getConfig().getInt("votes." + getConfig().getInt("current-id") + ".time-end") > System.currentTimeMillis() / 1000L)
			{
				sender.sendMessage(ChatColor.RED + "Error: a vote is already in progress. Please stop the current vote to continue.");
				return true;
			}
 			long endtime;
 			try {
 				endtime = (long) ((System.currentTimeMillis() / 1000L) + (Double.valueOf(args[0]) * 3600));
 			} catch (NumberFormatException e) {
 				sender.sendMessage(ChatColor.RED + "Invalid number");
 				return true;
 			}
			String[] choices = args[1].split(",");
 			List<String> disallowed = new ArrayList<String>();
 			if (args[2].equalsIgnoreCase("none"))
 				disallowed.add("$NONE");
 			else
 				disallowed = Arrays.asList(args[2].split(","));
 			try {
				if((Integer.valueOf(args[3]) == 0) || (Integer.valueOf(args[3]) == 1))
				{
					getConfig().set("votes." + getConfig().getInt("current-id") + ".use-weight", Integer.valueOf(args[3]));
				}
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "Invalid number");
			}
 			StringBuilder tempQuestion = new StringBuilder();
 			String question;
 			for (int i = 4; i < args.length; i++)
 			{
 				tempQuestion.append(args[i]);
 				tempQuestion.append(' ');
 			}
 			question = tempQuestion.substring(0, tempQuestion.length() - 1);
 			
 			String current = String.valueOf(this.getConfig().getInt("current-id") + 1);
 			this.getConfig().set("votes." + current + ".question", question);
 			this.getConfig().set("votes." + current + ".time-end", endtime);
 			this.getConfig().set("votes." + current + ".disallowed", disallowed);
 			for (String i : choices)
 			{
 				this.getConfig().createSection("votes." + current + ".choices." + i);
 			}
 			this.getConfig().set("current-id", this.getConfig().getInt("current-id") + 1);
 			this.saveConfig();
 			sender.sendMessage(ChatColor.AQUA + "Created vote!");
 			return true;
		}
		if(cmd.getName().equalsIgnoreCase("vote"))
		{
			int currentId = getConfig().getInt("current-id"); 
			if(getConfig().getInt("votes." + currentId + ".time-end") <= System.currentTimeMillis() / 1000L)
			{
				sender.sendMessage(ChatColor.RED + "There are currently no votes going on!");
				return true;
			}
			if(args.length == 0)
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
			if(!sender.hasPermission("vote.canvote") || getConfig().getStringList("votes." + currentId + ".disallowed").contains(sender.getName()))
			{
				sender.sendMessage(ChatColor.DARK_RED + "You are not allowed to participate in this vote.");
				return true;
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
				this.saveConfig();
				sender.sendMessage(ChatColor.AQUA + "Voted for " + args[0] + ".");
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Your choice is invalid!");
			}
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("getres"))
		{
			int currentId = getConfig().getInt("current-id");
			if (!this.getConfig().contains("votes." + currentId))
			{
				sender.sendMessage(ChatColor.RED + "Error: no vote data available");
			}
			Set<String> choices = getConfig().getConfigurationSection("votes." + currentId + ".choices").getKeys(false);
			sender.sendMessage(ChatColor.AQUA + "Question: " + getConfig().getString("votes." + currentId + ".question"));
			int choiceTotal;
			for(String i : choices)
			{
				choiceTotal = 0;
				Set<String> voters = getConfig().getConfigurationSection("votes." + currentId + ".choices." + i).getKeys(false);
				if (getConfig().getInt("votes." + currentId + ".use-weight") == 1)
				{
					for (String j : voters) {
						choiceTotal += getConfig().getInt(
								"votes." + currentId + ".choices." + i + "."
										+ j);
					}
					sender.sendMessage(ChatColor.AQUA + i + ": " + choiceTotal
							+ " weight points");
				}
				else
				{
					for(int j = 0; j < voters.size(); j++) { // I'm a bit sketchy about using .size() in the String<Set>. To be fixed. May be bug
						choiceTotal += 1;
					}
					sender.sendMessage(ChatColor.AQUA + i + ": " + choiceTotal + " votes");
				}
			}
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("bcres"))
		{
			int currentId = getConfig().getInt("current-id");
			if (!this.getConfig().contains("votes." + currentId))
			{
				sender.sendMessage("Error: no vote data available");
			}
			Bukkit.broadcastMessage(ChatColor.AQUA + "==== Vote results ===");
			Set<String> choices = getConfig().getConfigurationSection("votes." + currentId + ".choices").getKeys(false);
			Bukkit.broadcastMessage(ChatColor.AQUA + "Question: " + getConfig().getString("votes." + currentId + ".question"));
			int choiceTotal;
			for(String i : choices)
			{
				choiceTotal = 0;
				Set<String> voters = getConfig().getConfigurationSection("votes." + currentId + ".choices." + i).getKeys(false);
				if (getConfig().getInt("votes." + currentId + ".use-weight") == 1)
				{
					for (String j : voters) {
						choiceTotal += getConfig().getInt(
								"votes." + currentId + ".choices." + i + "."
										+ j);
					}
					Bukkit.broadcastMessage(ChatColor.AQUA + "[vote] " + i
							+ ": " + choiceTotal + " weight points");
				}
				else
				{
					for (int j = 0; j < voters.size(); j++) {
						choiceTotal += 1;
					}
					Bukkit.broadcastMessage(ChatColor.AQUA + "[vote] " + i
							+ ": " + choiceTotal + " votes");
				}
			}
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("endvote"))
		{
			int currentId = getConfig().getInt("current-id");
			getConfig().set("votes." + currentId + ".time-end", 0);
			this.saveConfig();
			sender.sendMessage(ChatColor.AQUA + "Ended current vote.");
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("extendtime"))
		{
			int currentId = getConfig().getInt("current-id");
			if (args.length > 0) {
				long endtime;
				try {
					endtime = (long) (getConfig().getInt(
							"votes." + getConfig().getInt("current-id")
									+ ".time-end") + (Double.valueOf(args[0]) * 3600));
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.RED + "Invalid number");
					return true;
				}
				this.getConfig().set("votes." + currentId + ".time-end",
						endtime);
				this.saveConfig();
				sender.sendMessage(ChatColor.AQUA + "Increased vote duration.");
			}
			else
			{
				sender.sendMessage(ChatColor.AQUA + "/extendtime [Hours]");
			}
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("dumpvotes"))
		{
			for (String i : this.getConfig().getConfigurationSection("votes").getKeys(false))
				this.getConfig().set("votes." + i, null);
			this.getConfig().set("current-id", 0);
			this.saveConfig();
			sender.sendMessage(ChatColor.AQUA + "Dumped all vote info.");
			return true;
		}
		return false;
	}
}
