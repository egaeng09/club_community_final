package web.term.club.service;

import org.springframework.core.io.Resource;
import web.term.club.domain.Club;
import web.term.club.domain.ClubInfo;
import web.term.club.domain.ClubMember;
import web.term.club.domain.Member;
import web.term.club.response.ClubMemberDto;
import web.term.club.response.waitingClubMemberDto;

import java.util.List;

public interface ClubMemberService {
    List<ClubMember> getAllClubMember(Club requestMember) throws Exception;
    //ClubMember applyClubMember(Club club, Member requestMember, )
    List<ClubMemberDto> convertToClubMemberDTOList(List<ClubMember> clubMembers) throws Exception;
    List<waitingClubMemberDto> getWaitingClubMember(Club club, Member requestMember)throws Exception;
    ClubMember approveClubMember(Member requestMember,Long clubId ,Member targetMember) throws Exception;
    ClubMember rejectClubMember(Member requestMember, Long clubId ,Member targetMember) throws Exception;
    ClubMember getClubMember(Long id) throws Exception;
    ClubMember banClubMember(Member requestMember, Long clubId ,Member banedMember) throws Exception;

    ClubMember applyClub(Long id, String name, Long clubId, String filePath);

    Resource getApplyClubFile(ClubMember clubMember, Long requestMemberId) throws Exception;

    ClubMember changeRank(Long requesterId, String role, Long targetMemberId) throws Exception;
}
