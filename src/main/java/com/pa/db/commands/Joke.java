package com.pa.db.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.pa.db.CommandContext;
import com.pa.db.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Joke implements ICommand {
	@Override
	public void handle(CommandContext context) {
		final TextChannel channel = context.getChannel();

		WebUtils.ins.getJSONObject("https://apis.duncte123.me/joke").async(
				(json)->{
					if(!json.get("success").asBoolean()){
						channel.sendMessage("Something went wrong, please try again!").queue();
						System.out.println(json);
						return;
					}

					final JsonNode data = json.get("data");
					final String title = data.get("title").asText();
					final String url = data.get("url").asText();
					final String body = data.get("body").asText();
					final EmbedBuilder embed = EmbedUtils.defaultEmbed()
							.setTitle(title,url)
							.setDescription(body);

					channel.sendMessage(embed.build()).queue();
				}
		);
	}

	@Override
	public String getName() {
		return "Joke";
	}

	@Override
	public String getHelp() {
		return "Provides a random joke \n" +
				"Usage: `!joke`";
	}
}
