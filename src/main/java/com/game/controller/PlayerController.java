package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    private PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public List<Player> getPlayers(@RequestParam Map<String,String> params) {
        List<Player> list = playerService.getPlayers(params);
        return list;
    }


    @GetMapping("/players/count")
    public Integer getCount(@RequestParam Map<String, String> params) {
      return   playerService.getCount(params);
    }

    @PostMapping("/players")
    public Player createPlayer(@RequestBody Player player) {
        return playerService.createPlayer(player);
    }

    @GetMapping("/players/{playerID}")
    public Player findPlayerById(@PathVariable String playerID) {
        return playerService.getPlayerById(playerID);
    }

    @PostMapping("/players/{playerID}")
    public Player updatePlayer(@RequestBody Player player, @PathVariable String playerID) {
        return playerService.updatePlayer(playerID, player);
    }

    @DeleteMapping("/players/{playerId}")
    public void deletePlayerById(@PathVariable String playerId) {
        playerService.deletePlayer(playerId);
    }
}
