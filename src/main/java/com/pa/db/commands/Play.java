package com.pa.db.commands;

import com.pa.db.CommandContext;
import com.pa.db.ICommand;
import com.pa.db.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class Play implements ICommand {
	@Override
	public void handle(CommandContext context) {
		final TextChannel channel = context.getChannel();
		final Member self = context.getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();

		final Member member = context.getMember();
		final GuildVoiceState memberVoiceState = member.getVoiceState();

		assert memberVoiceState != null;
		if(!memberVoiceState.inVoiceChannel()) {
			channel.sendMessage("You need to be in a voice channel to play music").queue();
			return;
		}

		final AudioManager audioManager = context.getGuild().getAudioManager();
		final VoiceChannel memberChannel = memberVoiceState.getChannel();

		audioManager.openAudioConnection(memberChannel);

		if(context.getArgs().isEmpty()) {
			channel.sendMessage("Correct usage is `!play <youtube link>`").queue();
			return;
		}

		assert selfVoiceState != null;
		if (!Objects.equals(memberVoiceState.getChannel(), selfVoiceState.getChannel())) {
			channel.sendMessage("You need to be in the same voice channel as me for this to work").queue();
			return;
		}

		String link = String.join(" ", context.getArgs());

		if (!isUrl(link)) {
			link = "ytsearch:" + link;
		}

		PlayerManager.getInstance().loadAndPlay(channel, link);
	}

	@Override
	public String getName() {
		return "play";
	}

	@Override
	public String getHelp() {
		return "this command is used to play music. In order to use it, you must provide a valid URL";
	}

	private boolean isUrl(String url) {
		try {
			new URI(url);
			return true;
		} catch(URISyntaxException e) {
			return false;
		}
	}
}
