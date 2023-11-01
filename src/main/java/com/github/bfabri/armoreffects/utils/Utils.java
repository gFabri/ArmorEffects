package com.github.bfabri.armoreffects.utils;

import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {
	public static String translate(String input) {
		return ChatColor.translateAlternateColorCodes('&', input.replace("<", "\u00AB").replace(">", "\u00BB"));
	}

	public static int getLevel(String s) {
		Map<Character, Integer> romanToInt = new HashMap<>();
		romanToInt.put('I', 1);
		romanToInt.put('V', 5);
		romanToInt.put('X', 10);
		romanToInt.put('L', 50);
		romanToInt.put('C', 100);
		romanToInt.put('D', 500);
		romanToInt.put('M', 1000);

		int result = 0;
		for (int i = 0; i < s.length(); i++) {
			if (i > 0 && romanToInt.get(s.charAt(i)) > romanToInt.get(s.charAt(i - 1))) {
				result += romanToInt.get(s.charAt(i)) - 2 * romanToInt.get(s.charAt(i - 1));
			} else {
				if (romanToInt.get(s.charAt(i)) == null) {
					return 0;
				}
				result += romanToInt.get(s.charAt(i));
			}
		}

		if (result > 255) {
			result = 1;
		}

		return result;
	}

	public static List<String> translate(List<String> input) {
		List<String> newInput = new ArrayList<String>();
		input.forEach(line -> newInput.add(ChatColor.translateAlternateColorCodes('&', line)));
		return newInput;
	}

	public static List<String> getCompletions(String[] args, List<String> input) {
		return getCompletions(args, input, 80);
	}

	public static List<String> getCompletions(String[] args, String... input) {
		return getCompletions(args, 80, input);
	}

	public static List<String> getCompletions(String[] args, List<String> input, int limit) {
		Preconditions.checkNotNull(args);
		Preconditions.checkArgument(args.length != 0);
		String argument = args[args.length - 1];
		return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length())).limit(limit).collect(Collectors.toList());
	}

	public static List<String> getCompletions(String[] args, int limit, String... input) {
		Preconditions.checkNotNull(args);
		Preconditions.checkArgument(args.length != 0);
		String argument = args[args.length - 1];
		return Arrays.stream(input).collect(Collectors.toList()).stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length())).limit(limit).collect(Collectors.toList());
	}
}
