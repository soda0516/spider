package soda.cantfind.apply.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/24 20:09
 **/
public class TxtReaderUtil {
    public static List<String> readTaobaoUrl(String filePath,Integer start,Integer pageNumber){
        List<String> list = new ArrayList<>();
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))
        ) {
            while (bufferedReader.ready()){
                list.add(bufferedReader.readLine());
//                System.out.println(bufferedReader.readLine());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == start || null == pageNumber){
            return list;
        }else {

        }
        return list.subList(start,pageNumber);
    }
    public static List<String> readTaobaoUrl(String filePath){
        List<String> list = new ArrayList<>();
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))
        ) {
            while (bufferedReader.ready()){
                list.add(bufferedReader.readLine());
//                System.out.println(bufferedReader.readLine());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
