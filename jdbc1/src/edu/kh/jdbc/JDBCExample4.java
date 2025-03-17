package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample4 {

	public static void main(String[] args) {

		// 부서명을 입력받아
		// 해당 부서에 근무하는 사원의
		// 사번, 이름, 부서명, 직급명을
		// 직급코드 오름차순을 조회
		
		// [실행화면]
		// 부서명 입력 : 총무부
		// 200 / 선동일 / 총무부 / 대표
		// 202 / 노옹철 / 총무부 / 부사장
		// 201 / 송종기 / 총무부 / 부사장
		
		// 부서명 입력 : 개발팀
		// 일치하는 부서가 없습니다!
		
		// hint : SQL에서 문자열은 양쪽 '' (홑따옴표) 필요
		// ex) 총무부 입력 -> '총무부'
		
		Connection conn = null; // DB 연결 정보 저장 객체
		Statement stmt = null;  // SQL 수행, 결과 반환용 객체
		ResultSet rs = null;    // SELECT 수행 결과 저장 객체
								
		Scanner sc = null; 	    // 키보드 입력용 객체
		
		try {
			
			// 2. DriverManager 객체를 이용해서 Connection 객체 생성
			// 2-1) Oracle JDBC Driver 객체 메모리 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2-2) DB 연결 정보 작성
			String url = "jdbc:oracle:thin:@localhost:1521:XE"; 
			String userName = "kh";   	// 사용자 계정명
			String password = "kh1234"; // 계정 비밀번호
			
			// 2-3) DB 연결정보와 DriverManager 를 이용해서 Connection 생성
			conn = DriverManager.getConnection(url,	userName, password);
			
			sc = new Scanner(System.in);
			System.out.print("부서명 입력 : ");
			String input = sc.nextLine();
			
			String sql = """
					SELECT EMP_ID, EMP_NAME, DEPT_TITLE, JOB_NAME
					FROM EMPLOYEE
					JOIN JOB ON(EMPLOYEE.JOB_CODE = JOB.JOB_CODE)
					LEFT JOIN DEPARTMENT ON(DEPT_CODE = DEPT_ID)
					WHERE DEPT_TITLE = '""" + input + "' ORDER BY EMPLOYEE.JOB_CODE";
			
			// 4. Statement 객체 생성
			stmt = conn.createStatement();
			
			// 5. Statement 객체를 이용하여 SQL 수행 후 결과 반환 받기
			// executeQuery() : SELECT실행, ResultSet 반환
			// executeUpdate() : DML 실행, 결과 행의 갯수 반환(int)
			rs = stmt.executeQuery(sql);
			
			/* 
			 * 플래그 이용법
			 
			boolean flag = true;
			// 조회결과가 있다면 false, 없으면 true
			
			while(flag) {
				
				flag = false;
				
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String depttitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				
				System.out.printf("%s / %s / %s / %s \n",
								empId, empName, depttitle, jobName);
			}
			
			if(flag) {
				System.out.println("일치하는 부서가 없습니다!");
				
			}
			*/
			
			// return 이용법
			if(!rs.next()) { // 조회 결과가 없다면
				System.out.println("일치하는 부서가 없습니다!");
				return;
				// return 구문이 실행되면 아래는 실행이 안되지만 
				// finally 구문은 실행함
				
			}
			
			do {
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String depttitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				
				System.out.printf("%s / %s / %s / %s \n",
								empId, empName, depttitle, jobName);
			
			} while(rs.next());
			
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