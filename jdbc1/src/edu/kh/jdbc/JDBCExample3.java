package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample3 {
	public static void main(String[] args) {
		
		// 입력 받은 최소 급여 이상
		// 입력 받은 최대 급여 이하를 받는
		// 사원의 사번, 이름, 급여를 급여 내림차순으로 조회
		// -> 이클립스 콘솔 출력
		
		// [실행화면]
		// 최소 급여 : 1000000
		// 최대 급여 : 3000000
		
		// 사번 / 이름 / 급여
		// 사번 / 이름 / 급여
		// 사번 / 이름 / 급여
		// 사번 / 이름 / 급여
		// 사번 / 이름 / 급여
		// 사번 / 이름 / 급여
		// 사번 / 이름 / 급여
		// ...
		
		Connection conn = null; // DB 연결 정보 저장 객체
		Statement stmt = null;  // SQL 수행, 결과 반환용 객체
		ResultSet rs = null;    // SELECT 수행 결과 저장 객체
								
		Scanner sc = null; 	    // 키보드 입력용 객체
		

		try {
			
			// 2. DriverManager 객체를 이용해서 Connection 객체 생성
			// 2-1) Oracle JDBC Driver 객체 메모리 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2-2) DB 연결 정보 작성
			String type = "jdbc:oracle:thin:@"; // 드라이버의 종류
			String host = "localhost"; // DB 서버 컴퓨터의 IP 또는 도메인주소
			String port = ":1521"; // 프로그램 연결을 위한 port 번호
			String dbName = ":XE"; // DBMS 이름 (XE == eXpress Edition)
			
			String userName = "kh";   	// 사용자 계정명
			String password = "kh1234"; // 계정 비밀번호
			
			// 2-3) DB 연결정보와 DriverManager 를 이용해서 Connection 생성
			conn = DriverManager.getConnection(type + host + port + dbName,
										userName, password);
			
			// 3. SQL 작성
			// 입력 받은 급여 -> Scanner 필요
			// int input 사용
			sc = new Scanner(System.in);
			
			System.out.print("최소 급여 입력 : ");
			int min = sc.nextInt();
			
			System.out.print("최대 급여 입력 : ");
			int max = sc.nextInt();
			
			/*
			String sql = "SELECT EMP_ID, EMP_NAME, SALARY "
					+ "FROM EMPLOYEE "
					+ "WHERE SALARY BETWEEN " + min + " AND " + max + ""
					+ "AND SALARY <= " + max; 
					내가 한거
			*/
			
			/*
			String sql = "SELECT EMP_ID, EMP_NAME, SALARY "
					+ "FROM EMPLOYEE "
					+ "WHERE SALARY BETWEEN " + min + " AND " + max + " ORDER BY SALARY DESC";
			*/
			
			// Java 13 부터 지원하는 Text Block (""") 문법
			// 자동으로 개행 포함 + 문자열 연결이 처리됨
			// 기존처럼 + 연산으로 문자열을연결할 필요가 없음
			String sql = """
					SELECT EMP_ID, EMP_NAME, SALARY
					FROM EMPLOYEE
					WHERE SALARY BETWEEN 
					"""+ min + " AND " + max + " ORDER BY SALARY DESC";
			
			// 4. Statement 객체 생성
			stmt = conn.createStatement();
			
			// 5. SQL 수행 후 결과 반환 받기
			rs = stmt.executeQuery(sql);
			
			// 6. 1행씩 접근해서 컬럼값 얻어오기
			while(rs.next()) {
				
				String empId = rs.getString("Emp_ID");
				String empName = rs.getString("Emp_NAME");
				int salary = rs.getInt("SALARY");
				
				System.out.printf("%s / %s / %d원 \n",
								empId, empName, salary);
				
			}
			
		} catch(Exception e) {
			// 최상위  예외인 Exception을 이용해서 모든 예외를 처리
			// -> 다형성 업캐스팅 적용
			e.printStackTrace();
			
		} finally {
			// 7. 사용 완료된 JDBC 객체 자원 반환(close)
			// -> 생성된 역순으로 close!
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
				if(sc != null) sc.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
