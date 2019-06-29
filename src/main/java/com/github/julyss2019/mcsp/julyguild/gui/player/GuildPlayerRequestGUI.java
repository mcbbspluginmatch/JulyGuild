package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.gui.BasePageableGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.request.JoinRequest;
import com.github.julyss2019.mcsp.julyguild.guild.request.Request;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;
import com.github.julyss2019.mcsp.julyguild.util.Util;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.SkullItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GuildPlayerRequestGUI extends BasePageableGUI {
    private Inventory inventory;
    private Guild guild;
    private List<Request> requests;

    public GuildPlayerRequestGUI(GuildPlayer guildPlayer) {
        super(GUIType.PLAYER_REQUEST, guildPlayer);

        this.guild = offlineGuildPlayer.getGuild();
        build();
    }

    @Override
    public void setCurrentPage(int page) {
        InventoryBuilder inventoryBuilder = new InventoryBuilder().title("&e&l玩家审批").colored().row(6);

        inventoryBuilder
                .item(53, CommonItem.BACK, new ItemListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        close();
                        new GuildManageGUI(guildPlayer).open();
                    }
                });

        if (getTotalPage() > 1) {
            inventoryBuilder
                    .item(51, CommonItem.PREVIOUS_PAGE, new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            if (hasPrecious()) {
                                close();
                                previousPage();
                            }
                        }
                    });
            inventoryBuilder
                    .item(52, CommonItem.NEXT_PAGE, new ItemListener() {
                        @Override
                        public void onClicked(InventoryClickEvent event) {
                            if (hasNext()) {
                                close();
                                nextPage();
                            }
                        }
                    });
        }

        this.requests = guild.getRequests().stream().sorted(Comparator.comparingLong(Request::getCreationTime)).filter(request -> request instanceof JoinRequest).collect(Collectors.toList());
        int requestSize = requests.size();
        int itemCounter = page * 51;
        int loopCount = requestSize - itemCounter < 51 ? requestSize - itemCounter : 51;

        for (int i = 0; i < loopCount; i++) {
            Request request = requests.get(i);
            OfflineGuildPlayer offlineGuildPlayer = request.getOfflineGuildPlayer();
            String playerName = offlineGuildPlayer.getName();

            inventoryBuilder.item(i, new SkullItemBuilder()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjIxYWIzMWE0MjczOWEzNTI3ZDMwNWNjOTU2YWVlNGQ2YmEzNDU1NTQzODFhNmE0YzRmZjA2YTFjMTlmZGQ0In19fQ==")
                    .displayName("&f" + playerName)
                    .addLore("&7- &a左键 &b▹ &a同意")
                    .addLore("&7- &c右键 &b▹ &c拒绝")
                    .addLore("")
                    .addLore("&7- &9申请时间 &b▹ &9" + Util.YMDHM_SDF.format(request.getCreationTime()))
                    .colored()
                    .build());
        }

        this.inventory = inventoryBuilder.build();
    }

    @Override
    public int getTotalPage() {
        int requestSize = requests == null ? 0 : requests.size();

        return requestSize % 51 == 0 ? requestSize / 51 : requestSize / 51 + 1;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public List<Request> getRequests() {
        return requests;
    }
}
