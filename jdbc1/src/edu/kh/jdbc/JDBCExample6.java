package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample6 {

	public static void main(String[] args) {
		
		// 아이디, 비밀번호, 이름을 입력받아
		// 아이디, 비밀번호가 일치하는 사용자의
		// 이름을 수정(UPDATE)
		
		// 1. PreparedStatement 이용하기
		// 2. commit/rollback 처리하기
		// 3. 성공 시 "수정 성공!" 출력 / 실패 시 "아이디 도는 비밀번호 불일치" 출력
		
		// 1) JDBC 객체 참조변수 선언 + 키보드 입력용 객체 sc 선언
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		// SELECT 가 아니기 때문에 ResultSet 필요 없음!
		
		Scanner sc = null;
		
		try {
			// 2. DriverManager 객체를 이용해서 Connection 객체 생성
			// 2-1) OracleDriver 객체 메모리 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
						
			// 2-2) DB 연결 정보 작성
			String url = "jdbc:oracle:thin:@localhost:1521:XE"; 
			String userName = "kh";   	// 사용자 계정명
			String password = "kh1234"; // 계정 비밀번호
						
			// 2-3) DB 연결정보와 DriverManager 를 이용해서 Connection 생성
			conn = DriverManager.getConnection(url,	userName, password);
		
			// 3. SQL 작성 + AutoCommit 끄기
			conn.setAutoCommit(false);
			
			sc = new Scanner(System.in);
			
			System.out.print("아이디 입력 : ");
			String id = sc.nextLine();
			
			System.out.print("비밀번호 입력 : ");
			String pw = sc.nextLine();
			
			System.out.print("수정할 이름 입력 : ");
			String name = sc.nextLine();
			
			String sql = """
					UPDATE TB_USER SET 
					USER_NAME = ?
					WHERE USER_ID = ?
					AND USER_PW = ?
					""";
			
			// 4. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 5. ? 위치홀더 알맞은 값 대입
			// pstmt.set자료형(?순서, 대입할 값)
			pstmt.setString(1, name);
			pstmt.setString(2, id);
			pstmt.setString(3, pw);
			// -> 여기까지 작성하면 SQL 완료된 상태!
			
			
			
			// 6. SQL(INSERT) 수행 후 결과(int) 반환 받기
			// executeQuery() : SELECT 수향, ResultSet 반환
			// executeUpdate() : DML 수행, 결과 행 갯수(int) 반환
			// -> 보통 DML 실패 0, 성공 시 0 초과된 값이 반환된다
			
			// pstmt 에서 executeQuery(매개변수), 
			// executeUpdate(매개변수) 매개변수 자리에 아무것도 없어야 한다!
			int result = pstmt.executeUpdate();
			
			// 7. result 값에 따른 결과 처리 + commit/rollback
			if(result > 0) { // INSERT 성공 시
				System.out.println("수정 성공!");
				conn.commit(); // INSERT 성공 시 COMMIT 수행 -> DB에 INSERT 영구 반영
				
			} else { // INSERT 실패
				System.out.println("추가 실패");
				conn.rollback(); // 실패 시 ROLLBACK 으아악 하기싫어
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		
		} finally {
			
			// 8. 사용한 JDBC 객체 자원 반환
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
				if(sc != null) sc.close();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		
		
		
		
	}
}