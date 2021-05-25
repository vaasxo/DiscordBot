package com.pa.db.commands.music;

import com.pa.db.CommandContext;
import com.pa.db.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class Join implements ICommand {

	@SuppressWarnings("ConstantConditions")
	@Override
	public void handle(CommandContext context) {
		final TextChannel channel = context.getChannel();
		final Member self = context.getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();

		//inVoiceChannel will not return null, since the cache is activated in MainBot for VoiceState
		if(selfVoiceState.inVoiceChannel()){
			channel.sendMessage("I'm already in a voice channel!").queue();
			return;
		}

		final Member member = context.getMember();
		final GuildVoiceState memberVoiceState = member.getVoiceState();

		if(!memberVoiceState.inVoiceChannel()){
			channel.sendMessage("Please join a voice channel before calling this command!").queue();
			return;
		}

		if(!self.hasPermission(Permission.VOICE_CONNECT)){
			channel.sendMessage("I do not have permission to join voice channels!").queue();
			return;
		}

		final AudioManager audioManager = context.getGuild().getAudioManager();
		final VoiceChannel memberChannel = memberVoiceState.getChannel();

		audioManager.openAudioConnection(memberChannel);
		channel.sendMessageFormat("Connecting to `\uD83D\uDD0A %s`", memberChannel.getName()).queue();
	}

	@Override
	public String getName() {
		return "Join";
	}

	@Override
	public String getHelp() {
		return "Makes the bot join the voice channel that you are currently in";
	}
}
