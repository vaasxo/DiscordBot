package com.pa.db.commands.music;

import com.pa.db.CommandContext;
import com.pa.db.ICommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

@SuppressWarnings("ConstantConditions")
public class NowPlaying implements ICommand {
	@Override
	public void handle(CommandContext context) {
		final TextChannel channel = context.getChannel();

		final Member self = context.getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();

		if(!selfVoiceState.inVoiceChannel()){
			channel.sendMessage("I am not in a voice channel, so no music is playing from me!").queue();
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
		final AudioTrack track = audioPlayer.getPlayingTrack();

		if(track==null){
			channel.sendMessage("No track playing!").queue();
			return;
		}

		final AudioTrackInfo info = track.getInfo();
		channel.sendMessageFormat("Now playing `%s` by `%s` (Link: <%s>)",info.title,info.author, info.uri).queue();
	}

	@Override
	public String getName() {
		return "NowPlaying";
	}

	@Override
	public String getHelp() {
		return "Shows the name of the current song";
	}
}
