package com.fours.tolevelup.repository.character;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserCharacterRepositoryImpl implements UserCharacterCustomRepository{
    private final EntityManager em;

    public UserCharacterRepositoryImpl(EntityManager em){
        this.em = em;
    }

}
