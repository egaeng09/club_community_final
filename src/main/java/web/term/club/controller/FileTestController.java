package web.term.club.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import web.term.club.domain.Club;
import web.term.club.domain.ClubInfo;
import web.term.club.domain.ClubMember;
import web.term.club.domain.FilePropertyResponse;
import web.term.club.response.ApiResponse;
import web.term.club.response.ResponseCode;
import web.term.club.service.ClubMemberService;
import web.term.club.service.FilePropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.term.club.service.MemberService;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileTestController {
    @Autowired
    private final FilePropertyService filePropertyService;

    @Autowired
    private final ClubMemberService clubMemberService;

    @GetMapping
    public String fileUpload(){
        return "file-upload";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping("/applyClub")
    // 파일 업로드 -> 가입신청이 될 예정
    public ApiResponse<FilePropertyResponse> uploadFileAndApplyClub(@ModelAttribute web.term.club.controller.ApplyClubForm form) throws IOException {
        MultipartFile attachedFile = form.getAttachedFile();
        String name = form.getName();
        Long id = form.getStudentId();
        Long clubId = form.getClubId();
        String department = form.getDepartment();
        FilePropertyResponse filePropertyResponse = filePropertyService.storeFile(attachedFile);
        ClubMember newClubMember =  clubMemberService.applyClub(id, name, clubId, filePropertyResponse.getFileUrl());

        return ApiResponse.response(ResponseCode.Created, filePropertyResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ApiResponse<FilePropertyResponse> deleteFile(@PathVariable Long id){
        return ApiResponse.response(ResponseCode.OK, filePropertyService.delete(id));
    }
    @Value("${file.root-path}")
    private String basePath;

    // 사용자의 파일 다운로드
    // 파일 경로에 넣어놓은 clubinfo에 저장된 파일명 전달, 검증 완료
    @GetMapping("/download/{clubId}")
    public ResponseEntity<Resource> downloadClubSignUpFile(@PathVariable Long clubId) {
        try {
            System.out.println("clubId = " + clubId);
            Resource file = filePropertyService.loadClubSignUpFile(clubId);
            System.out.println("전송할 파일 = " + file.getFilename());
            return ResponseEntity.ok().body(file);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

