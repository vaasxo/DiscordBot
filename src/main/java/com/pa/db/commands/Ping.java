package com.pa.db.commands;

import com.pa.db.CommandContext;
import com.pa.db.ICommand;
import net.dv8tion.jda.api.JDA;

public class Ping implements ICommand {

	public Ping() {
	}

	@Override
	public void handle(CommandContext context) {
		JDA jda = context.getJDA();

		jda.getRestPing().queue((ping)->context.getChannel().sendMessageFormat("Reset ping: %sms\n WS ping: %sms",
				ping, jda.getGatewayPing()).queue());
	}

	@Override
	public String getName() {
		return "Ping";
	}

	@Override
	public String getHelp() {
		return "Shows current ping from the bot to the server";
	}
}
