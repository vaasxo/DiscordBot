package com.pa.db;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface ICommand {

	void handle(CommandContext context);

	String getName();
	
	String getHelp();

	default List<String> getAliases(){
		return Collections.emptyList();
	}

}
