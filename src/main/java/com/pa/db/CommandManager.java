package com.pa.db;

import com.pa.db.commands.*;
import com.pa.db.commands.music.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {

	private final List<ICommand> commands = new ArrayList<>();

	public CommandManager() {
		addCommand(new Ping());
		addCommand(new Meme());
		addCommand(new Help(this));
		addCommand(new Kick());
		addCommand(new Webhook());
		addCommand(new Joke());
		addCommand(new SetPrefix());
		addCommand(new Join());
		addCommand(new Play());
		addCommand(new Stop());
		addCommand(new Skip());
		addCommand(new NowPlaying());
		addCommand(new Leave());
	}

	private void addCommand(ICommand cmd) {
		boolean nameFound = this.commands.stream().anyMatch(it -> it.getName().equalsIgnoreCase(cmd.getName()));
		if (nameFound)
			throw new IllegalArgumentException("Command is already on the list!");

		commands.add(cmd);
	}

	public List<ICommand> getCommands() {
		return this.commands;
	}

	@Nullable
	public ICommand getCommand(String command) {
		for (ICommand cmd : this.commands) {
			if (cmd.getName().equalsIgnoreCase(command) || cmd.getAliases().contains(command))
				return cmd;
		}

		return null;
	}

	//package-private
	void handle(GuildMessageReceivedEvent event, String prefix) {
		String[] split = event.getMessage().getContentRaw()
				.replaceFirst("(?i)" + Pattern.quote(prefix), "")
				.split("\\s+"); //get rid of prefix
		String invoke = split[0].toLowerCase(); //split[0] is the command name
		ICommand cmd = this.getCommand(invoke); //invoke the command

		if (cmd != null) {
			event.getChannel().sendTyping().queue();
			List<String> args = Arrays.asList(split).subList(1, split.length);

			CommandContext context = new CommandContext(event, args);

			cmd.handle(context);
		}
	}
}
