package com.game.service;

import com.game.entity.Player;

import java.util.List;
import java.util.Map;

public interface PlayerService {
    public List<Player> getPlayers(Map<String, String> params);

    public Integer getCount(Map<String, String> params);

    public Player createPlayer(Player player);

    public Player getPlayerById(String Id);

    public Player updatePlayer(String Id, Player player);

    public void deletePlayer(String id);
}
