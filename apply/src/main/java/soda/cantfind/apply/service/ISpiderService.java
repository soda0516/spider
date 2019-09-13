package soda.cantfind.apply.service;


import org.springframework.http.ResponseEntity;
import soda.cantfind.apply.entity.tusou.TusouSpiderTask;

public interface ISpiderService {
    String getJsonFromUrl(String url);
    String getStringFromUrl(String url);
    TusouSpiderTask activateOneTask(TusouSpiderTask task);
    ResponseEntity downloadExcel(String content);
}
