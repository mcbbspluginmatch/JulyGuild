package com.github.julyss2019.mcsp.julyguild.gui.player.pageable;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.gui.BasePageableGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.gui.player.GuildManageGUI;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.request.JoinGuildRequest;
import com.github.julyss2019.mcsp.julyguild.guild.request.GuildRequest;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;
import com.github.julyss2019.mcsp.julyguild.util.Util;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryListener;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.SkullItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GuildPlayerRequestGUI extends BasePageableGUI {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private Inventory inventory;
    private Guild guild;
    private List<JoinGuildRequest> joinGuildRequests = new ArrayList<>();

    public GuildPlayerRequestGUI(GuildPlayer guildPlayer) {
        super(GUIType.PLAYER_JOIN_REQUEST, guildPlayer);

        this.guild = offlineGuildPlayer.getGuild();
        setCurrentPage(0);
    }

    @Override
    public void setCurrentPage(int page) {
        super.setCurrentPage(page);

        joinGuildRequests.clear();

        for (GuildRequest request : guild.getRequests()) {
            if (request instanceof JoinGuildRequest && !request.isTimeout()) {
                joinGuildRequests.add((JoinGuildRequest) request);
            }
        }

        joinGuildRequests.sort((o1, o2) -> o1.getCreationTime() < o2.getCreationTime() ? -1 : 0);

        InventoryBuilder inventoryBuilder = new InventoryBuilder().title("&e&l玩家审批").colored().row(6)
                .listener(new InventoryListener() {
                    @Override
                    public void onClicked(InventoryClickEvent event) {
                        InventoryAction action = event.getAction();
                        int index = event.getSlot() + getCurrentPage() * 51;

                        if (index < joinGuildRequests.size()) {
                            JoinGuildRequest joinRequest = joinGuildRequests.get(index);
                            OfflineGuildPlayer requestOfflineGuildPlayer = joinRequest.getRequester();

                            String guildName = guild.getName();
                            String requesterName = requestOfflineGuildPlayer.getName();

                            if (requestOfflineGuildPlayer.isInGuild()) {
                                guild.removeRequest(joinRequest);
                                JulyMessage.sendColoredMessages(bukkitPlayer, "&c请求已失效: &e" + requesterName + "&c.");
                            }

                            // 左键
                            if (action == InventoryAction.PICKUP_ALL) {
                                guild.removeRequest(joinRequest);
                                guild.addMember(joinRequest.getRequester());

                                JulyMessage.sendColoredMessages(bukkitPlayer, "&b审批通过: &e" + requestOfflineGuildPlayer.getName() + "&a.");

                                if (requestOfflineGuildPlayer.isOnline()) {
                                    JulyMessage.sendColoredMessage(requestOfflineGuildPlayer.getBukkitPlayer(), "&d恭喜你通过审核, 正式成为 &e" + guildName + " &d宗门会员!");
                                }
                            } else if (action == InventoryAction.PICKUP_HALF) { // 右键
                                guild.removeRequest(joinRequest);
                                JulyMessage.sendColoredMessages(bukkitPlayer, "&c审批拒绝: &e" + requestOfflineGuildPlayer.getName() + "&c.");

                                if (requestOfflineGuildPlayer.isOnline()) {
                                    JulyMessage.sendColoredMessages(requestOfflineGuildPlayer.getBukkitPlayer(), "&c你在 &e" + guildName  + " &c宗门的申请请求被拒绝.");
                                }
                            }
                        }
                    }
                });

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

        int requestSize = joinGuildRequests.size();
        int itemCounter = page * 51;
        int loopCount = requestSize - itemCounter < 51 ? requestSize - itemCounter : 51;

        for (int i = 0; i < loopCount; i++) {
            GuildRequest guildRequest = joinGuildRequests.get(itemCounter++);
            OfflineGuildPlayer offlineGuildPlayer = guildRequest.getRequester();
            String playerName = offlineGuildPlayer.getName();

            inventoryBuilder.item(i, new SkullItemBuilder()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjIxYWIzMWE0MjczOWEzNTI3ZDMwNWNjOTU2YWVlNGQ2YmEzNDU1NTQzODFhNmE0YzRmZjA2YTFjMTlmZGQ0In19fQ==")
                    .displayName("&f" + playerName)
                    .addLore("&7- &a左键 &b▹ &a同意")
                    .addLore("&7- &c右键 &b▹ &c拒绝")
                    .addLore("")
                    .addLore("&7- &9申请时间 &b▹ &9" + Util.YMDHM_SDF.format(guildRequest.getCreationTime()))
                    .colored()
                    .build());
        }

        this.inventory = inventoryBuilder.build();
    }

    @Override
    public int getTotalPage() {
        int requestSize = joinGuildRequests.size();

        return requestSize == 0 ? 1 : requestSize % 51 == 0 ? requestSize / 51 : requestSize / 51 + 1;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
