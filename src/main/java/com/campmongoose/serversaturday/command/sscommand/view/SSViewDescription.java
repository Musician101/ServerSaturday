package com.campmongoose.serversaturday.command.sscommand.view;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.UUIDUtils;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.IOException;
import java.util.Arrays;

public class SSViewDescription extends AbstractCommand
{
    public SSViewDescription(ServerSaturday plugin)
    {
        super(plugin, "viewdescription", "View the description of a build.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("viewdescription"), new CommandArgument("player", Syntax.REQUIRED, Syntax.REPLACE), new CommandArgument("build", Syntax.REQUIRED, Syntax.REPLACE)), 2, "ss.view", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        if (!canSenderUseCommand(sender))
            return false;

        if (!minArgsMet(sender, args.length))
            return false;

        Player player = (Player) sender;
        Submitter submitter = null;
        try
        {
            submitter = plugin.getSubmissions().getSubmitter(UUIDUtils.getUUIDOf(args[0]));
        }
        catch (IOException e)
        {
            for (Submitter s : plugin.getSubmissions().getSubmitters())
                if (s.getName().equalsIgnoreCase(args[0]))
                    submitter = s;
        }

        if (submitter == null)
        {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "Could not find a player with that name.");
            return false;
        }

        Build build = submitter.getBuild(args[1]);
        if (build == null)
        {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name does not exist.");
            return false;
        }

        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        Submitter finalSubmitter = submitter;
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK)
        {
            {
                BookMeta bookMeta = (BookMeta) getItemMeta();
                bookMeta.setAuthor(finalSubmitter.getName());
                bookMeta.setPages(build.getDescription());
                bookMeta.setTitle(build.getName());
                setItemMeta(bookMeta);
            }
        };

        ItemStack old = player.getItemInHand();
        player.setItemInHand(book);
        ep.openBook(CraftItemStack.asNMSCopy(book));
        player.setItemInHand(old);
        return false;
    }
}
