package com.github.bfabri.armoreffects.commands.utils;


import com.github.bfabri.armoreffects.commands.ArmorEffectExecutor;
import com.github.bfabri.armoreffects.commands.utils.framework.BaseCommandModule;

public class CommandsModule extends BaseCommandModule {
	public CommandsModule() {
		this.commands.add(new ArmorEffectExecutor());
	}
}