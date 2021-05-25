package com.pa.db.commands;

import com.pa.db.CommandContext;
import com.pa.db.CommandManager;
import com.pa.db.ICommand;
import com.pa.db.database.SQLiteDataSource;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Help implements ICommand {

	private final CommandManager manager;

	public Help(CommandManager manager) {
		this.manager=manager;
	}
	@Override
	public void handle(CommandContext context) {
		List<String> args= context.getArgs();
		TextChannel channel = context.getChannel();
		long guildID = context.getGuild().getIdLong();

		if(args.isEmpty()){
			StringBuilder builder = new StringBuilder();

			builder.append("List of commands\n");

			String prefix = getPrefix(guildID);
			manager.getCommands().stream().map(ICommand::getName).forEach(
					it-> builder.append("`").append(prefix).append(it).append("`\n")
			);

			channel.sendMessage(builder.toString()).queue();
			return;
		}

		String search = args.get(0);
		ICommand command = manager.getCommand(search);

		if (command==null){
			channel.sendMessage("No info found for " +search).queue();
			return;
		}

		channel.sendMessage(command.getHelp()).queue();
	}

	@Override
	public String getName() {
		return "Help";
	}

	@Override
	public String getHelp() {
		return "shows information about all of the available commands";
	}

	@Override
	public List<String> getAliases(){
		return Arrays.asList("commands", "cmd", "vommands", "comenzi");
	}

	private String getPrefix(long guildID){
		try (final PreparedStatement preparedStatement = SQLiteDataSource
				.getConnection()
				// language=SQLite
				.prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")) {

			preparedStatement.setString(1, String.valueOf(guildID));

			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				return resultSet.getString("prefix");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "!";
	}
}
