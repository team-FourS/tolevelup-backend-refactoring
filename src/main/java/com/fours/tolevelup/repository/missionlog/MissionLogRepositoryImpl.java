package com.fours.tolevelup.repository.missionlog;
import com.fours.tolevelup.model.MissionStatus;
import com.fours.tolevelup.model.entity.MissionLog;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.sql.Date;
import javax.persistence.EntityManager;

@Repository
public class MissionLogRepositoryImpl implements MissionLogCustomRepository {

    private final EntityManager em;

    public MissionLogRepositoryImpl(EntityManager em) {
        this.em = em;
    }

//    @Override
//    public Optional<MissionLog> findByUserId(String id){
//        List<MissionLog> result =
//                em.createQuery("select m from MissionLog m where m.user.id = :id" , MissionLog.class)
//                        .setParameter("id", id)
//                        .getResultList();
//
//        return result.stream().findAny();
//    }


    @Override
    // 미션 수행 전 상태 저장
    public void saveMissionLog(MissionLog missionLog) {
        em.persist(missionLog);
    }

    @Override
    public List<MissionLog> findByStatus(MissionStatus status){
        return em.createQuery("select m from MissionLog m where m.status = :status", MissionLog.class)
                .setParameter("status", status)
                .getResultList();
    }


    @Override
    // 미션 수행 후 end_date와 status 업데이트 / 미션로그 id 를 이용
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE MissionLog m set m.end_time = :end_date, m.status = :status where m.id = :id")
    public void missionChecked(@Param("end_date") Date end_date,@Param("status") MissionStatus status, @Param("id") int id) {
    }

    @Override
    @Modifying(clearAutomatically = true)
    @Query(value  ="update MissionLog m set m.status = :status, m.end_time = null where m.id = :id")
    public void missionNonChecked(@Param("status")MissionStatus status, @Param("id") int id){

    }

    @Override
    public MissionLog findByMissionId(int mission_id) {
        return em.find(MissionLog.class, mission_id);
    }


/*    @Override
    public List<MissionLog> findByUser_IdAndStatus(String user_id, String missionStatus) {
        String query =  "select m.id from MissionLog m where m.user.id = :user_id and m.status = :staus";
        return (List<MissionLog>) em.createQuery(query);
    }*/
}




