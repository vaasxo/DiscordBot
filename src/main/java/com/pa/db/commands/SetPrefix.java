package com.pa.db.commands;

import com.pa.db.CommandContext;
import com.pa.db.ICommand;
import com.pa.db.database.SQLiteDataSource;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SetPrefix implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
		final List<String> args = ctx.getArgs();
		final Member member = ctx.getMember();

		if (!member.hasPermission(Permission.MANAGE_SERVER)) {
			channel.sendMessage("You must have permission to manage the server to use his command").queue();
			return;
		}

		if (args.isEmpty()) {
			channel.sendMessage("Please provide the new prefix that you want to use").queue();
			return;
		}

		final String newPrefix = String.join("", args);
		updatePrefix(ctx.getGuild().getIdLong(), newPrefix);

		channel.sendMessageFormat("New prefix has been set to `%s`", newPrefix).queue();
	}

	@Override
	public String getName() {
		return "SetPrefix";
	}

	@Override
	public String getHelp() {
		return "Sets the prefix for this server\n" +
				"Usage: `!setprefix <prefix>`";
	}

	private void updatePrefix(long guildId, String newPrefix) {

		try (final PreparedStatement preparedStatement = SQLiteDataSource
				.getConnection()
				// language=SQLite
				.prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {

			preparedStatement.setString(1, newPrefix);
			preparedStatement.setString(2, String.valueOf(guildId));

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

