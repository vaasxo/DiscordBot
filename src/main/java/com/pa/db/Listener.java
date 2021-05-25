package com.pa.db;

import com.pa.db.database.SQLiteDataSource;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Listener extends ListenerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
	private final CommandManager manager = new CommandManager();
	String prefix = "!";

	@Override
	public void onReady(@NotNull ReadyEvent event) {
		super.onReady(event);
		LOGGER.info("{} is ready", event.getJDA().getSelfUser());
		//shows a message stating that the bot is ready while running the bot
	}

	@Override
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);

		User user = event.getAuthor();

		if (user.isBot() || event.isWebhookMessage()) {
			return;
		}

		final long guildID=event.getGuild().getIdLong();
		prefix = getPrefix(guildID);
		String raw = event.getMessage().getContentRaw();

		if (raw.equalsIgnoreCase(prefix + "shutdown") && user.getId().equals("332579370520674325")) {
			LOGGER.info("Shutdown");
			event.getJDA().shutdown();
			BotCommons.shutdown(event.getJDA());
		} else if (raw.equalsIgnoreCase(prefix + "info")) {
			EmbedBuilder info = new EmbedBuilder();
			info.setTitle("Bot Information");
			info.setDescription("bot created as a project for the Advanced Programming class");
			info.addField("Creator", "Radu", false);
			info.setColor(0xffffff);

			event.getChannel().sendMessage(info.build()).queue();
		}

		if (raw.startsWith(prefix)) {
			manager.handle(event, prefix);
		}
	}

	private String getPrefix(long guildID) {
		try (final PreparedStatement preparedStatement = SQLiteDataSource
				.getConnection()
				//language=SQLite
				.prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id=?")) {
			preparedStatement.setString(1,String.valueOf(guildID));

			try(final ResultSet resultSet = preparedStatement.executeQuery()){
				if(resultSet.next()){
					return resultSet.getString("prefix");
				}
			}

			try(final PreparedStatement insertStatement = SQLiteDataSource
					.getConnection()
					//language=SQLite
					.prepareStatement("INSERT INTO guild_settings(guild_id) VALUES (?)")){
				insertStatement.setString(1,String.valueOf(guildID));

				insertStatement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "!";
	}
}
