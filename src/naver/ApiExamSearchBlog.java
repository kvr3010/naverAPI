package naver;
//네이버 검색 API 예제 - blog 검색
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class ApiExamSearchBlog {

 public static void main(String[] args) {
     String clientId = ""; //애플리케이션 클라이언트 아이디값"
     String clientSecret = ""; //애플리케이션 클라이언트 시크릿값"

     String text = null;
     try {
         text = URLEncoder.encode("폭우", "UTF-8");
     } catch (UnsupportedEncodingException e) {
         throw new RuntimeException("검색어 인코딩 실패",e);
     }

     String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text;    // json 결과
     //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과

     Map<String, String> requestHeaders = new HashMap<>();
     requestHeaders.put("X-Naver-Client-Id", clientId);
     requestHeaders.put("X-Naver-Client-Secret", clientSecret);
     
     
     
     String responseBody = get(apiURL,requestHeaders);

     System.out.println(responseBody);
     
     
     JsonParser Parser = new JsonParser();
     JsonObject jsonObj = (JsonObject) Parser.parse(responseBody);
     JsonArray memberArray = (JsonArray) jsonObj.get("items");
 
     
     NewsDto dto = new NewsDto();
     NewsDao dao = new NewsDao();
     for (int i = 0; i < memberArray.size(); i++) {
    	 JsonObject object = (JsonObject) memberArray.get(i);
    		dto.setTitle(object.get("title").getAsString());     
    		dto.setOglink(object.get("originallink").getAsString());
    		dto.setLink(object.get("link").getAsString());
    		dto.setDesc(object.get("description").getAsString());
    		dto.setPubDate(object.get("pubDate").getAsString());
    		dao.isert(dto);

    		
    		System.out.println(i+1+".저장완료");
    	 }
 
 
 }

 private static String get(String apiUrl, Map<String, String> requestHeaders){
     HttpURLConnection con = connect(apiUrl);
     try {
         con.setRequestMethod("GET");
         for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
             con.setRequestProperty(header.getKey(), header.getValue());
         }

         int responseCode = con.getResponseCode();
         if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
             return readBody(con.getInputStream());
         } else { // 에러 발생
             return readBody(con.getErrorStream());
         }
     } catch (IOException e) {
         throw new RuntimeException("API 요청과 응답 실패", e);
     } finally {
         con.disconnect();
     }
 }

 private static HttpURLConnection connect(String apiUrl){
     try {
         URL url = new URL(apiUrl);
         return (HttpURLConnection)url.openConnection();
     } catch (MalformedURLException e) {
         throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
     } catch (IOException e) {
         throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
     }
 }

 private static String readBody(InputStream body){
     InputStreamReader streamReader = new InputStreamReader(body);

     try (BufferedReader lineReader = new BufferedReader(streamReader)) {
         StringBuilder responseBody = new StringBuilder();

         String line;
         while ((line = lineReader.readLine()) != null) {
             responseBody.append(line);
         }

         return responseBody.toString();
     } catch (IOException e) {
         throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
     }
 }
}



