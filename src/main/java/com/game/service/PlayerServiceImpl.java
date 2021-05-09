package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.entity.exception.PlayerNotFoundException;
import com.game.entity.exception.PlayerNotValidException;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository playerRepository;

   /* private String getStringOrder(String orderName) {
        String result = null;

        if(orderName.equals(PlayerOrder.NAME.getFieldName())) result = "";
    }*/

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> getPlayers(Map<String, String> params) {
        String ps = params.get("pageSize");
        String pn = params.get("pageNumber");

        int size;
        int number;

        if(ps == null) size = 3;
        else  size = Integer.parseInt(ps);

        if(pn == null) number = 0;
        else  number = Integer.parseInt(params.get("pageNumber"));


        String order;
        String orderParam = params.get("order");

        if(orderParam==null) order = "id";
        else order = orderParam.toLowerCase();

        Sort sort = Sort.by(Sort.Direction.ASC, order);

        Pageable pageable = PageRequest.of(number, size, sort);


        List<Player> players = playerRepository.findByParams(params, pageable);

        return players;
    }

    @Override
    public Integer getCount(Map<String, String> params) {
        return playerRepository.getPlayerCounts(params);
    }


    private boolean isRequiredField(Player player) {
        if(player.getName() == null) return false;
        if(player.getTitle() == null) return false;
        if(player.getRace() == null) return false;
        if(player.getProfession() == null) return false;
        if(player.getBirthday() == null) return false;
        if(player.getExperience() == null) return false;
        player.setBanned(player.getBanned() != null);
        return true;
    }


    private boolean isValid(Player player) {
        if(player.getName().length() > 12 || player.getName().isEmpty()) return false;
        if(player.getTitle().length() > 30) return false;
        if(player.getExperience() > 10000000 || player.getExperience() < 0) return false;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(player.getBirthday());
        int year = calendar.get(Calendar.YEAR);
        if(player.getBirthday().getTime() < 0) return false;
        if(year < 2000 || year > 3000) return false;

        return true;
    }

    @Override
    public Player createPlayer(Player player) {
        if(!isRequiredField(player)) throw new PlayerNotValidException();

        if(!isValid(player)) throw new PlayerNotValidException();

        int currentLevel = (int)((Math.sqrt(2500 + 200*player.getExperience())-50)/100d);

        player.setLevel(currentLevel);

        int untilNextLevel = 50 * (currentLevel + 1) * (currentLevel + 2) - player.getExperience();

        player.setUntilNextLevel(untilNextLevel);

        return playerRepository.save(player);
    }


    @Override
    public Player getPlayerById(String Id) {
        long id;

        try{
            id = Long.parseLong(Id);
        }catch(Exception e){
            throw new PlayerNotValidException();
        }

        if(id <= 0) throw new PlayerNotValidException();

        Optional<Player> player = playerRepository.findById(id);

        if(!player.isPresent()) throw new PlayerNotFoundException();

        return player.get();
    }

    @Override
    public Player updatePlayer(String Id, Player playerForUpdate) {
        long id;

        try{
            id = Long.parseLong(Id);
        }catch(Exception e){
            throw new PlayerNotValidException();
        }

        if(id <= 0) throw new PlayerNotValidException();

        Optional<Player> optionalPlayer = playerRepository.findById(id);

        if(!optionalPlayer.isPresent()) throw new PlayerNotFoundException();

        Player player = optionalPlayer.get();

        String name = playerForUpdate.getName();
        if(name != null){
            if(name.length() > 12 || name.isEmpty()) throw new PlayerNotValidException();
            player.setName(name);
        }

        String title = playerForUpdate.getTitle();
        if (title != null) {
            if(title.length() > 30) throw new PlayerNotValidException();
            player.setTitle(title);
        }

        Race race = playerForUpdate.getRace();
        if(race != null){
            player.setRace(race);
        }

        Profession profession = playerForUpdate.getProfession();
        if(profession != null){
            player.setProfession(profession);
        }

        Date date = playerForUpdate.getBirthday();
        if(date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if(date.getTime() < 0) throw new PlayerNotValidException();
            int year = calendar.get(Calendar.YEAR);
            if(year < 2000 || year > 3000) throw new PlayerNotValidException();

            player.setBirthday(date);

        }

        Boolean banned = playerForUpdate.getBanned();
        if(banned != null){
            player.setBanned(banned);
        }

        Integer experience = playerForUpdate.getExperience();
        if(experience != null){
            if(experience > 10000000 || experience < 0) throw new PlayerNotValidException();
            player.setExperience(experience);

            int currentLevel = (int)((Math.sqrt(2500 + 200*experience)-50)/100d);

            player.setLevel(currentLevel);

            int untilNextLevel = 50 * (currentLevel + 1) * (currentLevel + 2) - experience;

            player.setUntilNextLevel(untilNextLevel);
        }

        return playerRepository.save(player);
    }

    @Override
    public void deletePlayer(String id) {
        long playerId;

        try{
            playerId = Long.parseLong(id);
        }catch(Exception e){
            throw new PlayerNotValidException();
        }

        if(playerId <= 0) throw new PlayerNotValidException();

        Optional<Player> player = playerRepository.findById(playerId);

        if(!player.isPresent()) throw new PlayerNotFoundException();

        playerRepository.delete(player.get());
    }
}
