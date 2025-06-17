package com.fours.tolevelup.repository.mission;

import com.fours.tolevelup.model.entity.Mission;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class MissionRepositoryImpl implements MissionCustomRepository {
    private final EntityManager em;

    public MissionRepositoryImpl(EntityManager em){
        this.em = em;
    }

    @Override
    public Mission findByContent(String content){
        String query = "select m from Mission m where m.content = :content";
        return (Mission) em.createQuery(query);
    }

    @Override
    public List<Mission> findByTheme(int theme_id){
        return em.createQuery("select m from Mission m where m.theme.id = :tid", Mission.class)
                .setParameter("tid", theme_id)
                .getResultList();
    }

}
