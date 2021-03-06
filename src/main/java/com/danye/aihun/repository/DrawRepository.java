package com.danye.aihun.repository;

import com.danye.aihun.model.Draw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:huangjiang1026@gmail.com">H.J</a> on 2018/4/29 14:26
 */
@Repository
public interface DrawRepository extends JpaRepository<Draw, String> {

    Draw getTopByUserIdAndIsDraw(String userId, Short isDraw);

    @Query(value = " SELECT COUNT(*) FROM t_draw td, t_contact tc WHERE td.user_id = tc.uid AND td.prize_id = ?1 ", nativeQuery = true)
    Integer getDrawCountByPrizeId(String prizeId);
}
