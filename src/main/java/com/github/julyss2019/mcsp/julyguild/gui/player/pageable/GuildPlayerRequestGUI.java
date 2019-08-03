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
import com.github.julyss2019.mcsp.julyguild.util.Util;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryListener;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.SkullItemBuilder;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class GuildPlayerRequestGUI extends BasePageableGUI {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private Inventory inventory;
    private Guild guild;
    private List<JoinGuildRequest> joinGuildRequests = new ArrayList<>();

    public GuildPlayerRequestGUI(GuildPlayer guildPlayer) {
        super(GUIType.PLAYER_JOIN_REQUEST, guildPlayer);

        this.guild = this.guildPlayer.getGuild();
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
                            GuildPlayer requestGuildPlayer = joinRequest.getRequester();

                            String guildName = guild.getName();
                            String requesterName = requestGuildPlayer.getName();

                            if (requestGuildPlayer.isInGuild()) {
                                guild.removeRequest(joinRequest);
                                Util.sendColoredMessage(bukkitPlayer, "&c请求已失效: &e" + requesterName + "&c.");
                            }

                            // 左键
                            if (action == InventoryAction.PICKUP_ALL) {
                                guild.removeRequest(joinRequest);
                                guild.addMember(joinRequest.getRequester());

                                Util.sendColoredMessage(bukkitPlayer, "&b审批通过: &e" + requestGuildPlayer.getName() + "&a.");

                                if (requestGuildPlayer.isOnline()) {
                                    Util.sendColoredMessage(requestGuildPlayer.getBukkitPlayer(), "&d恭喜你通过审核, 正式成为 &e" + guildName + " &d宗门会员!");
                                }
                            } else if (action == InventoryAction.PICKUP_HALF) { // 右键
                                guild.removeRequest(joinRequest);
                                Util.sendColoredMessage(bukkitPlayer, "&c审批拒绝: &e" + requestGuildPlayer.getName() + "&c.");

                                if (requestGuildPlayer.isOnline()) {
                                    Util.sendColoredMessage(requestGuildPlayer.getBukkitPlayer(), "&c你在 &e" + guildName  + " &c宗门的申请请求被拒绝.");
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
                        // 不应在 InventoryClickEvent 中开启或关闭背包 —— 754503921
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
            GuildPlayer guildPlayer = guildRequest.getRequester();
            String playerName = guildPlayer.getName();

            inventoryBuilder.item(i, new SkullItemBuilder()
                    .owner(playerName)
                    .displayName("&f" + playerName)
                    .addLore("&a左键 &b▹ &a同意")
                    .addLore("&c右键 &b▹ &c拒绝")
                    .addLore("")
                    .addLore("&9申请时间 &b▹ &9" + Util.YMDHM_SDF.format(guildRequest.getCreationTime()))
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
