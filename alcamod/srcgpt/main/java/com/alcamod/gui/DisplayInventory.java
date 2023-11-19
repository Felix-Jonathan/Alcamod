package com.alcamod.gui;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import java.util.Arrays;

public class DisplayInventory extends Inventory {
    private final ItemStack[] items;

    public DisplayInventory(int size) {
        this.items = new ItemStack[size];
        Arrays.fill(this.items, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return this.items.length;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack item : this.items) {
            if (!item.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return index >= 0 && index < this.items.length ? this.items[index] : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index >= 0 && index < this.items.length) {
            this.items[index] = stack;
        }
    }

    @Override
    public void setChanged() {
        // Pas nécessaire pour cet inventaire
    }

    @Override
    public void clearContent() {
        Arrays.fill(this.items, ItemStack.EMPTY);
    }

    // Ajoutez ici les autres méthodes requises par l'interface Inventory
}
