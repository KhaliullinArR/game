package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long>, PlayerRepositoryCustom {

    /*@Query("SELECT e from Player e where"+
            "(:name='' or e.name like '%:name%') and "
            +"(:title='' or e.title='%:title%') and "
            +"(:race='' or e.race='%:race%') and "
            +"(:profession='' or e.profession='%:profession%') and "
            +"(:profession='' or e.profession='%:profession%') and "




    )
    public List<Player> getByParams(Pageable pageable);*/
}
