package com.pa.db;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class MainBot {
	public static void main(String[] args) throws LoginException {

		JDA jda = JDABuilder.createDefault("ODQ2MDc5NzM4MTM2MjMxOTQ2.YKqTKA.2FfS1R5L-ApiHlYIYk6QAgwk_kc",
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
