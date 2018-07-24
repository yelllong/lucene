package com.analyzer;

import com.util.LuceneUtil;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.junit.Test;


/**
 * \* Created with IntelliJ IDEA.
 * \* User: lenovo
 * \* Date: 2018/7/23
 * \* Time: 21:48
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class WeightTest {
    //权重处理
    @Test
    public void indexWriter() {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        for (int i = 11; i <= 15; i++) {
            Document document = new Document();
            document.add(new IntField("id", i, Field.Store.YES));
            TextField textField = new TextField("title", "静夜思", Field.Store.YES);
            //加权
            textField.setBoost(10f);//浮点型的段
            document.add(textField);
            document.add(new StringField("author", "李白", Field.Store.YES));
            document.add(new StringField("date", "745-9-9", Field.Store.YES));
            document.add(new TextField("content", "床前明月光，疑是地上霜。举头望明月，低头思故乡。", Field.Store.YES));
            try {
                indexWriter.addDocument(document);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LuceneUtil.commit(indexWriter);
    }
}

