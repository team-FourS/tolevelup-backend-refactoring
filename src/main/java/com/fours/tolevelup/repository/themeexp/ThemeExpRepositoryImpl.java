package com.fours.tolevelup.repository.themeexp;

import com.fours.tolevelup.model.entity.ThemeExp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ThemeExpRepositoryImpl implements ThemeExpCustomRepository {
    private final EntityManager em;
    public ThemeExpRepositoryImpl(EntityManager em) { this.em = em; }

    @Override
    // 정보 저장
    public void save1(ThemeExp themeExp){
        em.persist(themeExp);
    }
/*    @Override
    // themeexp id 값 찾는 메서드
    public ThemeExp findById(Theme theme, User user){
        String query = "select t.id from ThemeExp t";
        return (ThemeExp) em.createQuery(query);
    }*/

    @Override
    public List<ThemeExp> findByUser_id(String id){
        return em.createQuery("select t from ThemeExp t where t.user.id = :uid", ThemeExp.class)
                .setParameter("uid", id)
                .getResultList();
    }

    @Override
    public List<ThemeExp> findThemeExp(String id){
        return em.createQuery("select t.exp_total from ThemeExp t where t.user.id=:uid", ThemeExp.class)
                .setParameter("uid", id)
                .getResultList();
    }
/*    @Override //요거 부탁해요♥
    public List<ThemeExp> findById(String user_id) {
        return em.createQuery("select t from ThemeExp t where t.user = :id")
                .setParameter("id", id)
                .getResultList();
    }*/

    @Override
    @Modifying(clearAutomatically = true)
    @Query(value = "update ThemeExp t set t.exp_total =+ :exp_total where t.user.id = :uid and t.theme.id = :tid")
    // exp 값 더해서 저장 메서드
    public void expPlus(@Param("exp_total") float exp_total, @Param("uid") String user_id, @Param("tid") int theme_id){
    }

    @Override
    @Modifying(clearAutomatically = true)
    @Query(value = "update ThemeExp t set t.exp_total =- :exp_total where t.user.id = :uid and t.theme.id = :tid")
    public void expMinus(@Param("exp_total") float exp_total, @Param("uid") String user_id, @Param("tid") int theme_id){
    }
}
