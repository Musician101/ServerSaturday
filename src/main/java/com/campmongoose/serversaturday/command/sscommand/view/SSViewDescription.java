package com.campmongoose.serversaturday.command.sscommand.view;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.UUIDCacheException;
import java.util.Arrays;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.EnumHand;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class SSViewDescription extends AbstractCommand {

    public SSViewDescription() {
        super("viewdescription", "View the description of a build.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("viewdescription"), new CommandArgument("player", Syntax.REQUIRED, Syntax.REPLACE), new CommandArgument("build", Syntax.REQUIRED, Syntax.REPLACE)), 2, "ss.view", true);
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
        try {
            Submitter submitter = getSubmitter(args[0]);
            if (submitter == null) {
                player.sendMessage(ChatColor.RED + Reference.PREFIX + "Could not find a player with that name.");
                return false;
            }

            Build build = submitter.getBuild(StringUtils.join(moveArguments(args), " "));
            if (build == null) {
                player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name does not exist.");
                return false;
            }

            EntityPlayer ep = ((CraftPlayer) player).getHandle();
            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bookMeta = (BookMeta) book.getItemMeta();
            bookMeta.setAuthor(submitter.getName());
            bookMeta.setPages(build.getDescription());
            bookMeta.setTitle(build.getName());
            book.setItemMeta(bookMeta);

            ItemStack old = player.getInventory().getItemInMainHand();
            player.getInventory().setItemInMainHand(book);
            ep.a(CraftItemStack.asNMSCopy(book), EnumHand.MAIN_HAND);
            player.getInventory().setItemInMainHand(old);
            return false;
        }
        catch (UUIDCacheException e) {
            player.sendMessage(e.getMessage());
            return false;
        }
    }
}
