package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.event.ClickEvent.callback;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextColor.fromHexString;

public class TextGUI {

    private TextGUI() {

    }

    public static void displayAllSubmissions(@NotNull Player player, int page) {
        display("All S. S. Submissions", player, page, getPlugin().getSubmissions().getSubmitters().stream().flatMap(submitter -> submitter.getBuilds().stream().filter(build -> !build.featured() && build.submitted()).map(build -> getAllComponent(submitter, build))).toList(), TextGUI::displayAllSubmissions);
    }

    private static Component getAllComponent(Submitter submitter, Build build) {
        return text(build.getName(), GOLD).hoverEvent(HoverEvent.showText(text("by " + submitter.getName(), fromHexString("#BDB76B")))).clickEvent(callback(a -> BuildGUI.open(build, submitter, (Player) a)));
    }

    private static void display(@NotNull String headerString, @NotNull Player player, int page, @NotNull List<Component> content, @NotNull BiConsumer<Player, Integer> pageAction) {
        Component header = textOfChildren(text("> ===== ", DARK_GREEN), text(headerString, GREEN), text(" ===== <", DARK_GREEN));
        player.sendMessage(header);
        Component leftArrow = text((page == 1 ? "" : "<- ")).clickEvent(callback(audience -> pageAction.accept((Player) audience, page - 1))).hoverEvent(HoverEvent.showText(text("Previous Page")));
        int maxPage = Math.max(1, (int) Math.ceil(content.size() / 10d));
        Component pageText = text("Page", GREEN);
        Component rightArrow = text(page >= maxPage ? "" : " ->").clickEvent(callback(audience -> pageAction.accept((Player) audience, page + 1))).hoverEvent(HoverEvent.showText(text("Next Page")));
        Component pageNumber = text(" " + page + "/" + maxPage);
        int index = (page - 1) * 10;
        content.subList(index, Math.min(index + 10, content.size())).forEach(player::sendMessage);
        player.sendMessage(textOfChildren(leftArrow, pageText, rightArrow, pageNumber));
    }

    private static Component getAllComponent(Submitter submitter) {
        return text(submitter.getName(), GOLD).hoverEvent(HoverEvent.showText(text(submitter.getBuilds().size() + " builds.", fromHexString("#BDB76B")))).clickEvent(callback(a -> displaySubmitter((Player) a, submitter, 1)));
    }

    public static void displaySubmitters(@NotNull Player player, int page) {
        display("S. S. Submitters", player, page, getPlugin().getSubmissions().getSubmitters().stream().map(TextGUI::getAllComponent).collect(Collectors.toList()), TextGUI::displaySubmitters);
    }

    private static Component getViewComponent(Submitter submitter, Build build) {
        return text(build.getName(), build.submitted() && !build.featured() ? GREEN : RED).clickEvent(callback(a -> BuildGUI.open(build, submitter, (Player) a)));
    }

    public static void displaySubmitter(@NotNull Player player, @NotNull Submitter submitter, int page) {
        display(submitter.getName() + "'s Builds", player, page, submitter.getBuilds().stream().map(b -> getViewComponent(submitter, b)).collect(Collectors.toList()), (p, i) -> displaySubmitter(p, submitter, i));
    }
}
