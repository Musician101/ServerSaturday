package com.campmongoose.serversaturday.sponge.submission;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.AbstractSubmissions;
import com.campmongoose.serversaturday.sponge.menu.chest.SubmissionsMenu;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.inventory.custom.CustomInventory;
import org.spongepowered.api.text.translation.FixedTranslation;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class SpigotSubmissions extends AbstractSubmissions<SpongeSubmitter>
{
    public SpigotSubmissions()
    {
        super(new File("config/" + Reference.NAME + "/submitters"));
    }

    @Override
    public SpongeSubmitter getSubmitter(UUID uuid)
    {
        Server server = Sponge.getServer();
        if (!submitters.containsKey(uuid))
            //noinspection OptionalGetWithoutIsPresent
            submitters.put(uuid, SpongeSubmitter.of(server.getGameProfileManager().createProfile(uuid, server.getPlayer(uuid).get().getName())));

        return submitters.get(uuid);
    }

    @Override
    public void openMenu(int page, UUID uuid)
    {
        new SubmissionsMenu(page, CustomInventory.builder().size(54).name(new FixedTranslation(MenuText.SUBMISSIONS)).build(), uuid).open(uuid);
    }

    @Override
    public void load()
    {
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        //noinspection ConstantConditions
        for (File file : dir.listFiles())//NOSONAR
        {
            if (!file.getName().endsWith(Config.HOCON_EXT))
                continue;

            ConfigurationNode cn;
            try
            {
                cn = HoconConfigurationLoader.builder().setFile(file).build().load();
            }
            catch (IOException e)//NOSONAR
            {
                LoggerFactory.getLogger(Reference.ID).error(Messages.ioException(file));
                continue;
            }

            UUID uuid = UUID.fromString(file.getName().replace(Config.HOCON_EXT, ""));
            submitters.put(uuid, SpongeSubmitter.of(uuid, cn));
        }
    }

    @Override
    public void save()
    {
        for (UUID uuid : submitters.keySet())
            getSubmitter(uuid).save(new File(dir, Config.getYAMLFileName(uuid)));
    }
}
