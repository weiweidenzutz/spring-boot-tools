package spring.boot.redis.service;

import java.util.List;

import spring.boot.redis.dto.Exposer;
import spring.boot.redis.dto.SeckillExecution;
import spring.boot.redis.entity.Seckill;
import spring.boot.redis.exception.RepeatKillException;
import spring.boot.redis.exception.SeckillCloseException;
import spring.boot.redis.exception.SeckillException;

/**
 * 业务接口:站在使用者(程序员)的角度设计接口 三个方面: 1.方法定义粒度，方法定义的要非常清楚 2.参数，要越简练越好 3.返回类型(return
 * 类型一定要友好/或者return异常，我们允许的异常)
 */
public interface SeckillService {

	/**
	 * 查询全部秒杀记录
	 * 
	 * @return
	 */
	List<Seckill> getSeckillList();

	/**
	 * 查询单个秒杀记录
	 * 
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);

	/**
	 * 在秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
	 */
	Exposer exportSeckillUrl(long seckillId);

	/**
	 * 执行秒杀操作，有可能失败，有可能成功，所以要抛出我们允许的异常
	 * 
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 * @throws SeckillException
	 * @throws RepeatKillException
	 * @throws SeckillCloseException
	 */
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException;

	SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException;
}