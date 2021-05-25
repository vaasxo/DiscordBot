package com.pa.db.commands;


import com.fasterxml.jackson.databind.JsonNode;
import com.pa.db.CommandContext;
import com.pa.db.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Meme implements ICommand {

	@Override
	public void handle(CommandContext context) {
		final TextChannel channel = context.getChannel();

		WebUtils.ins.getJSONObject("https://apis.duncte123.me/meme").async(
				(json)->{
					if(!json.get("success").asBoolean()){
						channel.sendMessage("Something went wrong, please try again!").queue();
						System.out.println(json);
						return;
					}

					final JsonNode data = json.get("data");
					final String title = data.get("title").asText();
					final String url = data.get("url").asText();
					final String image = data.get("image").asText();
					final EmbedBuilder embed = EmbedUtils.embedImageWithTitle(title, url, image);

					channel.sendMessage(embed.build()).queue();
				}
		);
	}

	@Override
	public String getName() {
		return "Meme";
	}

	@Override
	public String getHelp() {
		return "Displays a random meme it fancies";
	}
}
