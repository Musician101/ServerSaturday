package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Permissions;
import com.campmongoose.serversaturday.RewardHandler;
import com.campmongoose.serversaturday.submission.Submissions;
import com.campmongoose.serversaturday.submission.Submitter;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

public abstract class ServerSaturdayCommand {

    protected final RewardHandler getRewardHandler() {
        return getPlugin().getRewardHandler();
    }

    protected final Submissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    protected final Submitter getSubmitter(Player player) {
        return getSubmissions().getSubmitter(player);
    }

    protected final boolean canUseSubmit(@Nonnull CommandSender sender) {
        return sender instanceof Player && sender.hasPermission(Permissions.SUBMIT);
    }
}
