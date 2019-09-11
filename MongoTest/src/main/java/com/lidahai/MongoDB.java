package com.lidahai;

import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;

import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

/**
 * MongoDB CRUD����������
 * @author chenlipnig
 * @Time 2018-12-18
 */
@SuppressWarnings("all")
public class MongoDB {
	
    private static MongoCollection<Document> collection;
    
    /**
     * �������ݿ�
     * @param databaseName
     * @param collectionName
     * @param hostName
     * @param port
     */
    public static void connect(String databaseName,String collectionName,String hostName,int port){
    	 MongoClient client=new MongoClient(hostName,port);
    	 MongoDatabase database=client.getDatabase(databaseName);
    	 collection=database.getCollection(collectionName);
    	 System.out.println(collection);
    }
    
    /**
     * ����һ���ĵ�
     * @param document
     */
    public static void insert(Document document){
    	collection.insertOne(document);
    }
    
    /**
     * ��ѯ�����ĵ�
     * @return �����ĵ�����
     */
    public static List<Document> findAll(){
    	List<Document> results=new ArrayList<Document>();
    	FindIterable<Document> iterables=collection.find();
    	MongoCursor<Document> cursor=iterables.iterator();
    	while(cursor.hasNext()){
    		results.add(cursor.next());
    	}
    	return results;
    }
    
    /**
     * ����������ѯ
     * @param filter
     *     ��ѯ����   //ע��Bson�ļ���ʵ����,BasicDBObject,BsonDocument,BsonDocumentWrapper
     *     CommandResult,Document,RawBsonDocument
     * @return  ���ؼ����б�
     */
    public static List<Document> findBy(Bson filter){
    	List<Document> results=new ArrayList<Document>();
    	FindIterable<Document> iterables=collection.find(filter);
    	MongoCursor<Document> cursor=iterables.iterator();
    	while(cursor.hasNext()){
    		results.add(cursor.next());
    	}
    	return results;
    }
    
    /**
     * ���²�ѯ���ĵ�һ��
     * @param filter ��ѯ����
     * @param update �����ĵ�
     * @return ���½��
     */
    public static UpdateResult updateOne(Bson filter,Bson update){
    	UpdateResult result=collection.updateOne(filter, update);
    	return result;
    }
    
    /**
     * ���²�ѯ���������ĵ�
     * @param filter ��ѯ����
     * @param update �����ĵ�
     * @return ���½��
     */
    public static UpdateResult updateMany(Bson filter,Bson update){
    	UpdateResult result=collection.updateMany(filter, update);
    	return null;
    }
    
    /**
     * ����һ���ĵ�,�����replacement�����ĵ�,���ĵ���ȫ���滻
     * @param filter ��ѯ����
     * @param replacement �����ĵ�
     * @return �����ĵ�
     */
    public static UpdateResult replaceOne(Bson filter,Document replacement){
    	UpdateResult result=collection.replaceOne(filter, replacement);
    	return result;
    }
    
    /**
     * ��������ɾ��һ���ĵ�
     * @param filter
     */
    public static void deleteOne(Bson filter){
    	collection.deleteOne(filter);
    }
    
    /**
     * ��������ɾ������ĵ�
     * @param filter ��ѯ����
     */
    public static void deleteMany(Bson filter){
    	collection.deleteMany(filter);
    }
}
