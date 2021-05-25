package com.pa.db.commands.music;

import com.pa.db.CommandContext;
import com.pa.db.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

@SuppressWarnings("ALL")
public class Leave implements ICommand {

	@Override
	public void handle(CommandContext context) {
		final TextChannel channel = context.getChannel();

		final Member self = context.getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();

		if(!selfVoiceState.inVoiceChannel()){
			channel.sendMessage("I am not in a channel, so I can't leave one!").queue();
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

		final AudioManager audioManager = context.getGuild().getAudioManager();
		audioManager.closeAudioConnection();
		channel.sendMessage("Leaving the voice channel...").queue();

	}

	@Override
	public String getName() {
		return "Leave";
	}

	@Override
	public String getHelp() {
		return "leaves the voice channel that the bot is in";
	}
}
