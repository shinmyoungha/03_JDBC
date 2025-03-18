package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample7 {

	public static void main(String[] args) {
		// EMPLOYEE	테이블에서
		// 사번, 이름, 성별, 급여, 직급명, 부서명을 조회
		// 단, 입력 받은 조건에 맞는 결과만 조회하고 정렬할 것
				
		// - 조건 1 : 성별 (M, F)
		// - 조건 2 : 급여 범위
		// - 조건 3 : 급여 오름차순/내림차순
				
		// [실행화면]
		// 조회할 성별(M/F) : F
		// 급여 범위(최소, 최대 순서로 작성) :
		// 3000000
		// 4000000
		// 급여 정렬(1.ASC, 2.DESC) : 2
				
		// 사번 | 이름   | 성별 | 급여    | 직급명 | 부서명
		//--------------------------------------------------------
		// 218  | 이오리 | F    | 3890000 | 사원   | 없음
		// 203  | 송은희 | F    | 3800000 | 차장   | 해외영업2부
		// 212  | 장쯔위 | F    | 3550000 | 대리   | 기술지원부
		// 222  | 이태림 | F    | 3436240 | 대리   | 기술지원부
		// 207  | 하이유 | F    | 3200000 | 과장   | 해외영업1부
		// 210  | 윤은해 | F    | 3000000 | 사원   | 해외영업1부

		// 1. JDBC 객체 참조 변수 선언
		Connection conn = null; 
		PreparedStatement pstmt = null;  
		ResultSet rs = null;   
		
		Scanner sc = null; 	  
		
		try {
			// 2. DriverManager 객체를 이용해서 Connection 객체 생성
			// 2-1) Oracle JDBC Driver 객체 메모리 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
						
			// 2-2) DB 연결 정보 작성
			String url = "jdbc:oracle:thin:@localhost:1521:XE"; 
			String userName = "kh";   	// 사용자 계정명
			String password = "kh1234"; // 계정 비밀번호
			
			conn = DriverManager.getConnection(url,	userName, password);
			
			// 3. SQL 작성
			sc = new Scanner(System.in);
			System.out.print("조회할 성별(M/F) : ");
			String gender = sc.next().toUpperCase();
			
			System.out.println("급여 범위(최소, 최대 순서로 작성) : ");
			int min = sc.nextInt();
			int max = sc.nextInt();
			
			System.out.print("급여 정렬(1.ASC, 2.DESC) : ");
			int sort = sc.nextInt();
			
			String sql = """
					SELECT EMP_ID, EMP_NAME, 
					DECODE( SUBSTR(EMP_NO, 8, 1), '1', 'M', '2', 'F' ) GENDER,
					SALARY,
					JOB_NAME,
					NVL(DEPT_TITLE, '없음') DEPT_TITLE
					FROM EMPLOYEE
					JOIN JOB USING(JOB_CODE)
					LEFT JOIN DEPARTMENT ON(DEPT_CODE = DEPT_ID)
					WHERE 
						DECODE( SUBSTR(EMP_NO, 8, 1), '1', 'M', '2', 'F' ) = ?
					AND 
						SALARY BETWEEN ? AND ?
					ORDER BY SALARY
					""";
			// -> placeholder -> String -> 리터럴표기법 ''
			// -> int -> 값
		
			// 입력 받은 정렬(sort) 값에 따라서 sql에
			// 오름/내림 차순 SQL 추가하기
			if(sort == 1) sql += " ASC";
			else 		  sql += " DESC";
			
			// 4. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 5. ? (위치홀더)에 알맞은 값 세팅
			pstmt.setString(1, gender);
			pstmt.setInt(2, min);
			pstmt.setInt(3, max);
			
			// 6. SQL 수행 후 결과 반환 받기
			rs = pstmt.executeQuery();
			
			// 7. 커서를 이용해서 한 행씩 접근하여 컬럼 값 얻어오기
			System.out.println("사번 | 이름   | 성별 | 급여    | 직급명 | 부서명");
			System.out.println("--------------------------------------------------------");
			
			boolean flag = true; // true : 조회결과가 없음 , false : 조회 결과가 존재함!!
			
			while(rs.next()) {
				flag = false; // while 1회 이상 반복함 == 조회결과가 1행이라도 있다
				
				String empId     = rs.getString("EMP_ID");
				String empName   = rs.getString("EMP_NAME");
				String gen       = rs.getString("GENDER");
				int salary       = rs.getInt("SALARY");
				String jobName   = rs.getString("JOB_NAME");
				String deptTitle = rs.getString("DEPT_TITLE");
				
				System.out.printf("%-4s | %3s | %-4s | %7d | %-3s  | %s \n",
						empId, empName, gen, salary, jobName, deptTitle);
								
			}
			
			if(flag) { // flag == true인 경우 -> while문 수행 X -> 조회결과가 없음
				System.out.println("조회 결과 없음");
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			// 8.  사용한 JDBC 객체 자원 반환
			
			try {
				
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		

	}

}