package spring.boot.redis.seckill;

import java.util.Calendar;
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
import org.springframework.stereotype.Component;

import spring.boot.redis.util.DateUtil;
import spring.boot.redis.util.RedisUtil;

/**
 * @ClassName: LockRetry
 * @Description: 此功能只用于促销组
 */
@SuppressWarnings("rawtypes")
@Component("lockRetry")
public class LockRetry {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RedisTemplate redisTemplate;
 
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 
	 * @Description: 重入锁
	 * @param lock 名称
	 * @param expire 锁定时长（秒），建议10秒内
	 * @param num 取锁重试试数，建议不大于3
	 * @param interval 重试时长
	 * @param forceLock 强制取锁，不建议；
	 * @return
	 * @throws Exception    设定文件
	 * @return Boolean    返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public Boolean retryLock(final String lock, final int expire, final int num, final long interval, final boolean forceLock) throws Exception {
		Date lockValue = (Date) redisTemplate.opsForValue().get(lock);
		if (forceLock) {
			redisUtil.remove(lock);
		}
		if (num <= 0) {
			if (null != lockValue && lockValue.getTime() >= (new Date().getTime())) {
				logger.debug(String.valueOf((lockValue.getTime() - new Date().getTime())));
				Thread.sleep(lockValue.getTime() - new Date().getTime());
				redisUtil.remove(lock);
				return retryLock(lock, expire, 1, interval, forceLock);
			}
			return false;
		} else {
			return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					boolean locked = false;
					byte[] lockValue = redisTemplate.getValueSerializer().serialize(DateUtil.getDateAdd(null, expire, Calendar.SECOND));
					byte[] lockName = redisTemplate.getStringSerializer().serialize(lock);
					logger.debug(lockValue.toString());
					locked = connection.setNX(lockName, lockValue);
					if (locked)
						return connection.expire(lockName, TimeoutUtils.toSeconds(expire, TimeUnit.SECONDS));
					else {
						try {
							Thread.sleep(interval);
							return retryLock(lock, expire, num - 1, interval, forceLock);
						} catch (Exception e) {
							e.printStackTrace();
							return locked;
						}
 
					}
				}
			});
		}
 
	}
}
