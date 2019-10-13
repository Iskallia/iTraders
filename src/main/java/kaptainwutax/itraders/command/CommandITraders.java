package kaptainwutax.itraders.command;

import kaptainwutax.itraders.init.InitConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CommandITraders extends CommandBase {

	@Override
	public String getName() {
		return "itraders";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "itraders";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();

		if (args.length > 0) {
			String subcommand = args[0];

			if ("config".equals(subcommand)) {
				this.executeConfig(server, sender, args);
				return;
			} else {
				throw new CommandException("Unknown command [" + subcommand + "].", new Object[0]);
			}
		}

		throw new CommandException("Unknown iTraders command.", new Object[0]);
	}

	private void executeConfig(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 1) {
			String task = args[1];

			if ("reload".equals(task)) {
				InitConfig.registerConfigs();
				sender.sendMessage(new TextComponentString("Configs sucessfully reloaded."));
				return;
			} else {
				throw new CommandException("Unknown task [" + task + "].", new Object[0]);
			}
		}

		throw new CommandException("Config command needs a task.", new Object[0]);
	}

}
