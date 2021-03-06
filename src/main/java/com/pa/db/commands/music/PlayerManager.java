package com.pa.db.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

	private static PlayerManager INSTANCE;
	private final Map<Long, GuildMusicManager> musicManagers; //maps each guild to a music manager
	private final AudioPlayerManager audioPlayerManager;

	public PlayerManager(){
		this.musicManagers = new HashMap<>();
		this.audioPlayerManager = new DefaultAudioPlayerManager();

		//teaches the player what a youtube video is
		AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
		AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
	}

	public GuildMusicManager getMusicManager(Guild guild){
		return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId)->{
			final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

			guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

			return guildMusicManager;
		});
	}

	//handles the reading and replying of/to play commands
	public void loadAndPlay(TextChannel channel, String trackUrl){
		final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

		this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				musicManager.scheduler.queue(track);

				channel.sendMessage("Adding to queue: `")
						.append(track.getInfo().title)
						.append("` by `")
						.append(track.getInfo().author)
						.append("\n")
						.append(track.getInfo().uri)
						.append("`")
						.queue();
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				final List<AudioTrack> tracks=playlist.getTracks();

				channel.sendMessage("Adding to queue: `")
						.append(String.valueOf(tracks.size()))
						.append("` tracks from playlist `")
						.append(playlist.getName())
						.append("`")
						.queue();

				for(AudioTrack track:tracks){
					musicManager.scheduler.queue(track);
				}
			}

			@Override
			public void noMatches() {
				channel.sendMessage("No matches found for your search :(").queue();
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				channel.sendMessage("Error loading the track. Blame my programmer, not me!").queue();
			}
		});
	}

	public static PlayerManager getInstance() {
		if(INSTANCE == null){
			INSTANCE = new PlayerManager();
		}

		return INSTANCE;
	}
}
