package com.kodonho;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseMain {
	
	public static void main(String[] args) {
		create("제목 라라라", "내용이에요~");
		readAll();
		update(9,"제목수정9999","내용수정9999");
		read(9);
		delete(17);
		deleteRange(10,20);
	}
	
	// 범위삭제
	public static void deleteRange(int from, int to){
		try(Connection conn = connect()){
			String sql = "delete from bbs3 where bbsno >= ? and bbsno <= ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, from);
			pstmt.setInt(2, to);
			pstmt.executeUpdate();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// 삭제
	public static void delete(int bbsno){
		try(Connection conn = connect()){
			String sql = "delete from bbs3 where bbsno = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbsno);
			pstmt.executeUpdate();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// 수정
	public static void update(int bbsno, String title, String content){
		try(Connection conn = connect()){
			String sql = "update bbs3 set title = ? "
					+ "                 , content = ? "
					+ "                 , nDate = now() "
					+ "    where bbsno = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title); // 숫자는 쿼리내에 있는 물음표의 index : 1부터 시작
			pstmt.setString(2, content);
			pstmt.setInt(3, bbsno);
			// 쿼리실행
			pstmt.executeUpdate();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// 읽기 한개만
	public static void read(int bbsno){
		try(Connection conn = connect()){
			String sql = "select * from bbs3 where bbsno = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbsno);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				int id = rs.getInt("bbsno");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String date = rs.getString("nDate");
				System.out.printf("Set = bbsno:%d, title:%s, content:%s, date:%s \n", id,title,content,date);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// 전체 읽기
	public static void readAll(){
		// try - with 문을 사용한 close 처리
		try(Connection conn = connect()){
			// 1. 쿼리작성
			String sql = "select * from bbs3;";
			// 2. 데이터베이스에 쓰기 위한 한개의 실행단위
			Statement stmt = conn.createStatement();
			// 3. 쿼리 실행후 결과셋을 변수에 전달
			ResultSet rs = stmt.executeQuery(sql);
			// 4. 변수에 담긴 결과셋을 반복문을 돌면서 화면에 출력
			while(rs.next()){ // 데이터셋이 있을때 까지 반복
				int id = rs.getInt("bbsno");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String date = rs.getString("nDate");
				System.out.printf("One = bbsno:%d, title:%s, content:%s, date:%s \n", id,title,content,date);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// 입력
	public static void create(String title, String content){

		// try - with 문을 사용한 close 처리
		try(Connection conn = connect()){
			// 2. 쿼리작성
			String sql = "insert into bbs3(title,content,nDate) values(?,?,now());";
			
			// 3. 데이터베이스에 쓰기 위한 한개의 실행단위
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,title);
			pstmt.setString(2, content);
			pstmt.execute();
			
//			// 3. 데이터베이스에 쓰기 위한 한개의 실행단위
//			Statement stmt = conn.createStatement();
//			// 4. 도구를 사용해서 쿼리 실행
//			String sql1 = "insert into bbs3(title,content,nDate) values('제목287826','내용',now());";
//			stmt.execute(sql1);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	// 디비연결
	public static Connection connect(){
		try{
			String id = "root";     // mysql user id
			String pw = "mysql";    // mysql root 비밀번호
			String dbName = "test"; // db 이름
			
			// db 연결 주소 = 프로토콜 // 아이피:포트 / db이름
			String url = "jdbc:mysql://localhost:3306/"+dbName;
			
			// 드라이버 클래스를 동적으로 load 한다
			Class.forName("com.mysql.jdbc.Driver");

			// db 연결객체 생성
			Connection conn = DriverManager.getConnection(url,id,pw);
			System.out.println("Database "+dbName+"에 연결되었습니다.");
			
			return conn;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
