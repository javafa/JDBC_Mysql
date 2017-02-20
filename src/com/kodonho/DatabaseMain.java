package com.kodonho;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseMain {
	
	public static void main(String[] args) {
		create("���� ����", "�����̿���~");
		readAll();
		update(9,"�������9999","�������9999");
		read(9);
		delete(17);
		deleteRange(10,20);
	}
	
	// ��������
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
	
	// ����
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
	
	// ����
	public static void update(int bbsno, String title, String content){
		try(Connection conn = connect()){
			String sql = "update bbs3 set title = ? "
					+ "                 , content = ? "
					+ "                 , nDate = now() "
					+ "    where bbsno = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title); // ���ڴ� �������� �ִ� ����ǥ�� index : 1���� ����
			pstmt.setString(2, content);
			pstmt.setInt(3, bbsno);
			// ��������
			pstmt.executeUpdate();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// �б� �Ѱ���
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
	
	// ��ü �б�
	public static void readAll(){
		// try - with ���� ����� close ó��
		try(Connection conn = connect()){
			// 1. �����ۼ�
			String sql = "select * from bbs3;";
			// 2. �����ͺ��̽��� ���� ���� �Ѱ��� �������
			Statement stmt = conn.createStatement();
			// 3. ���� ������ ������� ������ ����
			ResultSet rs = stmt.executeQuery(sql);
			// 4. ������ ��� ������� �ݺ����� ���鼭 ȭ�鿡 ���
			while(rs.next()){ // �����ͼ��� ������ ���� �ݺ�
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
	
	// �Է�
	public static void create(String title, String content){

		// try - with ���� ����� close ó��
		try(Connection conn = connect()){
			// 2. �����ۼ�
			String sql = "insert into bbs3(title,content,nDate) values(?,?,now());";
			
			// 3. �����ͺ��̽��� ���� ���� �Ѱ��� �������
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,title);
			pstmt.setString(2, content);
			pstmt.execute();
			
//			// 3. �����ͺ��̽��� ���� ���� �Ѱ��� �������
//			Statement stmt = conn.createStatement();
//			// 4. ������ ����ؼ� ���� ����
//			String sql1 = "insert into bbs3(title,content,nDate) values('����287826','����',now());";
//			stmt.execute(sql1);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	// ��񿬰�
	public static Connection connect(){
		try{
			String id = "root";     // mysql user id
			String pw = "mysql";    // mysql root ��й�ȣ
			String dbName = "test"; // db �̸�
			
			// db ���� �ּ� = �������� // ������:��Ʈ / db�̸�
			String url = "jdbc:mysql://localhost:3306/"+dbName;
			
			// ����̹� Ŭ������ �������� load �Ѵ�
			Class.forName("com.mysql.jdbc.Driver");

			// db ���ᰴü ����
			Connection conn = DriverManager.getConnection(url,id,pw);
			System.out.println("Database "+dbName+"�� ����Ǿ����ϴ�.");
			
			return conn;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
