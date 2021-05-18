package spring5_db_study;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import spring5_db_study.config.AppCtx;
import spring5_db_study.spring.ChangePasswordService;
import spring5_db_study.spring.Member;
import spring5_db_study.spring.MemberDao;
import spring5_db_study.spring.MemberNotFoundException;
import spring5_db_study.spring.WrongIdPasswordException;

public class MainForMemberDao {

	private static MemberDao memberDao;

	public static void main(String[] args) throws IOException {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppCtx.class);) {
			memberDao = ctx.getBean(MemberDao.class);
			memberDao.insert(new Member("test@test.co.kr", "1234", "test", LocalDateTime.now()));
			System.out.println("회원을 추가했습니다.\n");

			ChangePasswordService cps = ctx.getBean(ChangePasswordService.class);
			cps.changePassword("test@test.co.kr", "1234", "new1234");
			System.out.println("암호를 변경했습니다.\n");
			Member member = memberDao.selectByEmail("test@test.co.kr");
			memberDao.delete(member);
			System.out.println("회원을 삭제했습니다.\n");

		} catch (MemberNotFoundException e) {
			System.err.println("존재하지 않는 이메일입니다.\n");
		} catch (WrongIdPasswordException e) {
			System.err.println("이메일과 암호가 일치하지 않습니다.\n");

//			selectAll();
//			insertMember();
//			selectAll();
//			updateMember();
//			selectAll();
//			deleteMember();
//			selectAll();
		}
	}

	private static void deleteMember() {
		System.out.println("-----deleteMember");
		Member member = memberDao.selectByEmail("test@test.co.kr");
		memberDao.delete(member);

	}

	private static void updateMember() {
		System.out.println("-----updateMember");
		Member member = memberDao.selectByEmail("test@test.co.kr");
		String oldPw = member.getPassword();
		String newPw = Double.toHexString(Math.random());
		member.changePassword(oldPw, newPw);

		memberDao.update(member);
		System.out.println("암호 변경 : " + oldPw + ">" + newPw);
		System.out.println(member.getId() + "데이터 삭제");

	}

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHMMss");

	private static void insertMember() {
		System.out.println("-----insertMember");
		String prefix = formatter.format(LocalDateTime.now());
		Member member = new Member(prefix + "@test.co.kr", prefix, prefix, LocalDateTime.now());
		memberDao.insert(member);
		System.out.println(member.getId() + " 데이터 추가");
	}

	private static void selectAll() {
		System.out.println("----- selectAll");
		int total = memberDao.count();
		System.out.println("전체 데이터 : " + total);
		List<Member> members = memberDao.selectAll();
		for (Member member : members) {
			System.out.printf("%d : %s : %s%n", member.getId(), member.getEmail(), member.getName());
		}
	}

}
