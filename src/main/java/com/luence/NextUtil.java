package com.luence;

import com.util.LuceneUtil;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
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
 * \* Date: 2018/7/24
 * \* Time: 16:07
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class NextUtil {
    @Test
    public void indexWriter() {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        Document document = new Document();
        for (int i = 1; i <= 5; i++) {
            document.add(new IntField("id", i, Field.Store.YES));
            document.add(new StringField("title", "七步诗", Field.Store.YES));
            document.add(new StringField("author", "曹植", Field.Store.YES));
            TextField field = new TextField("content", "煮豆燃豆萁，豆在釜中泣。本是同根生，相煎何太急。", Field.Store.YES);
            field.setBoost(10f);
            document.add(field);
            try {
                indexWriter.addDocument(document);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LuceneUtil.commit(indexWriter);
    }
    @Test
    public void search() throws IOException {
        IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
        TopDocs topDocs = indexSearcher.search(new TermQuery(new Term("title", "绝句")), 10);
        System.out.println("文档总数为："+topDocs.totalHits);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            float score = scoreDoc.score;
            System.out.println("文档得分："+score);
            int doc = scoreDoc.doc;
            Document doc1 = indexSearcher.doc(doc);
            for (IndexableField field : doc1.getFields()) {
                System.out.println(field.name()+":"+doc1.get(field.name()));
            }
        }
    }
    @Test
    public void updateIndex() throws IOException {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        Term term = new Term("id");
        Document doc = new Document();
        doc.add(new IntField("id", 2, Field.Store.YES));
        doc.add(new StringField("title", "绝句", Field.Store.YES));
        doc.add(new StringField("author", "匿名", Field.Store.YES));
        doc.add(new StringField("content", "黑云压城城欲摧，漫天黄沙石乱走", Field.Store.YES));
        indexWriter.updateDocument(term, doc);
        LuceneUtil.commit(indexWriter);
    }
    @Test
    public void delete() throws IOException {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        indexWriter.deleteAll();
        LuceneUtil.commit(indexWriter);
    }
}

