package io.github.thepowderguy.ZweiAndPowdersVotePlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ZweiAndPowdersVotePlugin extends JavaPlugin {
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[]args)
  {
	  if (cmd.getName().equalsIgnoreCase("createvote"))
	  {
		  String[] choices = args[1].split(",");
		  String[] disallowed;
		  if (args[2].equalsIgnoreCase("none"))
			  disallowed = null;
		  else
			  disallowed = args[2].split(",");
		  long endtime = (long) ((System.currentTimeMillis() / 1000L) + (Double.valueOf(args[0]) * 3600));
		  //TODO
	  }
	  return false;
  }
}
