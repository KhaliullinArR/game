package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PlayerRepositoryCustom {
    public List<Player> findByParams(Map<String, String> params, Pageable pageable);

    public Integer getPlayerCounts(Map<String, String> params);
}
