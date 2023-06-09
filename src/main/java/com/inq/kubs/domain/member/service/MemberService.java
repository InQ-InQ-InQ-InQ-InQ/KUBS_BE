package com.inq.kubs.domain.member.service;

import com.inq.kubs.domain.department.Department;
import com.inq.kubs.domain.department.repository.DepartmentRepository;
import com.inq.kubs.domain.member.Member;
import com.inq.kubs.domain.member.dto.CreateMemberDto;
import com.inq.kubs.domain.member.dto.request.ChangePwRequest;
import com.inq.kubs.domain.member.dto.request.FindPwRequest;
import com.inq.kubs.domain.member.repository.MemberRepository;
import com.inq.kubs.web.exception.ErrorType;
import com.inq.kubs.web.exception.KubsException;
import com.inq.kubs.domain.member.dto.request.CreateMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public Long createMember(CreateMemberRequest request) {

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new KubsException(ErrorType.NOT_EXIST_KEY));

        CreateMemberDto dto = new CreateMemberDto(request);
        dto.registerDepartment(department);
        Member member = Member.createMember(dto);
        memberRepository.save(member);

        return member.getId();
    }

    public void checkFindPwRequest(FindPwRequest request) {

        Member member = memberRepository.findByStudentId(request.getStudentId())
                .orElseThrow(() -> new KubsException(ErrorType.NOT_EXIST_KEY));
        if (!member.getPhoneNumber().equals(request.getPhoneNumber()) ||
                !member.getEmail().equals(request.getEmail())) {
            throw new KubsException(ErrorType.INCONSISTENT_DATA);
        }
    }

    @Transactional
    public void changePw(ChangePwRequest request, Long studentId) {

        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new KubsException(ErrorType.NOT_EXIST_KEY));

        member.changePw(request.getNewPw());
    }

    public Member getMemberWithDepartment(Long id) {
        return memberRepository.findWithDepartmentById(id)
                .orElseThrow(() -> new KubsException(ErrorType.NOT_EXIST_KEY));
    }

    public void validateDuplicatedStudentId(Long studentId) {
        Boolean isExist = memberRepository.existsByStudentId(studentId);
        if (isExist) throw new KubsException(ErrorType.DUPLICATED_STUDENT_ID);
    }
}
