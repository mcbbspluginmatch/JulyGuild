package com.github.julyss2019.mcsp.julyguild.gui;

import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import org.bukkit.inventory.Inventory;

/**
 * 基础GUI，实现了换页等功能
 */
public class BasePageableGUI extends BaseGUI implements Pageable {
    private int currentPage;

    public BasePageableGUI(GUIType guiType, GuildPlayer guildPlayer) {
        super(guiType, guildPlayer);
    }

    @Override
    public void build() {
        setCurrentPage(0);
    }

    @Override
    public void nextPage() {
        if (!hasNext()) {
            throw new IllegalArgumentException("没有下一页了");
        }

        currentPage++;
        setCurrentPage(currentPage);
    }

    @Override
    public void previousPage() {
        if (!hasPrecious()) {
            throw new IllegalArgumentException("没有上一页了");
        }

        currentPage--;
        setCurrentPage(currentPage);
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    // 需要子类来覆盖
    @Override
    public void setCurrentPage(int page) {
        if (page < 0 || page > getTotalPage()) {
            throw new IllegalArgumentException("页数不合法: " + page);
        }
    }

    // 需要子类来覆盖
    @Override
    public int getTotalPage() {
        return 0;
    }
}