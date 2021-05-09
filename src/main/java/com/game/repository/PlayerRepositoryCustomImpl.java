package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class PlayerRepositoryCustomImpl implements PlayerRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

   /* @Autowired
   .
    public PlayerRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }*/

    private Race getRace(String raceName) {
        Race race;
        switch (raceName){
            case "GIANT" :
                race = Race.GIANT;
                break;
            case "HUMAN" :
                race = Race.HUMAN;
                break;
            case "DWARF" :
                race = Race.DWARF;
                break;
            case "ELF" :
                race = Race.ELF;
                break;
            case "ORC" :
                race = Race.ORC;
                break;
            case "TROLL" :
                race = Race.TROLL;
                break;
            case "HOBBIT" :
                race = Race.HOBBIT;
                break;
            default:
                race = Race.HOBBIT;
        }
        return race;
    }

    private Profession getProfession(String profName) {
        Profession prof;
        switch (profName) {
            case "WARRIOR":
                prof = Profession.WARRIOR;
                break;
            case "WARLOCK":
                prof = Profession.WARLOCK;
                break;
            case "ROGUE":
                prof = Profession.ROGUE;
                break;
            case "SORCERER":
                prof = Profession.SORCERER;
                break;
            case "CLERIC":
                prof = Profession.CLERIC;
                break;
            case "PALADIN":
                prof = Profession.PALADIN;
                break;
            case "NAZGUL":
                prof = Profession.NAZGUL;
                break;
            case "DRUID":
                prof = Profession.DRUID;
                break;
            default:
                prof = Profession.DRUID;
        }
        return prof;
    }


    @Override
    public List<Player> findByParams(Map<String, String> params, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
        Root<Player> playersRoot = criteriaQuery.from(Player.class);

        List<Predicate> predicates = new ArrayList<>();


        if(params.get("name") != null) predicates.add(criteriaBuilder.like(criteriaBuilder.lower(playersRoot.get("name")),"%"+params.get("name").toLowerCase()+"%"));

        if(params.get("title") != null) predicates.add(criteriaBuilder.like(criteriaBuilder.lower(playersRoot.get("title")),"%"+params.get("title").toLowerCase()+"%"));

        if(params.get("race") != null){
            String raceString = params.get("race");
            Race race = getRace(raceString);
            predicates.add(criteriaBuilder.equal(playersRoot.get("race"),race));

        }

        if(params.get("profession") != null){
            String profString = params.get("profession");
            Profession profession = getProfession(profString);
            predicates.add(criteriaBuilder.equal(playersRoot.get("profession"),profession));
        }

        if(params.get("after") != null) predicates.add(criteriaBuilder.greaterThan(playersRoot.<Date>get("birthday"),new Date(Long.parseLong(params.get("after")))));

        if(params.get("before") != null) predicates.add(criteriaBuilder.lessThan(playersRoot.<Date>get("birthday"),new Date(Long.parseLong(params.get("before")))));

        if(params.get("banned") != null) predicates.add(criteriaBuilder.equal(playersRoot.get("banned"),(params.get("banned").equals("true"))?1:0));

        /*if(Boolean.getBoolean(params.get("banned"))){
            predicates.add(criteriaBuilder.isTrue(playersRoot.get("banned")));
        }else predicates.add(criteriaBuilder.isFalse(playersRoot.get("banned")));*/

        if(params.get("minExperience") != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(playersRoot.get("experience"),Integer.parseInt(params.get("minExperience"))));

        if(params.get("maxExperience") != null) predicates.add(criteriaBuilder.lessThanOrEqualTo(playersRoot.get("experience"),Integer.parseInt(params.get("maxExperience"))));

        if(params.get("minLevel") != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(playersRoot.get("level"),Integer.parseInt(params.get("minLevel"))));

        if(params.get("maxLevel") != null) predicates.add(criteriaBuilder.lessThanOrEqualTo(playersRoot.get("level"),Integer.parseInt(params.get("maxLevel"))));

        String order = params.get("order");
        if(order == null) order = "id";


        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0]))).orderBy(criteriaBuilder.asc(playersRoot.get(order.toLowerCase())));

        return entityManager.createQuery(criteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
    }

    @Override
    public Integer getPlayerCounts(Map<String, String> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Player> playersRoot = criteriaQuery.from(Player.class);

        List<Predicate> predicates = new ArrayList<>();


        if(params.get("name") != null) predicates.add(criteriaBuilder.like(criteriaBuilder.lower(playersRoot.get("name")),"%"+params.get("name").toLowerCase()+"%"));

        if(params.get("title") != null) predicates.add(criteriaBuilder.like(criteriaBuilder.lower(playersRoot.get("title")),"%"+params.get("title").toLowerCase()+"%"));

        if(params.get("race") != null){
            String raceString = params.get("race");
            Race race = getRace(raceString);
            predicates.add(criteriaBuilder.equal(playersRoot.get("race"),race));

        }

        if(params.get("profession") != null){
            String profString = params.get("profession");
            Profession profession = getProfession(profString);
            predicates.add(criteriaBuilder.equal(playersRoot.get("profession"),profession));
        }

        if(params.get("after") != null) predicates.add(criteriaBuilder.greaterThan(playersRoot.<Date>get("birthday"),new Date(Long.parseLong(params.get("after")))));

        if(params.get("before") != null) predicates.add(criteriaBuilder.lessThan(playersRoot.<Date>get("birthday"),new Date(Long.parseLong(params.get("before")))));

        if(params.get("banned") != null) predicates.add(criteriaBuilder.equal(playersRoot.get("banned"),(params.get("banned").equals("true"))?1:0));

        if(params.get("minExperience") != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(playersRoot.get("experience"),Integer.parseInt(params.get("minExperience"))));

        if(params.get("maxExperience") != null) predicates.add(criteriaBuilder.lessThanOrEqualTo(playersRoot.get("experience"),Integer.parseInt(params.get("maxExperience"))));

        if(params.get("minLevel") != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(playersRoot.get("level"),Integer.parseInt(params.get("minLevel"))));

        if(params.get("maxLevel") != null) predicates.add(criteriaBuilder.lessThanOrEqualTo(playersRoot.get("level"),Integer.parseInt(params.get("maxLevel"))));

        CriteriaQuery<Long> cQ =   criteriaQuery.select(criteriaBuilder.count(playersRoot)).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        return  entityManager.createQuery(cQ).getSingleResult().intValue();
    }
}
