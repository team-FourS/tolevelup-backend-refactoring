package com.fours.tolevelup.repository.character;

import com.fours.tolevelup.model.entity.Character;
import com.fours.tolevelup.model.entity.UserCharacter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

@Repository
public class CharacterRepositoryImpl implements CharacterCustomRepository{
    private final EntityManager em;

    public CharacterRepositoryImpl(EntityManager em){
        this.em = em;
    }

    @Override
    public Character findById(String id){
        return em.find(Character.class, id);
    }


/*    @Override
    public List<Character> findByLevel(){
        return em.createQuery("select ch from Character ch where ch.level = 1", Character.class).getResultList();
    }*/
}
