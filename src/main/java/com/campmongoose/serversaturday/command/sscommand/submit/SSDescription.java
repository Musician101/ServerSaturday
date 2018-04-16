package com.campmongoose.serversaturday.command.sscommand.submit;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.menu.EditBookGUI;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SSDescription extends AbstractCommand {

    public SSDescription() {
        super("description", "Change the description of a submission.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("description"), new CommandArgument("build", Syntax.REQUIRED, Syntax.REPLACE)), 1, "ss.submit", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args) {
        if (!canSenderUseCommand(sender)) {
            return false;
        }

        if (!minArgsMet(sender, args.length)) {
            return false;
        }

        Player player = (Player) sender;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "You need an empty hand in order to run this command.");
            return false;
        }

        EditBookGUI gui = getPluginInstance().getDescriptionGUI();
        if (gui.containsPlayer(player)) {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "You're in the middle of editing another build.");
            return false;
        }

        String name = StringUtils.join(args, " ");
        Submitter submitter = getSubmitter(player);
        if (submitter.getBuild(name) == null) {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name does not exist.");
            return false;
        }

        Build build = submitter.getBuild(name);
        gui.add(player, build);
        player.sendMessage(ChatColor.GOLD + Reference.PREFIX + "Open the book to edit the description for your build.");
        player.sendMessage(ChatColor.GOLD + Reference.PREFIX + "If you change your mind, then just sign the book anyway.");
        return true;
    }
}
