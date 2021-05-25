package com.pa.db.commands;

import com.pa.db.CommandContext;
import com.pa.db.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Kick implements ICommand {


	@Override
	public void handle(CommandContext context) {
		final TextChannel channel = context.getChannel();
		final Message message = context.getMessage();
		final Member member = context.getMember();
		final List<String> args = context.getArgs();

		if (args.size() < 2 || message.getMentionedMembers().isEmpty()) {
			channel.sendMessage("Missing arguments: no mentioned member or no reason provided").queue();
			return;
		}

		final Member target = message.getMentionedMembers().get(0);

		if (!member.canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)) {
			channel.sendMessage("You do not have permission to kick members").queue();
			return;
		}

		final Member selfMember = context.getSelfMember();

		if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
			channel.sendMessage("I do not have permission to kick members").queue();
			return;
		}

		final String reason = String.join(" ",args.subList(1, args.size()));
		context.getGuild().kick(target,reason).reason(reason).queue(
				(__) -> channel.sendMessage("Kick successful").queue(),
				(error) -> channel.sendMessageFormat("Could not kick %s",error.getMessage()).queue()
		); //first reason is for the kick itself, second reason is for audit logs
	}

	@Override
	public String getName() {
		return "Kick";
	}

	@Override
	public String getHelp() {
		return "Kicks a member off the server.\n" +
				"Usage: `!kick <@user> <reason>`";
	}
}
