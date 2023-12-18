<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>

<!-- CSS -->
<link rel="stylesheet" href="resources/notice/css/notice-list-view.css">

<!-- bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- SweetAlert -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body onload="noticeOnload()">
<jsp:include page="../common/header.jsp" />
	<c:if test="${!empty alertMsg}">
		<script>
			swal("", "${alertMsg}", "success");
			/* alertify.alert('알림', "${alertMsg}"); */
		</script>
		<c:remove var="alertMsg" scope="session" />
	</c:if>
	
	<div class="menu">
		<div class="notice-area">
			<div class="notice-top">공지사항</div>
			
			<!-- 작성하기 버튼 -->
			<div class="notice-write-top">
			
				<c:choose>
					<c:when test="${loginUser.deptCode ne 'D5'}">
						<div class="notice-write">
							<button class="notice-write-btn" type="button" onclick="location.href='enrollForm.no'">작성하기</button>
						</div>
					</c:when>
					<c:otherwise>
						<div style="height: 54px;"></div>
					</c:otherwise>
				</c:choose>
				
			</div>
				
			<!-- 공지사항 리스트 -->
			<div class="notice-content">
				<table class="notice-table">
					<thead>
						<tr>
							<th>번호</th>
							<th>제목</th>
							<th>작성자</th>
							<th>조회수</th>
							<th>작성일</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="n" items="${list}">
							<tr onclick="location.href='detail.no?nno=${n.noticeNo}'">
								<td>${n.noticeNo}</td>
								<td>
									<div class="notice-title">
									${n.noticeTitle}
										<c:if test="${not empty n.noticeOriginName}">
											<div class="notice-attachment-icon">
												<ion-icon class="attachment-icon" name="document-attach-outline"></ion-icon>
											</div>
										</c:if>
										<div class="notice-reply-count">
											<span id="rcount"><!-- 댓글갯수 --></span>
										</div>
									</div>
								</td>
								<td>${n.empName}</td>
								<td>${n.noticeCount}</td>
								<td>${n.noticeCreateDate}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			
			<!-- 페이지네이션 -->
			<div class="notice-pagenation">
				<ul id="pagination" class="pagination">
					
					<c:choose>
						<c:when test="${pi.currentPage eq 1}">
							<li class="page-item disabled"><a class="page-link">&lt;</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a class="page-link" href="list.no?cpage=${pi.currentPage - 1}">&lt;</a></li>
						</c:otherwise>
					</c:choose>
					
					<c:forEach var="p" begin="${pi.startPage}" end="${pi.endPage}">
						<li class="page-item"><a class="page-link" href="list.no?cpage=${p}">${p}</a></li>
					</c:forEach>
					
					<c:choose>
						<c:when test="${pi.currentPage eq pi.maxPage}">
							<li class="page-item disabled"><a class="page-link">&gt;</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a class="page-link" href="list.no?cpage=${pi.currentPage + 1}">&gt;</a></li>
						</c:otherwise>
					</c:choose>
					
				</ul>
			</div>
			
			<script>
			


			</script>
			
			
			
		</div>
	</div>
	
</body>
</html>