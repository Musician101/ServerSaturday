package com.campmongoose.serversaturday.paper.command;

import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.paper.PaperRewardHandler;
import com.campmongoose.serversaturday.paper.submission.PaperSubmissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.campmongoose.serversaturday.paper.PaperServerSaturday.getPlugin;

public abstract class ServerSaturdayCommand {

    protected final PaperRewardHandler getRewardHandler() {
        return getPlugin().getRewardHandler();
    }

    protected final PaperSubmissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    protected final Submitter getSubmitter(Player player) {
        return getSubmissions().getSubmitter(player.getUniqueId());
    }

    protected final boolean canUseSubmit(@NotNull CommandSender sender) {
        return sender instanceof Player && sender.hasPermission(Permissions.SUBMIT);
    }
}
