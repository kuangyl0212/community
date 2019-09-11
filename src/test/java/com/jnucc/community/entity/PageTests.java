package com.jnucc.community.entity;

import org.junit.Test;;

import static org.junit.Assert.*;

public class PageTests {

    private Page page;

    @Test
    public void testGetLimit() {
        page = new Page();
        assertEquals(10, page.getLimit());
    }

    @Test
    public void testGetOffset() {
        page = new Page();
        assertEquals(0, page.getOffset());
        page.setCurrent(2);
        assertEquals(10, page.getOffset());
    }

    @Test
    public void testRows() {
        page = new Page();
        page.setRows(10);
        assertEquals(10, page.getRows());
    }

    @Test
    public void testTotal() {
        page = new Page();
        page.setRows(150);
        assertEquals(15, page.getTotal());
    }

    @Test
    public void testGetFromAndTo() {
        page = new Page();
        page.setRows(35);
        assertEquals(1, page.getFrom());
        assertEquals(3, page.getTo());
    }
}
