package com.campmongoose.serversaturday.spigot.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.gui.chest.ChestGUIs;
import com.campmongoose.serversaturday.spigot.command.SpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.command.Syntax;
import com.campmongoose.serversaturday.spigot.gui.chest.SpigotChestGUI;
import com.campmongoose.serversaturday.spigot.gui.chest.SpigotChestGUIBuilder;
import com.campmongoose.serversaturday.spigot.gui.chest.SpigotChestGUIs;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SSEdit extends SpigotCommand {

    public SSEdit() {
        super(Commands.EDIT_NAME, Commands.EDIT_DESC);
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD + Commands.EDIT_NAME), new SpigotCommandArgument(Commands.BUILD, Syntax.REPLACE, Syntax.OPTIONAL)));
        permissions = new SpigotCommandPermissions(Permissions.SUBMIT, true);
        executor = (sender, args) -> {
            Player player = (Player) sender;
            SpigotSubmitter submitter = getSubmitter(player);
            ChestGUIs<SpigotChestGUIBuilder, ClickType, SpigotChestGUI, Inventory, SpigotBuild, Location, Player, ItemStack, String, SpigotSubmitter> guis = SpigotChestGUIs.INSTANCE;
            if (!args.isEmpty()) {
                String name = StringUtils.join(args, " ");
                SpigotBuild build = submitter.getBuild(name);
                if (build == null) {
                    player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
                    return false;
                }

                guis.editBuild(build, submitter, player, null);
                return true;
            }

            guis.submitter(1, player, submitter, null);
            return true;
        };
    }
}
