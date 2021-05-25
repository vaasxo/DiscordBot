package com.pa.db;

import com.pa.db.database.SQLiteDataSource;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.EnumSet;

public class MainBot {
	public static void main(String[] args) throws LoginException, SQLException {
		SQLiteDataSource.getConnection();

		JDA jda = JDABuilder.createDefault("ODQ2MDc5NzM4MTM2MjMxOTQ2.YKqTKA.1LA4gkMxWdG6OGySHBgKYJ6GLx4",
				GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_MESSAGES,
				GatewayIntent.GUILD_VOICE_STATES).disableCache(EnumSet.of(  //we need voice_states in order to play music properly
				CacheFlag.CLIENT_STATUS,
				CacheFlag.ACTIVITY,
				CacheFlag.EMOTE
		)).enableCache(CacheFlag.VOICE_STATE).addEventListeners(new Listener())
				.setActivity(Activity.listening("to your orders!"))
				.build();
	}
}
