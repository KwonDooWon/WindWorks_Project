package com.kh.ww.community.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.kh.ww.common.model.vo.PageInfo;
import com.kh.ww.common.template.Pagenation;
import com.kh.ww.community.model.service.CommunityService;
import com.kh.ww.community.model.vo.Community;
import com.kh.ww.community.model.vo.CommunityAttachment;
import com.kh.ww.community.model.vo.CommunityBoard;
import com.kh.ww.community.model.vo.CommunityReply;
import com.kh.ww.employee.model.vo.Employee;
import com.kh.ww.studyManagement.model.vo.ClassAttachment;

@Controller
public class CommunityController {
	
	@Autowired
	private CommunityService communityService;
	

	// 커뮤니티 첫 화면 불러오기
	@RequestMapping("list.com")
	public ModelAndView communityList(ModelAndView mv, HttpSession session) {
		mv.setViewName("community/communityList");
		return mv;	
	}
	
	// 내 커뮤니티 조회
	@ResponseBody
	@RequestMapping(value="myComList.com", produces="application/json; charset=UTF-8")
	public String myCommunityList(HttpSession session) {
		Employee e = (Employee)session.getAttribute("loginUser");
		ArrayList<Community> comList = communityService.communityList(e);
		return new Gson().toJson(comList);
	}
	
	// 멤버 조회
	@ResponseBody
	@RequestMapping(value="memberList.com", produces="application/json; charset=UTF-8")
	public String memberList(int comNo) {
		ArrayList<Employee> memberList = communityService.memberList(comNo);
		return new Gson().toJson(memberList);	
	}
	
	
	// 보드 리스트 불러오기
	@ResponseBody
	@RequestMapping(value="boardList.com", produces="application/json; charset=UTF-8")
	public JSONObject boardList(int comNo, int cpage) {
		// 페이지정보
		PageInfo pi = Pagenation.getPageInfo(communityService.boardListCount(comNo), cpage, 10, 5);
		
		// 보드리스트 조회
		ArrayList<CommunityBoard> boardList = communityService.boardList(pi, comNo);
		JSONObject jobj = new JSONObject();
		jobj.put("boardList", boardList);
		jobj.put("pi", pi);
		return jobj;
	}

	// 커뮤니티 보드 디테일  조회
	@ResponseBody
	@RequestMapping(value="board.com", produces="application/json; charset=UTF-8")
	public JSONObject selectBoard(int bno) {
		CommunityBoard board = communityService.selectBoard(bno);
		ArrayList<CommunityAttachment> atList = communityService.boardFile(bno);
		
		JSONObject jobj = new JSONObject();
		jobj.put("board", board);
		jobj.put("atList", atList);

		return jobj;
	}
	
	// 리플, 리리플 리스트 조회
	@ResponseBody
	@RequestMapping(value="replyList.com", produces="application/json; charset=UTF-8")
	public JSONObject selectReplyList(int bno) {
		ArrayList<CommunityReply> replyList = communityService.replyList(bno);
		ArrayList<CommunityReply> reReplyList = communityService.reReplyList(bno);
		
		JSONObject jobj = new JSONObject();
		jobj.put("reList", replyList);
		jobj.put("reReList", reReplyList);	
		return jobj;	
	}
	
	// 댓글 등록
	@ResponseBody
	@RequestMapping(value="replyIn.com")
	public String communityReplyIn(int bno, int eno, String reCont) {
		CommunityReply cr = new CommunityReply();
		cr.setCommunityBoardNo(bno);
		cr.setEmpNo(eno);
		cr.setCommunityReplyComment(reCont);
		return communityService.communityReplyIn(cr) > 0 ? "success" : "fail";
	}
	
	// 댓글 삭제
	@ResponseBody
	@RequestMapping(value="replyDel.com")
	public String communityReplyDel(int rno) {
		return communityService.communityReplyDel(rno) > 0 ? "success" : "fail";
	}
	
	// 대댓글 등록
	@ResponseBody
	@RequestMapping(value="reReplyIn.com")
	public String communityReReplyIn(int replyNo, int boardNo, int empNo, String reReplyContent) {
		CommunityReply cr = new CommunityReply();
		cr.setCommunityBoardNo(boardNo);
		cr.setEmpNo(empNo);
		cr.setCommunityReplyComment(reReplyContent);
		cr.setParentReplyNo(replyNo);
		return communityService.communityReReplyIn(cr) > 0 ? "success" : "fail";
	}
	
	// 댓글, 대댓글 수정
	@ResponseBody
	@RequestMapping(value="replyUp.com")
	public String communityReplyUp(int replyNo, int boardNo, String updateContent) {
		CommunityReply cr = new CommunityReply();
		cr.setCommunityReplyNo(replyNo);
		cr.setCommunityBoardNo(boardNo);
		cr.setCommunityReplyComment(updateContent);
		return communityService.communityReplyUp(cr) > 0 ? "success" : "fail";
	}

	// 전체 커뮤 리스트
	@ResponseBody
	@RequestMapping(value="listAll.com", produces="application/json; charset=UTF-8")
	public JSONObject communityListAll(HttpSession session) {
		ArrayList<Community> communityListAll = communityService.communityListAll();
		
		//비교용 내 커뮤 리스트
		Employee e = (Employee)session.getAttribute("loginUser");
		ArrayList<Community> communityListMy = communityService.communityList(e);
		
		JSONObject jobj = new JSONObject();
		jobj.put("allList", communityListAll);
		jobj.put("myList", communityListMy);	
		
		return jobj;	
	}
	
	// 커뮤 가입
	@ResponseBody
	@RequestMapping(value="comIn.com")
	public String communityIn(int comNo, int empNo) {
		Community c = new Community();
		c.setCommunityNo(comNo);
		c.setEmpNo(empNo);
		return  communityService.communityIn(c) > 0 ? "success" : "fail";
	}
	
	// 커뮤 탈퇴
	@ResponseBody
	@RequestMapping(value="comOut.com")
	public String communityOut(int comNo, int empNo) {
		Community c = new Community();
		c.setCommunityNo(comNo);
		c.setEmpNo(empNo);
		return communityService.communityOut(c) > 0 ? "success" : "fail";
	}
	
	// 커뮤 등록 (커뮤 + 커뮤그룹)
	@ResponseBody
	@RequestMapping(value="create.com")
	public String communityCreate(int empNo, String comName, String comPoint) {
		Community c = new Community();
		c.setEmpNo(empNo);
		c.setCommunityName(comName);
		c.setCommunityPoint(comPoint);	
		
		// 커뮤 등록
		int result = communityService.communityCreate(c);
		int result2 = 0;
		if (result > 0) {
			// 커뮤그룹 등록
			result2 = communityService.communityGroupCreate(c);
		} 
		if (result * result2 > 0) {
			return "success";
		} else {
			return "fail";
		}
	}
	
	// 커뮤 수정
	@ResponseBody
	@RequestMapping(value="update.com")
	public String communityUpdate(String comName, String comPoint, int comNo) {
		Community c = new Community();
		c.setCommunityNo(comNo);
		c.setCommunityName(comName);
		c.setCommunityPoint(comPoint);
		return communityService.communityUpdate(c) > 0 ? "success" : "fail";
	}
	
	// 커뮤 삭제
	@ResponseBody
	@RequestMapping(value="delete.com")
	public String communityDelete(int comNo) {
		Community c = new Community();
		c.setCommunityNo(comNo);
		return communityService.communityDelete(c) > 0 ? "success" : "fail";
	}
	
	// 게시글 작성
	@RequestMapping(value="createBo.com")
	public String comBoardCreate(HttpSession session, CommunityBoard cb, CommunityAttachment ca, MultipartHttpServletRequest file) {
		// 게시글 등록
		int result1 = communityService.comBoardInsert(cb);
		int result2 = 0;
		int i = 1;	
		List<MultipartFile> fileList = file.getFiles("filesss");
		// 파일 등록
		for (MultipartFile f : fileList) {
			if(!f.getOriginalFilename().equals("") && i == 1) {
				String changeName = getSaveFileInfo(f, session, "resources/uploadFiles/community/");
				ca.setCommunityOriginName(f.getOriginalFilename());
				ca.setCommunityChangeName(changeName);
				// ca.setClassFileSize(f.getSize()/1024);
				ca.setCommunityFilePath("resources/uploadFiles/community/"+ changeName);
				ca.setCommunityFileLevel(i);
				result2 = communityService.comBoardAttInsert(ca);
				i = 0;
			}else if(!f.getOriginalFilename().equals("") && i == 0){
				String changeName = getSaveFileInfo(f, session, "resources/uploadFiles/community/");
				ca.setCommunityOriginName(f.getOriginalFilename());
				ca.setCommunityChangeName(changeName);
				// ca.setClassFileSize(f.getSize());
				ca.setCommunityFilePath("resources/uploadFiles/community/"+ changeName);
				ca.setCommunityFileLevel(i);
				result2 = communityService.comBoardAttInsert(ca);
			}
		}
		if((result1 * result2) == 1) {
			return "redirect:/list.com";
		} else {
			return "redirect:/list.com";
		}
	}
	
	// 게시글 삭제
	@RequestMapping(value = "deleteBo.com")
	public String comBoardDelete(HttpSession session, int bno) {

		int result1 = communityService.comBoardDelete(bno);
		System.out.println(result1);
		ArrayList<CommunityAttachment> calist = communityService.selectBoardAttList(bno);
		if(calist.size() > 0) {
			for(CommunityAttachment c : calist) {
				new File(session.getServletContext().getRealPath(c.getCommunityFilePath())).delete();
				int result2 = communityService.deleteBoardAtt(c.getCommunityFileNo());
				System.out.println(result2);
			}
		}
		return "redirect:/list.com";
	}
	

	
	
	
	
	

	public String getSaveFileInfo(MultipartFile upfile, HttpSession session, String path) {
	      // 파일명 수정 후 서버 업로드 시키기("이미지저장용 (2).jpg" => 20231109102712345.jpg)
	      // 년월일시분초 + 랜덤숫자 5개 + 확장자
	      // 원래 파일명
	      String originName = upfile.getOriginalFilename();
	      // 시간정보 (년월일시분초)
	      String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	      // 랜덤숫자 5자리
	      int ranNum = (int) (Math.random() * 90000) + 10000;
	      // 확장자
	      String ext = originName.substring(originName.lastIndexOf("."));
	      // 변경된이름
	      String changeName = currentTime + ranNum + ext;

	      // 첨부파일 저장할 폴더의 물리적인 경우
	      String savePath = session.getServletContext().getRealPath(path);
	      try {
			upfile.transferTo(new File(savePath + changeName));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	      return changeName;
	}


	
	
}

	

	
	

