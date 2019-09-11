package com.jnucc.community.entity;

import org.springframework.context.annotation.Bean;

public class Page {
    private int current = 1;
    private String path;
    private int limit = 10;
    private int rows;

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit > 0 && limit <= 100)
            this.limit = limit;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1)
            this.current = current;
    }

    public int getTotal() {
        if (rows % limit == 0)
            return rows / limit;
        else return rows / limit + 1;
    }

    public int getOffset() {
        return (current - 1) * limit;
    }

    public int getRows() {
        return this.rows;
    }

    public int getFrom() {
        return current - 2 > 0 ? current - 2 : 1;
    }

    public int getTo() {
        return current + 2 < getTotal() ? current + 2 : getTotal();
    }
}
