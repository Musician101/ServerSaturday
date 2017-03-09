package com.campmongoose.serversaturday.spigot.command.sscommand.view;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument.Syntax;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import net.minecraft.server.v1_10_R1.EnumHand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class SSViewDescription extends AbstractSpigotCommand {

    public SSViewDescription() {
        super(Commands.VIEW_DESCRIPTION_NAME, Commands.VIEW_DESCRIPTION_DESC, new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD), new SpigotCommandArgument(Commands.VIEW_DESCRIPTION_NAME), new SpigotCommandArgument(Commands.PLAYER, Syntax.REQUIRED, Syntax.REPLACE), new SpigotCommandArgument(Commands.BUILD, Syntax.REQUIRED, Syntax.REPLACE)), 2), new SpigotCommandPermissions(Permissions.VIEW, true));
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args) {
        if (!testPermission(sender)) {
            return false;
        }

        if (!minArgsMet(sender, args.length)) {
            return false;
        }

        Player player = (Player) sender;
        SpigotSubmitter submitter = getSubmitter(args[0]);
        if (submitter == null) {
            player.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_FOUND);
            return false;
        }

        SpigotBuild build = submitter.getBuild(combineStringArray(moveArguments(args)));
        if (build == null) {
            player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
            return false;
        }

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor(submitter.getName());
        bookMeta.setPages(build.getDescription());
        bookMeta.setTitle(build.getName());
        book.setItemMeta(bookMeta);
        ItemStack old = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(book);
        ((CraftPlayer) player).getHandle().a(CraftItemStack.asNMSCopy(book), EnumHand.MAIN_HAND);
        player.getInventory().setItemInMainHand(old);

        return true;
    }
}