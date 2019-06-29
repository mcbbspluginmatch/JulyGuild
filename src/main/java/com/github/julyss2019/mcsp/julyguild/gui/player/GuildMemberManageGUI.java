package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.gui.BasePageableGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.gui.GUIType;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildMember;
import com.github.julyss2019.mcsp.julyguild.guild.player.Permission;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.util.Util;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.item.SkullItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuildMemberManageGUI extends BasePageableGUI {
    private Inventory inventory;
    private Guild guild;
    private List<GuildMember> guildMembers = new ArrayList<>();

    public GuildMemberManageGUI(GuildPlayer guildPlayer) {
        super(GUIType.MEMBER_MANAGE, guildPlayer);

        this.guild = offlineGuildPlayer.getGuild();
        build();
    }

    @Override
    public void setCurrentPage(int page) {
        InventoryBuilder inventoryBuilder = new InventoryBuilder().title("&e&l宗门成员管理(第" + (getCurrentPage() + 1) + "页)").colored().row(6);

        inventoryBuilder.item(53, CommonItem.BACK, new ItemListener() {
            @Override
            public void onClicked(InventoryClickEvent event) {
                close();

                if (guild.isValid()) {
                    new GuildManageGUI(guildPlayer).open();
                }
            }
        });

        if (guild.getMemberCount() > 51) {
            inventoryBuilder.item(51, CommonItem.PREVIOUS_PAGE, new ItemListener() {
                @Override
                public void onClicked(InventoryClickEvent event) {
                    if (hasPrecious()) {
                        close();
                        previousPage();
                    }
                }
            });
            inventoryBuilder.item(52, CommonItem.NEXT_PAGE, new ItemListener() {
                @Override
                public void onClicked(InventoryClickEvent event) {
                    if (hasNext()) {
                        close();
                        nextPage();
                    }
                }
            });
        }


        Permission permission = guild.getMember(guildPlayer.getName()).getPermission();

        if (permission == Permission.OWNER) {
            this.guildMembers = guild.getMembers().stream().filter(guildMember -> guildMember.getPermission() == Permission.MEMBER || guildMember.getPermission() == Permission.ADMIN).collect(Collectors.toList());
        } else if (permission == Permission.ADMIN) {
            this.guildMembers = guild.getMembers().stream().filter(guildMember -> guildMember.getPermission() == Permission.MEMBER).collect(Collectors.toList());
        }

        Guild.sortMembers(guildMembers);

        int memberSize = guildMembers.size();
        int itemCounter = page * 51;
        int loopCount = memberSize - itemCounter < 51 ? memberSize - itemCounter : 51;

        for (int i = 0; i < loopCount; i++) {
            GuildMember member = guildMembers.get(i);
            ItemBuilder itemBuilder = new SkullItemBuilder()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjIxYWIzMWE0MjczOWEzNTI3ZDMwNWNjOTU2YWVlNGQ2YmEzNDU1NTQzODFhNmE0YzRmZjA2YTFjMTlmZGQ0In19fQ==")
                    .displayName("&f" + member.getName())
                    .addLore("&a▸ &c" + member.getPermission().getChineseName() + " &a◂")
                    .addLore("")
                    .addLore("&7- &d左键 &b▹ &d任命管理员")
                    .addLore("&7- &c右键 &b▹ &c移出宗门")
                    .addLore("")
                    .addLore("&7- &e金币贡献 &b▹ &e¥" + member.getDonatedBalance())
                    .addLore("&7- &a入宗时间 &b▹ &a" + Util.YMD_SDF.format(member.getJoinTime()))
                    .colored();

            inventoryBuilder.item(i, itemBuilder.build());
        }

        this.inventory = inventoryBuilder.build();
    }

    public List<GuildMember> getGuildMembers() {
        return guildMembers;
    }

    @Override
    public int getTotalPage() {
        int memberSize = guild.getMemberCount() - 1; // 去掉自己

        return memberSize % 51 == 0 ? memberSize / 51 : memberSize / 51 + 1;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
