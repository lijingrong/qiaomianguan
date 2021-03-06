package com.danye.aihun.service;

import com.danye.aihun.model.Draw;
import com.danye.aihun.model.DrawNot;
import com.danye.aihun.model.Prize;
import com.danye.aihun.repository.DrawNotRepository;
import com.danye.aihun.repository.DrawRepository;
import com.danye.aihun.repository.PrizeRepository;
import com.danye.aihun.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DrawService {

    @Autowired
    private PrizeRepository prizeRepository;
    @Autowired
    private DrawRepository drawRepository;
    @Autowired
    private DrawNotRepository drawNotRepository;

    /**
     * 获取中奖奖品，如果用户已中过奖，再次抽奖则不中奖
     *
     * @return Prize
     */
    public Prize getPrize() {
        return getRandomPrize(prizeRepository.getAvailablePrize());
    }

    /**
     * 根据中奖概率随机获取奖品
     *
     * @param prizes List对象
     * @return Prize
     */
    private Prize getRandomPrize(List<Prize> prizes) {
        if (null != prizes && !prizes.isEmpty()) {
            int sum = 0; // 概率数组的总概率精度
            // 中奖总概率
            for (Prize p : prizes) {
                sum += p.getRand();
            }

            for (Prize prize : prizes) {
                Random rand = new Random();
                // 获取1-sum之间的概率
                int randNum = rand.nextInt(sum);
                // 中奖了
                if (randNum <= prize.getRand()) {
                    return prize;
                } else {
                    // 继续
                    sum -= prize.getRand();
                }
            }
        }
        return null;
    }

    /**
     * 抽奖
     *
     * @param userId 用户Id
     * @return Short
     */
    @Transactional
    public Short draw(String userId) {
        try {
            Draw drawRecord = drawRepository.getTopByUserIdAndIsDraw(userId, Constants.WINNING_PRIZE);
            if (null != drawRecord) {
                return Constants.NO_PRIZE;
            }

            Prize prize = getPrize();
            if (prize.getPrizeType().shortValue() != Constants.NO_PRIZE) {
                Integer updateCount = prizeRepository.updatePrizeRemainCount(prize.getPrizeId());
                if (updateCount == 0) {
                    return Constants.NO_PRIZE;
                }
                Draw draw = new Draw(UUID.randomUUID().toString(), userId, prize.getPrizeId(), (short) 1);
                drawRepository.save(draw);
            } else {
                DrawNot drawNot = new DrawNot(UUID.randomUUID().toString(), userId, prize.getPrizeId(), (short) 0);
                drawNotRepository.save(drawNot);
            }

            return prize.getPrizeType();
        } catch (Exception e) {
            return Constants.NO_PRIZE;
        }
    }

    public Integer getDrawCountByPrizeId(String prizeId) {
        return drawRepository.getDrawCountByPrizeId(prizeId);
    }
}
