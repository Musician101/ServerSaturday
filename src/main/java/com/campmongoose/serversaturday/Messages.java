package com.campmongoose.serversaturday;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public interface Messages {

    String PREFIX = "[SS] ";
    Component BUILD_DOES_NOT_EXIST = text(PREFIX + "A build with that name does not exist.", RED);
    Component REWARDS_WAITING = MiniMessage.miniMessage().deserialize("<gold><click:run_command:/ss claim>" + PREFIX + "Hey, you! You have rewards waiting for you. Click this message to claim them!");
}
