package FileDataBase;

import java.util.*;
import java.io.*;
import java.sql.Timestamp;
import org.json.simple.JSONObject;

public class FileDB {

    File dbFile;
    HashMap<String, ValueObject> keyValuePairs = new HashMap<String, ValueObject>();
    boolean createInProgress=false;
    boolean deleteInProgress=false;

    FileDB(){
        this.dbFile = new File("C:/Users/DataBaseFile.txt");
    }

    FileDB(String path){
        this.dbFile = new File(path);
    }

    private boolean isValidAction(){
        if(createInProgress==true){
            System.out.println("Other client is creating");
            return false;
        }
        if(deleteInProgress==true){
            System.out.println("Other client is deleting");
            return false;
        }
        return true;
    }

    public void create(String key, JSONObject value, int TTL) {
        if(!isValidAction()) return;

        if(dbFile.length()>1024*1024*1024){
            System.out.println("Space limit Exceeded");
            return;
        }
        createInProgress=true;
        BufferedWriter br;
        try{
            if(keyValuePairs.containsKey(key)) {
                ValueObject obj = keyValuePairs.get(key);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                if(obj.TTL>timestamp.getTime()) {
                    System.out.println("The key already exists");
                }else{
                    keyValuePairs.remove(key);
                }
            }
            
           if(!keyValuePairs.containsKey(key)){

               Timestamp update = new Timestamp(System.currentTimeMillis());
               long getTime = update.getTime();
               ValueObject ob = new ValueObject(value,getTime+TTL);

               keyValuePairs.put(key,ob);

               PrintWriter writer = new PrintWriter(dbFile);
               writer.print("");
               writer.close();

               br = new BufferedWriter(new FileWriter(dbFile));
               for(Map.Entry<String, ValueObject> entry : keyValuePairs.entrySet()){
                    br.write( entry.getKey() + ":" + entry.getValue() );
                    br.newLine();
                }
                System.out.println("created");
               br.flush();
               br.close();
           }
        }catch(Exception e){
            System.out.println(e);
        }finally{
            createInProgress=false;
        }
    }
    public JSONObject read(String key) {
        if(!isValidAction()) return null;

        BufferedWriter br;
        try{
            if(keyValuePairs.containsKey(key)) {
                ValueObject obj = keyValuePairs.get(key);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                if(obj.TTL>timestamp.getTime()) {
                    return obj.value;                    
                }else{
                    keyValuePairs.remove(key);

                    PrintWriter writer = new PrintWriter(dbFile);
                    writer.print("");
                    writer.close();

                    br = new BufferedWriter(new FileWriter(dbFile));
                    for(Map.Entry<String, ValueObject> entry : keyValuePairs.entrySet()){
                        br.write( entry.getKey() + ":" + entry.getValue() );
                        br.newLine();
                    }
                    br.flush();
                    br.close();
                }
            }else{
                System.out.println("The key does not exist");
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
     public void delete(String key) {
        if(!isValidAction()) return;
        deleteInProgress=true;
        BufferedWriter br;                                                                                                                              
         try{
            if(keyValuePairs.containsKey(key)){
                ValueObject obj = keyValuePairs.get(key);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                keyValuePairs.remove(key);

                PrintWriter writer = new PrintWriter(dbFile);
                writer.print("");
                writer.close();

                br = new BufferedWriter(new FileWriter(dbFile));
                for(Map.Entry<String, ValueObject> entry : keyValuePairs.entrySet()){
                    br.write( entry.getKey() + ":" + entry.getValue() );
                    br.newLine();
                }
                br.flush();
                br.close();

                if(obj.TTL<=timestamp.getTime()) {
                    System.out.println("The key does not exists");
                }
            }else{
                System.out.println("The key does not exists");
            }
        }catch(Exception e){
            System.out.println(e);
        }finally{
            deleteInProgress=false;
        }
     }
    public static void main(String args[]) {
        JSONObject obj = new JSONObject();
        FileDB a = new FileDB("C:/Users/nisha/Desktop/Freshworks/DataBaseFile.txt");
        a.create("Hello",obj,200);
        a.create("HI",obj,200);
        a.create("Hey",obj,200);
        a.create("Guten",obj,200);
        a.read("hello");
        a.delete("hello");
        a.create("Hello",obj,100);
    }
}