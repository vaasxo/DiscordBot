package com.pa.db.commands.music;

import com.pa.db.CommandContext;
import com.pa.db.ICommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

@SuppressWarnings("ConstantConditions")
public class Skip implements ICommand {
	@Override
	public void handle(CommandContext context) {
		final TextChannel channel = context.getChannel();

		final Member self = context.getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();

		if(!selfVoiceState.inVoiceChannel()){
			channel.sendMessage("I am not in a voice channel, so I have no music to skip!!").queue();
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

		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(context.getGuild());
		final AudioPlayer audioPlayer = musicManager.audioPlayer;

		if(audioPlayer.getPlayingTrack()==null){
			channel.sendMessage("No track playing!").queue();
			return;
		}

		musicManager.scheduler.nextTrack();
		channel.sendMessage("Skipped track!").queue();
	}

	@Override
	public String getName() {
		return "Skip";
	}

	@Override
	public String getHelp() {
		return "Skips the current track in the queue";
	}
}
