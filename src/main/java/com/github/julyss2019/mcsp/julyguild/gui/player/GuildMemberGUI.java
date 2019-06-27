package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.gui.BasePageableGUI;
import com.github.julyss2019.mcsp.julyguild.gui.CommonItem;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.guild.player.GuildMember;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.util.Util;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.item.SkullItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class GuildMemberGUI extends BasePageableGUI {
    private Guild guild;
    private Inventory inventory;

    public GuildMemberGUI(Guild guild, GuildPlayer guildPlayer) {
        super(guildPlayer);

        this.guild = guild;
        setCurrentPage(0);
    }

    @Override
    public void setCurrentPage(int page) {
        InventoryBuilder inventoryBuilder = new InventoryBuilder().title(guild.getName() + " &7&l- &e&l成员列表").colored().row(6);

        inventoryBuilder.item(53, CommonItem.BACK, new ItemListener() {
            @Override
            public void onClicked(InventoryClickEvent event) {
                close();

                if (guild.isValid()) {
                    new GuildInfoGUI(guildPlayer, guild).open();
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

        List<GuildMember> members = guild.getMembers(true, true);
        int memberSize = members.size();
        int itemCounter = page * 51;
        int loopCount = memberSize - itemCounter < 51 ? memberSize - itemCounter : 51;

        for (int i = 0; i < loopCount; i++) {
            GuildMember member = members.get(i);

            inventoryBuilder.item(i, new SkullItemBuilder()
                    .owner(member.getName())
                    .displayName("&f" + member.getName())
                    .addLore("&a▸ &c" + member.getPermission().getChineseName() + " &a◂")
                    .addLore("")
                    .addLore("&7- &e金币贡献 &b▹ &e¥" + member.getDonateMoney())
                    .addLore("&7- &a入宗时间 &b▹ &a" + Util.SDF.format(member.getJoinTime()))
                    .colored()
                    .build());
        }

        this.inventory = inventoryBuilder.build();
    }

    @Override
    public int getTotalPage() {
        int memberSize = guild.getMembers(true, false).size();

        return memberSize % 51 == 0 ? memberSize / 51 : memberSize / 51 + 1;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
