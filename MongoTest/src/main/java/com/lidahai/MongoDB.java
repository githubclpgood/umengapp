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
 * MongoDB CRUD操作工具类
 * @author chenlipnig
 * @Time 2018-12-18
 */
@SuppressWarnings("all")
public class MongoDB {
	
    private static MongoCollection<Document> collection;
    
    /**
     * 连接数据库
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
     * 插入一个文档
     * @param document
     */
    public static void insert(Document document){
    	collection.insertOne(document);
    }
    
    /**
     * 查询所有文档
     * @return 所有文档集合
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
     * 根据条件查询
     * @param filter
     *     查询条件   //注意Bson的几个实现类,BasicDBObject,BsonDocument,BsonDocumentWrapper
     *     CommandResult,Document,RawBsonDocument
     * @return  返回集合列表
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
     * 更新查询到的第一个
     * @param filter 查询条件
     * @param update 更新文档
     * @return 更新结果
     */
    public static UpdateResult updateOne(Bson filter,Bson update){
    	UpdateResult result=collection.updateOne(filter, update);
    	return result;
    }
    
    /**
     * 更新查询到的所有文档
     * @param filter 查询条件
     * @param update 更新文档
     * @return 更新结果
     */
    public static UpdateResult updateMany(Bson filter,Bson update){
    	UpdateResult result=collection.updateMany(filter, update);
    	return null;
    }
    
    /**
     * 更新一个文档,结果是replacement是新文档,老文档完全被替换
     * @param filter 查询条件
     * @param replacement 更新文档
     * @return 更新文档
     */
    public static UpdateResult replaceOne(Bson filter,Document replacement){
    	UpdateResult result=collection.replaceOne(filter, replacement);
    	return result;
    }
    
    /**
     * 根据条件删除一个文档
     * @param filter
     */
    public static void deleteOne(Bson filter){
    	collection.deleteOne(filter);
    }
    
    /**
     * 根据条件删除多个文档
     * @param filter 查询条件
     */
    public static void deleteMany(Bson filter){
    	collection.deleteMany(filter);
    }
}
