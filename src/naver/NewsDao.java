package naver;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;

public class NewsDao {
	
	

	public void isert(NewsDto dto) {
		
		
		Connection conn = null; // DB연결 
		PreparedStatement pstmt = null;
		// 디비 연결
		// 쿼리 만들고 실행
		
		try{
			conn = DB.conn();
			String sql = "INSERT INTO naverapi (title, oglink, link, descc, pubDate) VALUES (?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getOglink());
			pstmt.setString(3, dto.getLink());
			pstmt.setString(4, dto.getDesc());
			pstmt.setString(5, dto.getPubDate());
			
			int count = pstmt.executeUpdate();
			if( count == 0 ){
				System.out.println("실패");
			}
			else{
				System.out.println("성공");
			}
			
			
		}
		
		catch(Exception e){
			System.out.println("데이터 입력 에러3: " + e);
		}
		finally{
			try{
				if( conn != null && !conn.isClosed()){
					conn.close();
				}
				if( pstmt != null && !pstmt.isClosed()){
					pstmt.close();
				}        
			}
			catch( SQLException e){
				e.printStackTrace();
			}
		}



	}
}




