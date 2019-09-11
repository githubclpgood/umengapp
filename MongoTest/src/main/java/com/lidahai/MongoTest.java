package com.lidahai;

import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;

/**
 * MongoDB CURD/API测试类
 * 
 * @author chenliping
 * @Time 2018-12-18
 *
 */
@SuppressWarnings("all")
public class MongoTest {

	@Before
	public void before() {
		MongoDB.connect("test", "person", "127.0.0.1", 27017);
	}

	/**
	 * 单元测试 插入测试
	 */
	@Test
	public void insertTest() {
		Document document = new Document();
		// document.append("name",
		// "nie").append("password","nie").append("nickname","nn").append("iid",8);
		// document.append("name",
		// "y").append("password","girl").append("nickname","ygirl12").append("iid",6);
		// document.append("name",
		// "x").append("password","boy").append("nickname","boy12").append("iid",-1);
		//document.append("name", "nie").append("password", "mynie").append("nickname", "nie12").append("iid", 2);
		//document.append("name", "clp").append("password", "clpgood").append("nickname", "xiaoping").append("iid", 1);
		//document.append("name", "xp").append("password", "xp").append("nickname", "xiaoping").append("iid", 5);
		//document.append("name", "xp").append("password", "xp").append("nickname", "xiaoping").append("iid", 5);
		document.append("name", "xp").append("password", "xp").append("nickname", "xiaoping").append("iid", 5);
		MongoDB.insert(document);
	}

	/**
	 * 查询全部记录
	 */
	@Test
	public void findAllTest() {
		List<Document> list = MongoDB.findAll();
		for (Document document : list) {
			System.out.println(document.toJson());
		}
	}

	/**
	 * 精确查询
	 */
	@Test
	public void findByExactQueryTest() {
		Document documentFilter = new Document();
		documentFilter.append("name", "y");
		documentFilter.append("nickname", "ygirl12");
		List<Document> list = MongoDB.findBy(documentFilter);
		for (Document document : list) {
			System.out.println(document.toJson());
		}
	}

	/**
	 * 模糊,精确混合查询 利用Document.append随意添加条件
	 */
	@Test
	public void findByLickQueryTest() {
		int iid = 2;
		String name = "";
		String nickname = "";
		Document filter = new Document();
		if (iid >= 0) {
			filter.append("iid", new Document("$gte", 2));
		}
		if (name != "") {
			filter.append("name", new Document("$regex", "y"));
		}
		if (nickname != "") {
			filter.append("nickname", "ygirl12");
		}
		List<Document> results = MongoDB.findBy(filter);
		for (Document document : results) {
			System.out.println(document.toJson());
		}
	}
    
	/**
	 * Document OR AND查询
	 */
	@Test
	public void findByOrAndTest() {
		// 查询iid等于2或者iid等于8的数据,失败,只能查到8
		Document filter = new Document();
		filter.append("iid", 2);
		filter.append("iid", 8);
		List<Document> list = MongoDB.findBy(filter);

		// $or(查询iid等于2或者iid等于8的数据)
		BasicDBObject basicDBObjectOR = new BasicDBObject().append("$or",
				new BasicDBObject[] { new BasicDBObject("iid", 2), new BasicDBObject("iid", 8) });

		// (查询iid等于2并且name等于nie的数据) 成功查询出相应的数据
		Document filter2 = new Document();
		filter2.append("iid", 2);
		filter2.append("name", "nie");
		List<Document> list2 = MongoDB.findBy(filter2);

		// $and(查询iid等于2并且name等于nie的数据)
		BasicDBObject basicDBObjectAND = new BasicDBObject().append("$and",
				new BasicDBObject[] { new BasicDBObject("iid", 2), new BasicDBObject("name", "nie") });

		// 结果集
		List<Document> resultOR = MongoDB.findBy(basicDBObjectOR);
		List<Document> resultAND = MongoDB.findBy(basicDBObjectAND);

		// 输出结果集合
		for (Document documentOR : resultOR) {
			System.out.println("OR结果集: " + documentOR.toJson());
		}

		// 输出结果集
		for (Document documentAND : resultAND) {
			System.out.println("AND结果集: " + documentAND.toJson());
		}

		System.out.println("-----------------------------------------------");

		// 输出结果集
		for (Document list1 : list) {
			System.out.println("AND结果集: " + list1.toJson());
		}

		System.out.println("-----------------------------------------------");

		// 输出结果集
		for (Document listtwo : list2) {
			System.out.println("AND结果集: " + listtwo.toJson());
		}
	}
	
	/**
	 * Document in nin查询
	 */
	@Test
	public void findByInNin(){
		//$in (查询iid为1和2的数据)
		BasicDBObject queryObjectIn=new BasicDBObject().append("iid", 
				new BasicDBObject("$in",new int[]{2,8}));
		
		//$nin (查询iid不为1,2,3,4,5,6,7,8,9的数据)
		BasicDBObject queryObjectNin=new BasicDBObject().append("iid",
				new BasicDBObject("$nin",new int[]{2,8}));
		
		//数据集合
		List<Document> list=MongoDB.findAll();
		List<Document> result3=MongoDB.findBy(queryObjectIn);
		List<Document> result4=MongoDB.findBy(queryObjectNin);
		
		//输出集合数据
		for (Document document : list) {
			System.out.println("all data: "+document.toJson());
		}
		System.out.println("-------------------------------------");
		
		//输出数据
		for (Document document : result4) {
			System.out.println("not in{2,8}: "+document.toJson());
		}
		
		System.out.println("-------------------------------------");
		
		for (Document document : result3) {
			System.out.println("in{2,8}: "+document.toJson());
		}
	}
	
	/**
	 * 更新符合条件的第一条记录的字段
	 */
	@Test
	public void updateOneTest(){
		Document filter=new Document();
		filter.append("nickname", "ygirl12");
		
		List<Document> results=MongoDB.findBy(filter);
		for (Document document : results) {
			System.out.println(document.toJson());
		}
		
		Document filter2=new Document();
		filter2.append("nickname","ygirl34");
		
		//注意update文档里要包含"$set"字段
		Document update=new Document();
		update.append("$set", new Document("nickname","ygirl34").append("password", "ygirl2"));
		UpdateResult result=MongoDB.updateOne(filter, update);
		
		System.out.println("matched count= "+result.getMatchedCount());
		List<Document> list=MongoDB.findBy(filter);
		for (Document document : list) {
			System.out.println(document.toJson());
		}
		System.out.println("======================================");
		
		List<Document> result2=MongoDB.findBy(filter2);
		for (Document document : result2) {
			System.out.println(document.toJson());
		}
	}
	
	/**
	 * 更新符合条件的所有记录的字段
	 * 用"$set"更新
	 */
	@Test
	public void updateManyTest(){
		Document filter=new Document();
		filter.append("nickname", "xiaoping");
		
		List<Document> results=MongoDB.findBy(filter);
		for (Document document : results) {
			System.out.println(document.toJson());
		}
		
		//注意update文档里面要包含"$set"字段
		Document update=new Document();
		update.append("$set",new Document("nickname","chenliping").append("password",123));
		
		//字段更新操作
		UpdateResult result=MongoDB.updateMany(filter, update);
		System.out.println("matched count= "+result.getMatchedCount());
	}
	
	/**
	 * 更新符合条件的第一条记录
	 * 不用"$set"更新
	 */
	@Test
	public void replaceTest(){
		Document filter=new Document();
		filter.append("nickname","chenliping");
		
		List<Document> results=MongoDB.findBy(filter);
		for (Document document : results) {
		    System.out.println(document.toJson());	
		}
		
		//注意:更新文档时,不需要使用"$set"
		Document update=new Document();
		update.append("nickname", "陈立平").append("password","clp");
		UpdateResult result=MongoDB.replaceOne(filter, update);
		System.out.println("matched count= "+result.getMatchedCount());
		
		List<Document> list=MongoDB.findAll();
		for (Document document : list) {
		    System.out.println(document.toJson());	
		}
	}
	
	/**
	 * 删除符合条件的第一条记录
	 */
	@Test
	public void delteOneTest(){
		Document filter=new Document();
		filter.append("nickname","陈立平");
		
		List<Document> list=MongoDB.findBy(filter);
		for (Document document : list) {
			System.out.println(document.toJson());
		}
		
		//删除记录数
		MongoDB.deleteOne(filter);
		List<Document> result2=MongoDB.findBy(filter);
		System.out.println("=========================");
		for (Document document : result2) {
			System.out.println(document.toJson());
		}
	}
	
	/**
	 * 删除符合条件的所有记录数
	 */
	@Test
	public void deleteMany(){
		Document filter=new Document();
		filter.append("nickname","xiaoping");

		List<Document> list=MongoDB.findBy(filter);
		for (Document document : list) {
			System.out.println(document.toJson());
		}
		
		System.out.println("=========================");
		
		//调用删除多条记录方法
		MongoDB.deleteMany(filter);
		
		List<Document> list2=MongoDB.findAll();
		for (Document document : list2) {
			System.err.println(document.toJson());
		}
	}
}
