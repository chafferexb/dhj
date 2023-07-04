package com.example.stellarmemo.service;

import com.example.stellarmemo.dao.NoteDao;
import com.example.stellarmemo.pojo.IDSet;
import com.example.stellarmemo.pojo.Note;
import com.example.stellarmemo.pojo.WebResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class NoteOpImpl implements NoteOp{
    @Autowired
    NoteDao noteDao;
    public NoteOpImpl() {
        super();
    }

    //没有上传图片
    @Override
    public WebResult createNote(String user_id,String content,String note_id, String imageSrc) {
        WebResult webResult=new WebResult<>();
        try {
            if(!imageSrc.equals("null")){
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
                String imageName = formatter.format(date);
                File target = new File("D:/IDEA/StellarMemo/src/main/resources/image/" + imageName + ".jpg");
                if (!target.exists()) {
                        target.createNewFile();
                }
                File src = new File(imageSrc);

                FileInputStream fis = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(target);

                int len = 0;
                byte[] data = new byte[20];
                while ((len = fis.read(data)) != -1) {
                    fos.write(data, 0, len);
                }

                fis.close();
                fos.close();
            }
            note_id= IDSet.getShortUuid();
            noteDao.createNote(user_id,content,note_id, imageSrc);
            webResult.success("创建笔记成功");

        }catch (Exception e){
            webResult.error("创建笔记失败");
            System.out.println(e.getMessage());
        }
        return webResult;
    }

    @Override
    public WebResult modifyNote(String note_id,String content) {
        WebResult webResult=new WebResult<>();
        try{
            noteDao.modifyNote(note_id,content);
            webResult.success("更改笔记成功");

        }catch (Exception e){
            webResult.error("更改笔记失败");
            System.out.println(webResult.getMessage());
        }
        return webResult;
    }

    @Override
    public WebResult deleteNote(String note_id) {
        WebResult webResult=new WebResult();
        try{
            noteDao.deleteNote(note_id);
            webResult.success("删除笔记成功");
            System.out.println("删除笔记成功");

        }catch (Exception e){
            webResult.error("删除笔记错误");
            System.out.println(webResult.getMessage());
        }
        return webResult;
    }
    @Override
    public List<Note> searchByKey(String key, int startIndex, int pageSize) {
        try {

            System.out.println(notedao.searchByKey(key, startIndex, pageSize));
        } catch (Exception e) {
            System.out.println("查询失败");
            System.out.println(e);
        }
        return notedao.searchByKey(key, startIndex, pageSize);
    }

    @Override
    public List<Note> searchByTag(String tag1, String tag2, String tag3) {
        try{if (tag2 == "" && tag3 == "" && tag1!="") {
            System.out.println("根据一个tag查询");
            return notedao.searchByTag1(tag1);
        } else if (tag3 == "" && tag1!="" && tag2!="") {
            System.out.println("根据两个tag查询");
            return notedao.searchByTag2(tag1, tag2);
        } else if (tag1!="" && tag2!="" && tag3!=""){
            System.out.println("根据三个tag查询");
            return notedao.searchByTag3(tag1, tag2, tag3);
        }else return null;}
        catch (Exception e){
            System.out.println("查询错误");
            System.out.println(e);
            return null;
        }


    }
}
