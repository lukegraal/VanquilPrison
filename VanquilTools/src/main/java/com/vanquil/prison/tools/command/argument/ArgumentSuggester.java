package com.vanquil.prison.tools.command.argument;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import java.util.List;

public interface ArgumentSuggester {
    ArgumentSuggester DEFAULT = player -> Lists.newArrayList();

    List<String> suggest(Player player);
}
