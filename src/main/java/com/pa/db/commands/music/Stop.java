package com.pa.db.commands.music;

import com.pa.db.CommandContext;
import com.pa.db.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

@SuppressWarnings("ALL")
public class Stop implements ICommand {

	@SuppressWarnings("ConstantConditions")
	@Override
	public void handle(CommandContext context) {
		final TextChannel channel = context.getChannel();

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

		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(context.getGuild());

		musicManager.scheduler.player.stopTrack();
		musicManager.scheduler.queue.clear();

		channel.sendMessage("The player has been stopped, queue cleared!").queue();
	}

	@Override
	public String getName() {
		return "Stop";
	}

	@Override
	public String getHelp() {
		return "Stops the current song and clears the queue";
	}
}
