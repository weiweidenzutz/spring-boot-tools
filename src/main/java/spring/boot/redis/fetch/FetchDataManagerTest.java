package spring.boot.redis.fetch;

import org.junit.Test;

import spring.boot.redis.fetch.FetchDataManager;

public class FetchDataManagerTest {

	/**
	 * 爬取python词条
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		String url = "https://baike.baidu.com/item/Python/407313";
		FetchDataManager fd = new FetchDataManager();
		fd.craw(url);
	}
}
