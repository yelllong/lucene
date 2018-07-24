package com.luence;


import com.util.LuceneUtil;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import java.io.IOException;


/**
 * \* Created with IntelliJ IDEA.
 * \* User: lenovo
 * \* Date: 2018/7/23
 * \* Time: 12:21
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class UtilsTest {
    @Test
    public void indexWriter() {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        Document document = new Document();
        document.add(new StringField("id","1", Field.Store.YES));
        document.add(new StringField("title","背影",Field.Store.YES));
        document.add(new StringField("author","朱自清",Field.Store.YES));
        document.add(new StringField("date","2000",Field.Store.YES));
        document.add(new TextField("content","xxxx", Field.Store.YES));
        try {
            indexWriter.addDocument(document);
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            e.printStackTrace();
            LuceneUtil.rollback(indexWriter);
        }
    }
    @Test
    public void indexSearcher() {
        IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
        try {
            TopDocs topDocs = indexSearcher.search(new TermQuery(new Term("author", "李白")), 10);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            System.out.println("文档数量："+topDocs.totalHits);
            for (ScoreDoc scoreDoc : scoreDocs) {
                //对应文档的id
                int doc = scoreDoc.doc;
                float score = scoreDoc.score;
                System.out.println("文档的得分："+score);
                Document doc1 = indexSearcher.doc(doc);
                System.out.println("^_^"+doc1.get("id"));
                System.out.println("^_^"+doc1.get("title"));
                System.out.println("^_^"+doc1.get("author"));
                System.out.println("^_^"+doc1.get("content"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //更新
    @Test
    public void modifyIndex() {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        Term term = new Term("id", "1");
        Document document = new Document();
        document.add(new StringField("id", "1", Field.Store.YES));
        document.add(new StringField("title", "沁园春·雪", Field.Store.YES));
        document.add(new StringField("author", "毛泽东", Field.Store.YES));
        document.add(new StringField("content", "北国风光，千里冰封，万里雪飘", Field.Store.YES));
        try {
            indexWriter.updateDocument(term, document);
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            e.printStackTrace();
            LuceneUtil.rollback(indexWriter);
        }
    }
    //删除
    @Test
    public void deleteIndex() {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        try {
            //indexWriter.deleteDocuments(new Term("id", "2"));
            indexWriter.deleteAll();
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            e.printStackTrace();
            LuceneUtil.rollback(indexWriter);
        }
    }
}

