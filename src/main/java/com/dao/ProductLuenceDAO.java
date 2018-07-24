package com.dao;

import com.entity.Product;
import com.util.LuceneUtil;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: lenovo
 * \* Date: 2018/7/23
 * \* Time: 13:48
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class ProductLuenceDAO {
    //给商品建立索引
    public void createIndex(Product product) {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        product = new Product(3, "电饭冰箱", 99.0, new Date(), "海尔", "h.jpg");
        Document document = new Document();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(product.getDate());
        document.add(new IntField("id", 3, Field.Store.YES));
        document.add(new StringField("title", product.getTitle(), Field.Store.YES));
        document.add(new DoubleField("price", product.getPrice(), Field.Store.YES));
        document.add(new StringField("date", format, Field.Store.YES));
        document.add(new StringField("description", product.getDescription(), Field.Store.YES));
        document.add(new StringField("picPath", product.getPicPath(), Field.Store.YES));
        try {
            indexWriter.addDocument(document);
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            e.printStackTrace();
            LuceneUtil.commit(indexWriter);
        }
    }
    //搜索， 传入查询条件
    public void searchIndex(String content) {
        IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
        try {
            TopDocs topDocs = indexSearcher.search(new TermQuery(new Term("title", "电饭冰箱")), 10);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            System.out.println("文档总数为："+topDocs.totalHits);
            for (ScoreDoc scoreDoc : scoreDocs) {
                System.out.println("当前得分："+scoreDoc.score);
                Document doc = indexSearcher.doc(scoreDoc.doc);
                System.out.println("^^__^^"+doc.get("id"));
                System.out.println("^^__^^"+doc.get("title"));
                System.out.println("^^__^^"+doc.get("price"));
                System.out.println("^^__^^"+doc.get("date"));
                System.out.println("^^__^^"+doc.get("description"));
                System.out.println("^^__^^"+doc.get("picPath"));
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}

