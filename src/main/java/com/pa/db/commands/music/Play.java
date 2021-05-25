package com.pa.db.commands.music;

import com.pa.db.CommandContext;
import com.pa.db.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.net.URI;
import java.net.URISyntaxException;

public class Play implements ICommand {
	@SuppressWarnings("ConstantConditions")
	@Override
	public void handle(CommandContext context) {
		final TextChannel channel = context.getChannel();

		if(context.getArgs().isEmpty()){
			channel.sendMessage("Correct usage is `!play <youtube link>`!").queue();
			return;
		}
		final Member self = context.getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();

		if(!selfVoiceState.inVoiceChannel()){
			channel.sendMessage("Please use the Join command first to get me into the voice channel!").queue();
			return;
		}

		final Member member = context.getMember();
		final GuildVoiceState memberVoiceState = member.getVoiceState();

		if(!memberVoiceState.inVoiceChannel()){
			channel.sendMessage("Please join a voice channel before calling this command!").queue();
			return;
		}

		if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
			channel.sendMessage("We need to be in the same channel for this to work!").queue();
			return;
		}

		String link = String.join(" ", context.getArgs());

		if(!isUrl(link)){
			link="ytsearch:"+link;
		}

		PlayerManager.getInstance()
				.loadAndPlay(channel, link);
	}

	@Override
	public String getName() {
		return "Play";
	}

	@Override
	public String getHelp() {
		return "Plays a song in the current voice channel\n" +
				"Usage: `!play <youtube link>`";
	}

	private boolean isUrl(String url){
		try{
			new URI(url);
			return true;
		}catch(URISyntaxException e){
			return false;
		}
	}
}
