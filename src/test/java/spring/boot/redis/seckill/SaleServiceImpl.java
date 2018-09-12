package spring.boot.redis.seckill;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;

import spring.boot.redis.util.DateUtil;
import spring.boot.redis.util.RedisUtil;

/**
 * Redis分布式锁
 * 		用redis如何处理电商平台，秒杀、抢购超卖
 * @author zhengjiaxing
 * @date 2018年9月11日
 */
public class SaleServiceImpl {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * @Description: 抢购的计数处理（用于处理超卖）
	 * @param @param key 购买计数的key
	 * @param @param limitCount 总的限购数量
	 * @param @param buyCount 当前购买数量
	 * @param @param endDate 抢购结束时间
	 * @param @param lock 锁的名称与unDieLock方法的lock相同
	 * @param @param expire 锁占有的时长（毫秒）
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws Exception
	 */
	public boolean checkSoldCountByRedisDate(String key, int limitCount, int buyCount, Date endDate, String lock, int expire) throws Exception {
		boolean check = false;
		if (this.lock(lock, expire)) {
			Integer soldCount = (Integer) redisUtil.get(key);
			Integer totalSoldCount = (soldCount == null ? 0 : soldCount) + buyCount;
			if (totalSoldCount <= limitCount) {
				redisUtil.set(key, totalSoldCount, DateUtil.diffDateTime(endDate, new Date()));
				check = true;
			}
			redisUtil.remove(lock);
		} else {
			if (this.unDieLock(lock)) {
				logger.info("解决了出现的死锁");
			} else {
				throw new Exception("活动太火爆啦,请稍后重试");
			}
		}
		return check;
	}
 
	/**
	 * @Description: 加锁机制
	 * @param @param lock 锁的名称
	 * @param @param expire 锁占有的时长（毫秒）
	 * @param @return 设定文件
	 * @return Boolean 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public Boolean lock(final String lock, final int expire) {
		return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				boolean locked = false;
				byte[] lockValue = redisTemplate.getValueSerializer().serialize(DateUtil.getDateAddMillSecond(null, expire));
				byte[] lockName = redisTemplate.getStringSerializer().serialize(lock);
				locked = connection.setNX(lockName, lockValue);
				if (locked)
					connection.expire(lockName, TimeoutUtils.toSeconds(expire, TimeUnit.MILLISECONDS));
				return locked;
			}
		});
	}
 
	/**
	 * @Description: 处理发生的死锁
	 * @param @param lock 是锁的名称
	 * @param @return 设定文件
	 * @return Boolean 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public Boolean unDieLock(final String lock) {
		boolean unLock = false;
		Date lockValue = (Date) redisTemplate.opsForValue().get(lock);
		if (lockValue != null && lockValue.getTime() <= (new Date().getTime())) {
			redisTemplate.delete(lock);
			unLock = true;
		}
		return unLock;
	}
}
