package com.fours.tolevelup.repository.theme;

import com.fours.tolevelup.model.entity.Theme;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ThemeRepositoryImpl implements ThemeCustomRepository {

    private final EntityManager em;

    public ThemeRepositoryImpl(EntityManager em ){
        this.em = em;
    }

    @Override
    // theme 의 모든 필드를 담은 리스트
    public List<Theme> findAll(){
        return em.createQuery("select distinct t from Theme t", Theme.class)
                .getResultList();

    }

    @Override
    public List<Theme> findByType(String type){
        return em.createQuery("select t from Theme t where t.type = :type", Theme.class)
                .setParameter("type", type)
                .getResultList();
    }


}
