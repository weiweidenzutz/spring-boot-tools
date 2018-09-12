package spring.boot.redis.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import spring.boot.redis.entity.Seckill;

/**
 * 秒杀接口
 * 
 * @author zhengjiaxing
 * @date 2018年9月11日
 */
public interface SeckillMapper {
	/**
	 * 减库存
	 * 
	 * @param seckillId
	 * @param killTime
	 * @return 如果影响行数>1，表示更新库存的记录行数
	 */
	int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

	/**
	 * 根据id查询秒杀的商品信息
	 * 
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(@Param("seckillId") long seckillId);

	/**
	 * 根据偏移量查询秒杀商品列表
	 * 
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

	void killByProcedure(Map<String, Object> paramMap);
}
