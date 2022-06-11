package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_실패;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.토큰_인증_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.토큰_인증_실패;
import static nextstep.subway.member.MemberAcceptanceTestMethod.로그인한_회원_정보_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_생성을_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String 손성현_EMAIL = "chosunci@gmail.com";
    private static final String 손성현_PASSWORD = "1111";
    private static final int 손성현_AGE = 35;

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(손성현_EMAIL, 손성현_PASSWORD, 손성현_AGE);
    }

    /**
     * When. 가입된 회원정보로 로그인을 시도한다.
     * Then. 로그인이 성공한다.
     */
    @DisplayName("가입된 회원정보로 로그인이 성공되면 Access Token을 발급받는다.")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(TokenRequest.of(손성현_EMAIL, 손성현_PASSWORD));

        // then
        로그인_됨(response);
    }

    /**
     * Given. 가임되지 않은 회원정보로 토큰요청정보를 생성한다.
     * When. 토큰 발급(로그인) 요청을 시도한다.
     * Then. 로그인이 실패한다.
     */
    @DisplayName("로그인 실패 시 UNAUTHORIZED 응답이 반환된다.")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        String wrongEmail = "wwww@wwww.com";
        String wrongPassword = "wwww";
        TokenRequest wrongTokenRequest = TokenRequest.of(wrongEmail, wrongPassword);

        // when
        ExtractableResponse<Response> response = 로그인_요청(wrongTokenRequest);

        // then
        로그인_실패(response);
    }

    /**
     * Given. 유효하지 않은 인증 토큰
     * When. 토큰을 이용하여 회원 정보를 조회한다.
     * Then. 토큰 인증에 실패한다.
     */
    @DisplayName("유효하지 않은 토큰으로 회원정보 조회 시 UNAUTHORIZED 응답이 반환된다.")
    @ParameterizedTest
    @ValueSource(strings = {"1", "aa", "bbb", "asdofina", "!@#!@#!@#"})
    void myInfoWithWrongBearerAuth(String accessToken) {
        // given
        TokenResponse tokenResponse = new TokenResponse(accessToken);

        // when
        ExtractableResponse<Response> response = 로그인한_회원_정보_요청(tokenResponse);

        // then
        토큰_인증_실패(response);
    }
    /**
     * Given. 정상적인 로그인을 통해 발급받은 토큰
     * When. 토큰을 이용하여 회원 정보를 조회한다.
     * Then. 토큰 인증에 성공한다.
     */
    @DisplayName("정상적인 토큰으로 회원정보 조회 시 정상 조회가 된다.")
    @Test
    void myInfoWithBearerAuth01() {
        // Given
        TokenResponse tokenResponse = 로그인_요청(TokenRequest.of(손성현_EMAIL, 손성현_PASSWORD)).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 로그인한_회원_정보_요청(tokenResponse);

        // then
        토큰_인증_성공(response);
    }
}
