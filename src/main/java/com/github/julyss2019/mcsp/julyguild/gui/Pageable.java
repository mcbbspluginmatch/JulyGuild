package com.github.julyss2019.mcsp.julyguild.gui;

public interface Pageable extends GUI {
    void nextPage(); // 下一页
    void previousPage(); // 上一页
    default boolean hasNext() {
        return getCurrentPage() < getTotalPage() - 1;
    }
    default boolean hasPrecious() {
        return getCurrentPage() > 0;
    }
    void setCurrentPage(int page); // 打开指定页
    int getTotalPage(); // 得到总数
    int getCurrentPage(); // 得到当前页数
}
