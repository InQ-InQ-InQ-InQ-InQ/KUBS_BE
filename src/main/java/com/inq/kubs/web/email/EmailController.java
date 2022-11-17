package com.inq.kubs.web.email;

import com.inq.kubs.domain.email.EmailService;
import com.inq.kubs.domain.email.MailDto;
import com.inq.kubs.web.common.consts.SessionConst;
import com.inq.kubs.web.common.logic.CommonMethod;
import com.inq.kubs.web.common.response.Success;
import com.inq.kubs.web.exception.ErrorType;
import com.inq.kubs.web.exception.KubsException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email/validation")
    @Operation(summary = "검증 이메일 발송", description = "이메일을 입력받아 해당 이메일로 검증코드가 담긴 메일을 발송한다.")
    public ResponseEntity<Success> sendValidationMail(@RequestParam String email,
                                                      HttpServletRequest request) {

        CommonMethod.registerKeyAndSendMail(email, request, emailService);

        return new ResponseEntity<>(new Success(true), HttpStatus.OK);
    }

    @GetMapping("/email/validation")
    @Operation(summary = "검증코드 확인", description = "검증코드를 입력받아 올바른 코드인지 확인한다.")
    public ResponseEntity<Success> validateKey(@RequestParam("key") String inputKey,
                                               HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        String key = (String) session.getAttribute(SessionConst.V_KEY);

        if (key.equals(inputKey)) {
            return new ResponseEntity<>(new Success(true), HttpStatus.OK);
        } else throw new KubsException(ErrorType.INCONSISTENT_KEY);
    }
}