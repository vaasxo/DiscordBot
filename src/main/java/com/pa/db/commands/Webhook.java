package com.pa.db.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.pa.db.CommandContext;
import com.pa.db.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class Webhook implements ICommand {

	private final WebhookClient client;

	public Webhook() {
		WebhookClientBuilder builder = new WebhookClientBuilder("https://discord.com/api/webhooks/846680523589943377/Al8pkZhgHEZc_KcScTTQb2UqICq4J92AHGzs3QUpJ2_kRSPRFqdfuu3LeruME0qEVzyL");
		builder.setThreadFactory((job)-> {
			Thread thread = new Thread(job);
			thread.setName("Webhook-thread");
			thread.setDaemon(true);
			return thread;
		});

		this.client = builder.build();
	}

	@Override
	public void handle(CommandContext context) {
		final List<String> args = context.getArgs();
		final TextChannel channel = context.getChannel();

		if(args.isEmpty()){
			channel.sendMessage("Missing arguments!").queue();
			return;
		}

		final User user = context.getAuthor();

		WebhookMessageBuilder builder = new WebhookMessageBuilder()
				.setUsername(user.getName())
				.setAvatarUrl(user.getEffectiveAvatarUrl().replaceFirst("gif","png")+"?size=512")
				.setContent(String.join(" ",args));

		client.send(builder.build());
	}

	@Override
	public String getName() {
		return "Webhook";
	}

	@Override
	public String getHelp() {
		return "Send a message in your name using webhook!\n" +
				"Usage: `!webhook [message]`";
	}
}
