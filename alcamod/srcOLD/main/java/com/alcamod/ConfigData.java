package com.alcamod; // Remplacez par votre package approprié

import java.util.List;

public class ConfigData {
    private List<String> rewards;
    private List<String> topRewards;

    // Getters et Setters
    public List<String> getRewards() {
        return rewards;
    }

    public void setRewards(List<String> rewards) {
        this.rewards = rewards;
    }

    public List<String> getTopRewards() {
        return topRewards;
    }

    public void setTopRewards(List<String> topRewards) {
        this.topRewards = topRewards;
    }
}
