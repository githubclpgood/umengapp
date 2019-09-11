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
 * MongoDB CURD/API������
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
	 * ��Ԫ���� �������
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
	 * ��ѯȫ����¼
	 */
	@Test
	public void findAllTest() {
		List<Document> list = MongoDB.findAll();
		for (Document document : list) {
			System.out.println(document.toJson());
		}
	}

	/**
	 * ��ȷ��ѯ
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
	 * ģ��,��ȷ��ϲ�ѯ ����Document.append�����������
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
	 * Document OR AND��ѯ
	 */
	@Test
	public void findByOrAndTest() {
		// ��ѯiid����2����iid����8������,ʧ��,ֻ�ܲ鵽8
		Document filter = new Document();
		filter.append("iid", 2);
		filter.append("iid", 8);
		List<Document> list = MongoDB.findBy(filter);

		// $or(��ѯiid����2����iid����8������)
		BasicDBObject basicDBObjectOR = new BasicDBObject().append("$or",
				new BasicDBObject[] { new BasicDBObject("iid", 2), new BasicDBObject("iid", 8) });

		// (��ѯiid����2����name����nie������) �ɹ���ѯ����Ӧ������
		Document filter2 = new Document();
		filter2.append("iid", 2);
		filter2.append("name", "nie");
		List<Document> list2 = MongoDB.findBy(filter2);

		// $and(��ѯiid����2����name����nie������)
		BasicDBObject basicDBObjectAND = new BasicDBObject().append("$and",
				new BasicDBObject[] { new BasicDBObject("iid", 2), new BasicDBObject("name", "nie") });

		// �����
		List<Document> resultOR = MongoDB.findBy(basicDBObjectOR);
		List<Document> resultAND = MongoDB.findBy(basicDBObjectAND);

		// ����������
		for (Document documentOR : resultOR) {
			System.out.println("OR�����: " + documentOR.toJson());
		}

		// ��������
		for (Document documentAND : resultAND) {
			System.out.println("AND�����: " + documentAND.toJson());
		}

		System.out.println("-----------------------------------------------");

		// ��������
		for (Document list1 : list) {
			System.out.println("AND�����: " + list1.toJson());
		}

		System.out.println("-----------------------------------------------");

		// ��������
		for (Document listtwo : list2) {
			System.out.println("AND�����: " + listtwo.toJson());
		}
	}
	
	/**
	 * Document in nin��ѯ
	 */
	@Test
	public void findByInNin(){
		//$in (��ѯiidΪ1��2������)
		BasicDBObject queryObjectIn=new BasicDBObject().append("iid", 
				new BasicDBObject("$in",new int[]{2,8}));
		
		//$nin (��ѯiid��Ϊ1,2,3,4,5,6,7,8,9������)
		BasicDBObject queryObjectNin=new BasicDBObject().append("iid",
				new BasicDBObject("$nin",new int[]{2,8}));
		
		//���ݼ���
		List<Document> list=MongoDB.findAll();
		List<Document> result3=MongoDB.findBy(queryObjectIn);
		List<Document> result4=MongoDB.findBy(queryObjectNin);
		
		//�����������
		for (Document document : list) {
			System.out.println("all data: "+document.toJson());
		}
		System.out.println("-------------------------------------");
		
		//�������
		for (Document document : result4) {
			System.out.println("not in{2,8}: "+document.toJson());
		}
		
		System.out.println("-------------------------------------");
		
		for (Document document : result3) {
			System.out.println("in{2,8}: "+document.toJson());
		}
	}
	
	/**
	 * ���·��������ĵ�һ����¼���ֶ�
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
		
		//ע��update�ĵ���Ҫ����"$set"�ֶ�
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
	 * ���·������������м�¼���ֶ�
	 * ��"$set"����
	 */
	@Test
	public void updateManyTest(){
		Document filter=new Document();
		filter.append("nickname", "xiaoping");
		
		List<Document> results=MongoDB.findBy(filter);
		for (Document document : results) {
			System.out.println(document.toJson());
		}
		
		//ע��update�ĵ�����Ҫ����"$set"�ֶ�
		Document update=new Document();
		update.append("$set",new Document("nickname","chenliping").append("password",123));
		
		//�ֶθ��²���
		UpdateResult result=MongoDB.updateMany(filter, update);
		System.out.println("matched count= "+result.getMatchedCount());
	}
	
	/**
	 * ���·��������ĵ�һ����¼
	 * ����"$set"����
	 */
	@Test
	public void replaceTest(){
		Document filter=new Document();
		filter.append("nickname","chenliping");
		
		List<Document> results=MongoDB.findBy(filter);
		for (Document document : results) {
		    System.out.println(document.toJson());	
		}
		
		//ע��:�����ĵ�ʱ,����Ҫʹ��"$set"
		Document update=new Document();
		update.append("nickname", "����ƽ").append("password","clp");
		UpdateResult result=MongoDB.replaceOne(filter, update);
		System.out.println("matched count= "+result.getMatchedCount());
		
		List<Document> list=MongoDB.findAll();
		for (Document document : list) {
		    System.out.println(document.toJson());	
		}
	}
	
	/**
	 * ɾ�����������ĵ�һ����¼
	 */
	@Test
	public void delteOneTest(){
		Document filter=new Document();
		filter.append("nickname","����ƽ");
		
		List<Document> list=MongoDB.findBy(filter);
		for (Document document : list) {
			System.out.println(document.toJson());
		}
		
		//ɾ����¼��
		MongoDB.deleteOne(filter);
		List<Document> result2=MongoDB.findBy(filter);
		System.out.println("=========================");
		for (Document document : result2) {
			System.out.println(document.toJson());
		}
	}
	
	/**
	 * ɾ���������������м�¼��
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
		
		//����ɾ��������¼����
		MongoDB.deleteMany(filter);
		
		List<Document> list2=MongoDB.findAll();
		for (Document document : list2) {
			System.err.println(document.toJson());
		}
	}
}
