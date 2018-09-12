package spring.boot.redis.mapper;

import org.apache.ibatis.annotations.Param;

import spring.boot.redis.entity.SuccessKilled;

/**
 * 成功秒杀接口
 * @author zhengjiaxing
 * @date 2018年9月11日
 */
public interface SuccessKilledMapper {
	/**
	 * 插入购买明细，可过滤重复
	 * 
	 * @param seckillId
	 * @param userPhone
	 * @return 插入的行数
	 */
	int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

	/**
	 * 根据秒杀商品ID查询明细SuccessKilled对象， 携带了Seckill秒杀产品对象
	 * 
	 * @param seckillId
	 * @param userPhone
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

}
